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
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class InfectMain extends ActiveApplet {

  public static final String TITLE = "Infection";
  public static final String DESCR =
  	"Shows a thread-safe simulation strategy with the simulation thread polling the event channels that " +
  	"control it. User interface controls generate the events for these channels. Whenever no data is " +
  	"available on a channel the simulation thread generates the next frame. The simulation is a cellular " +
  	"automata representing the spread of an infection. The slider controls the rate of spread.";

  public static final int minWidth = 350;
  public static final int maxWidth = 1024;

  public static final int minHeight = 350;
  public static final int maxHeight = 768;

  public static final int minRate = 0;
  public static final int maxRate = 100;
  public static final int standbyRate = 35;

  public void init () {
    final int rate = getAppletInt ("rate", minRate, maxRate, standbyRate);
    setProcess (new InfectNetwork (rate, this));
  }

  public static void main (String[] args) {

    Ask.app (TITLE, DESCR);
    Ask.addPrompt ("width", minWidth, maxWidth, 640);
    Ask.addPrompt ("height", minHeight, maxHeight, 480);
    Ask.addPrompt ("rate", minRate, maxRate, standbyRate);
    Ask.show ();
    final int width = Ask.readInt ("width");
    final int height = Ask.readInt ("height");
    final int rate = Ask.readInt ("rate");
    Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame (TITLE);
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final InfectNetwork infect = new InfectNetwork (rate, activeFrame);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        infect
      }
    ).run ();

  }

}
