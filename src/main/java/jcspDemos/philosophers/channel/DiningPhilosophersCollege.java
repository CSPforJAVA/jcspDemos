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

package jcspDemos.philosophers.channel;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class DiningPhilosophersCollege implements CSProcess {

  private final int nPhilosophers;
  private final ChannelOutput report;

  public DiningPhilosophersCollege (int nPhilosophers, ChannelOutput report) {
    this.nPhilosophers = nPhilosophers;
    this.report = report;
  }

  public void run () {

    final One2OneChannel[] left = Channel.one2oneArray (nPhilosophers);
    final One2OneChannel[] right = Channel.one2oneArray (nPhilosophers);

    final Any2OneChannel down = Channel.any2one ();
    final Any2OneChannel up = Channel.any2one ();

    final Fork[] fork = new Fork[nPhilosophers];
    for (int i = 0; i < nPhilosophers; i++) {
      fork[i] = new Fork (nPhilosophers, i,
                          left[i].in (), right[(i + 1)%nPhilosophers].in (), report);
    }

    final Philosopher[] phil = new Philosopher[nPhilosophers];
    for (int i = 0; i < nPhilosophers; i++) {
      phil[i] = new Philosopher (i, left[i].out (), right[i].out (), down.out (), up.out (), report);
    }

    new Parallel (
      new CSProcess[] {
        new Parallel (phil),
        new Parallel (fork),
        new Security (down.in (), up.in (), report, nPhilosophers - 1),
        new Clock (report)
      }
    ).run ();

  }

}
