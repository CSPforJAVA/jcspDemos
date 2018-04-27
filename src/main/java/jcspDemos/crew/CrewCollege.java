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

package jcspDemos.crew;



//|
//|                       The Scribbling Philosophers
//|                       ===========================
//|
//| This program demonstrates the Crew class for Concurrent-read-exclusive-write
//| access to a shared object.
//|
//| A college consists of five philosophers and a blackboard.  The philosophers spend
//| their time between thinking, looking at what others have written on the blackboard
//| and scribbling on the blackboard themselves.
//|
//| Access to the blackboard is a bit constrained, so only one at a time may scribble.
//| Any number of philosophers may read the blackboard simultaenously, but not whilst
//| a scribbler is scribbling -- space is so tight that someone scribbling on the board
//| completely blocks the view.
//|
//|                              ______________
//|                              |            |       _______________
//|                              | blackboard |       |             |
//|    ---------------------<->--|            |   /-<-|  TimeKeeper |
//|      |   |   |   |   |       |   (CREW)   |   |   |_____________|
//|      :)  :)  :)  :)  :)      |____________|   |
//|      1   2   3   4   5                        v    _______________
//|      |   |   |   |   |                        |    |             |
//|    ---------------------->-------------------------| TextDisplay |
//|                   display/displayInfo              |_____________|
//|
//|

import jcsp.lang.*;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class CrewCollege {

  public static final String TITLE = "Scribbling Philosophers";
  public static final String DESCR =
  	"Shows the use of the Crew class for Concurrent-read-exclusive-write access to a shared object.\n\n" +

	"A college consists of five philosophers and a blackboard.  The philosophers spend their time between " +
	"thinking, looking at what others have written on the blackboard and scribbling on the blackboard " +
	"themselves.\n\n" +

	"Access to the blackboard is a bit constrained, so only one at a time may scribble. Any number of " +
	"philosophers may read the blackboard simultaenously, but not whilst a scribbler is scribbling -- " +
	"space is so tight that someone scribbling on the board completely blocks the view.";

  public static void main (String argv[]) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final int n_philosophers = 10;
    final int blackboard_size = 10;

    final int[] blackboard = new int[blackboard_size];
    for (int i = 0; i < blackboard_size; i++) {             // initially, there is
      blackboard[i] = -1;                                   // garbage in the shared
    }                                                       // resource (blackboard)

    final Crew crewBlackboard = new Crew (blackboard);

    final Any2OneChannelInt display = Channel.any2oneInt ();
    final One2OneChannelInt displayInfo = Channel.one2oneInt ();

    final CrewPhilosopher[] phil = new CrewPhilosopher[n_philosophers];
    for (int i = 0; i < n_philosophers; i++) {
      phil[i] = new CrewPhilosopher (i, crewBlackboard, display.out (), displayInfo.out ());
    }

    final TimeKeeper timeKeeper = new TimeKeeper (display.out (), displayInfo.out ());

	final CSProcess crewDisplay;

	/*if (argv.length == 1) {
		if (argv[0].equals("vt100")) {
		    crewDisplay = new VT100Display (display.in (), displayInfo.in ());
		} else if (argv[0].equals ("awt")) {
			crewDisplay = new AWTDisplay (display.in (), displayInfo.in ());
		} else {
		    crewDisplay = new CrewDisplay (display.in (), displayInfo.in ());
		}
	} else {
	    crewDisplay = new CrewDisplay (display.in (), displayInfo.in ());
	}*/
	crewDisplay = new AWTDisplay (display.in (), displayInfo.in ());

    new Parallel (
      new CSProcess[] {
        new Parallel (phil),
        timeKeeper,
        crewDisplay
      }
    ).run ();

  }

}

