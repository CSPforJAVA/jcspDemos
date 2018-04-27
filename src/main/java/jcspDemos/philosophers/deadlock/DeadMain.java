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

package jcspDemos.philosophers.deadlock;



import jcsp.lang.*;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class DeadMain {

  public static final String TITLE = "Dining Philosophers [deadlocking]";
  public static final String DESCR =
  	"Shows the 'dining philosophers' deadlock problem. Each of the philosophers attempts to claim the " +
  	"shared resources (forks) via channels (which may block). Deadlock may occur if all philosophers are " +
  	"seated and each holds one fork. This is a non-deterministic system and so the point at which deadlock " +
  	"occurs (if at all) cannot be predicted. When deadlock occurs, the clock will continue ticking but " +
  	"no actions will be written to the screen by the philosophers as each is waiting to pick up another " +
  	"fork.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.addPrompt ("philosophers", 1, 100, 5);
  	Ask.show ();
  	final int nPhilosophers = Ask.readInt ("philosophers");
  	Ask.blank ();

    Any2OneChannel report = Channel.any2one ();

    new Parallel (
      new CSProcess[] {
        new DiningPhilosophersCollege (nPhilosophers, report.out ()),
        new TextDisplay (nPhilosophers, report.in ())
      }
    ).run ();
  }

}
