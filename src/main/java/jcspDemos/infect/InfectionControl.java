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

package jcspDemos.infect;



import jcsp.lang.*;
import jcsp.awt.*;

/**
 * @author P.H. Welch
 */
class InfectionControl implements CSProcess {

  protected ActiveButtonControl control;

  protected AltingChannelInput[] event;
    // from buttons ...
  protected ChannelOutput[] configure;     // to buttons ...

  protected ChannelOutput report;          // to whoever is out there ...

  public final static int reset   = 0;     // these are the state names ...
  public final static int centre  = 1;
  public final static int random  = 2;
  public final static int running = 3;
  public final static int frozen  = 4;

  public final static int NUMBER = 4;      // this is the number of buttons

  public final static int RESET = 0;       // these are the button names ...
  public final static int RANDOM = 1;
  public final static int CENTRE = 2;
  public final static int FREEZE = 3;

  protected String[][] label               // these are the button labels ...
    = {new String[] {"reset", "RESET"}, 
       new String[] {"random", "RANDOM"}, 
       new String[] {"centre", "CENTRE"}, 
       new String[] {"start", "START", "FREEZE", "UNFREEZE"},
       new String[] {"dummy"}};

  protected int[][] labelId                // which label to use next ...
    = {new int[] {0, 1, 1, 0, 0},                          // reset
       new int[] {1, 0, 0, 1, 0},                          // centre
       new int[] {1, 1, 0, 1, 0},                          // random
       new int[] {0, 0, 0, 2, 0},                          // running
       new int[] {1, 0, 0, 3, 0}};                         // frozen

  protected boolean[][] enable             // next button enable status ...
    = {new boolean[] {false,  true,  true, false,  true},      // reset
       new boolean[] { true, false, false,  true,  true},      // centre
       new boolean[] { true,  true, false,  true,  true},      // random
       new boolean[] {false, false, false,  true,  true},      // running
       new boolean[] { true, false, false,  true,  true}};     // frozen

  protected int[][] nextState
    = {new int[] {0, 2, 1, 0, 0},                          // reset
       new int[] {0, 1, 1, 3, 1},                          // centre
       new int[] {0, 2, 2, 3, 2},                          // random
       new int[] {3, 3, 3, 4, 0},                          // running
       new int[] {0, 4, 4, 3, 4}};                         // frozen

  protected ActiveButtonState[] makeState () {
    System.out.println ("InfectionControl making ActiveButtonState[] ...");
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
    System.out.println ("InfectionControl made ActiveButtonState[]");
    return state;
  }

  public InfectionControl (AltingChannelInput[] event,
                           ChannelOutput[] configure,
                           ChannelOutput report) {
    System.out.println ("InfectionControl creating ...");
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
    System.out.println ("InfectionControl created");
  }

  public void run () {
    System.out.println ("InfectionControl starting ...");
    control.run ();
  }

}
