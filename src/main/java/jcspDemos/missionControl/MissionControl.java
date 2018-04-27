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

package jcspDemos.missionControl;



import jcsp.lang.*;
import jcsp.awt.*;
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class MissionControl {

  public static final String TITLE = "Mission Control";
  public static final String DESCR = "Demonstrates the handling of AWT button and timer events.";

  public static final int minStart = 10;
  public static final int maxStart = 1000;
  public static final int defaultStart = 300;

  public static final int minInterval = 10;
  public static final int maxInterval = 1000;
  public static final int defaultInterval = 10;

  public static void main(String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.addPrompt ("start", minStart, maxStart, defaultStart);
  	Ask.addPrompt ("interval", minInterval, maxInterval, defaultInterval);
  	Ask.show ();
  	final int start = Ask.readInt ("start");
  	final int interval = Ask.readInt ("interval");
  	Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame (TITLE);
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    final MissionControlNetwork net =
      new MissionControlNetwork (interval, start, activeFrame);

    activeFrame.pack ();
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        net
      }
    ).run ();

  }

}
