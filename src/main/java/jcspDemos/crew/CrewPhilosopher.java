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
//| Each philosopher reads/writes a shared blackboard through a Crew-lock.
//|
//|                                crewBlackboard
//|             ------------------------<--->--------
//|                            |
//|                     _______|_________ 
//|                   |                 |
//|                   | CrewPhilosopher |
//|                   |      (n)        |
//|                   |_________________|
//|                            |
//|                            |
//|             -------------------------->----------
//|                               display/displayInfo
//|
//| Each philosopher reports its state through a shared channel (display).
//|

import jcsp.lang.*;
import java.util.*;

/**
 * @author P.H. Welch
 */
class CrewPhilosopher implements CSProcess {

  private final int id;

  private final Crew crewBlackboard;

  private final ChannelOutputInt display, displayInfo;

  public CrewPhilosopher (final int id, final Crew crewBlackboard,
                          final ChannelOutputInt display,
                          final ChannelOutputInt displayInfo) {
    this.id = id;
    this.crewBlackboard = crewBlackboard;
    this.display = display;
    this.displayInfo = displayInfo;
  }

  protected Random random;

  protected int range (int n) {
    int i = random.nextInt ();
    if (i < 0) {
      i = (i == Integer.MIN_VALUE) ? 42 : -i;
    }
    return i % n;
  }

  public void run () {

    final int[] blackboard = (int[]) crewBlackboard.getShared ();
    int scribble;

    final int milliseconds = 1;
    final int seconds = 1000*milliseconds;

    final int minThink = 2*seconds;
    final int maxThink = 10*seconds;
    final int minRead = 5*seconds;
    final int maxRead = 10*seconds;
    final int minWrite = 5*seconds;
    final int maxWrite = 10*seconds;

    final int readPercent = 80;

    final CSTimer tim = new CSTimer ();
    long t;

    t = tim.read ();                             // initial thoughts
    tim.after (t + ((15*id)*milliseconds));      // to randomise this
    random = new Random ();                      // random generator

    while (true) {

      display.write (PhilState.THINKING);
      displayInfo.write (id);

      t = tim.read ();                           // do the thinking
      tim.after (
        t + range (maxThink - minThink) + minThink
      );

      if (range (100) < readPercent) {

        display.write (PhilState.WANNA_READ);
        displayInfo.write (id);

        crewBlackboard.startRead ();

        display.write (PhilState.READING);
        displayInfo.write (id);

        t = tim.read ();                         // do the reading
        tim.after (
          t + range (maxRead - minRead) + minRead
        );
        scribble = blackboard[0];

        display.write (PhilState.DONE_READ);
        displayInfo.write (id);
        displayInfo.write (scribble);

        crewBlackboard.endRead ();

      } else {

        display.write (PhilState.WANNA_WRITE);
        displayInfo.write (id);

        crewBlackboard.startWrite ();

        display.write (PhilState.WRITING);
        displayInfo.write (id);

        t = tim.read ();                         // do the writing
        tim.after (
          t + range (maxWrite - minWrite) + minWrite
        );
        for (int i = 0; i < blackboard.length; i++) {
          blackboard[i] = id;
        }
        scribble = blackboard[0];

        display.write (PhilState.DONE_WRITE);
        displayInfo.write (id);
        displayInfo.write (scribble);

        crewBlackboard.endWrite ();

      }

    }

  }

}
