//////////////////////////////////////////////////////////////////////
//                                                                  //
//  jcspDemos Demonstrations of the JCSP ("CSP for Java") Library   //
//  Copyright (C) 1996-2018 Peter Welch, Paul Austin and Neil Brown //
//                2001-2004 Quickstone Technologies Limited         //
//                2005-2018 Kevin Chalmers                          //
//                                                                  //
//  You may use this work under the terms of either                 //
//  1. The Apache License, Version 2.0                              //
//  2. or (at your option), the GNU Lesser General Public License,  //
//       version 2.1 or greater.                                    //
//                                                                  //
//  Full licence texts are included in the LICENCE file with        //
//  this library.                                                   //
//                                                                  //
//  Author contacts: P.H.Welch@kent.ac.uk K.Chalmers@napier.ac.uk   //
//                                                                  //
//////////////////////////////////////////////////////////////////////

package jcspDemos.splat;



import jcsp.lang.*;
import jcsp.awt.*;

/**
 * @author P.H. Welch
 */
class SplatterControl implements CSProcess {

  protected ActiveButtonControl control;

  protected AltingChannelInput[] event;
    // from buttons ...
  protected ChannelOutput[] configure;     // to buttons ...

  protected ChannelOutput report;          // to whoever is out there ...

  public final static int restart = 0;     // these are the state names ...
  public final static int frozen = 1;
  public final static int clear = 2;
  public final static int splatting = 3;
  public final static int unsplatting = 4;

  public final static int NUMBER = 5;      // this is the number of buttons

  public final static int RESTART = 0;     // these are the button names ...
  public final static int FREEZE = 1;
  public final static int CLEAR = 2;
  public final static int SPLAT = 3;
  public final static int UNSPLAT = 4;

  protected String[][] label               // these are the button labels ...
    = {new String[] {    "restart", "RESTART"}, 
       new String[] {     "frozen",  "FREEZE"}, 
       new String[] {      "clear",   "CLEAR"}, 
       new String[] {  "splatting",   "SPLAT"}, 
       new String[] {"unsplatting", "UNSPLAT"}};

  protected int[][] labelId                // which label to use next ...
    = {new int[] {0, 0, 1, 1, 0},                               // restart
       new int[] {1, 0, 1, 1, 1},                               // frozen
       new int[] {1, 0, 0, 0, 1},                               // clear
       new int[] {1, 1, 1, 0, 1},                               // splatting
       new int[] {1, 1, 1, 1, 0}};                              // unsplatting

  protected boolean[][] enable             // next button enable status ...
    = {new boolean[] {false, false,  true,  true, false},       // restart
       new boolean[] { true, false,  true,  true,  true},       // frozen
       new boolean[] { true, false, false, false,  true},       // clear
       new boolean[] { true,  true,  true, false,  true},       // splatting
       new boolean[] { true,  true,  true,  true, false}};      // unsplatting

  protected int[][] nextState
    = {new int[] {0, 0, 2, 3, 0},                               // restart
       new int[] {0, 1, 2, 3, 4},                               // frozen
       new int[] {0, 2, 2, 2, 4},                               // clear
       new int[] {0, 1, 2, 3, 4},                               // splatting
       new int[] {0, 1, 2, 3, 4}};                              // unsplatting

  protected ActiveButtonState[] makeState () {
    System.out.println ("SplatterControl making ActiveButtonState[] ...");
    ActiveButtonState[] state = null;
    try {
      state = new ActiveButtonState[enable.length];
      for (int i = 0; i < state.length; i++) {
        state[i] = new ActiveButtonState (labelId[i], enable[i],
                                             nextState[i]);
      }
    } catch (ActiveButtonState.BadArguments e) {
      System.out.println (e);
      System.exit (0);
    }
    System.out.println ("SplatterControl made ActiveButtonState[]");
    return state;
  }

  public SplatterControl (AltingChannelInput[] event,
                          ChannelOutput[] configure,
                          ChannelOutput report) {
    System.out.println ("SplatterControl creating ...");
    try {
      control
        = new ActiveButtonControl
            (event, configure, report, label, makeState (), 0);
    } catch (ActiveButtonControl.BadArguments e) {
      System.out.println (e);
      System.exit (0);
    }
    this.event = event;
    this.configure = configure;
    this.report = report;
    System.out.println ("SplatterControl created");
  }

  public void run () {
    System.out.println ("SplatterControl starting ...");
    control.run ();
  }

}
