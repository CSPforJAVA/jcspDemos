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

package jcspDemos.philosophers.callChannel;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class DiningPhilosophersCollege implements CSProcess {

  private final int nPhilosophers;
  private final int clockPeriod;
  private final PhilReport philReport;
  private final ForkReport forkReport;
  private final ChannelOutputInt securityReport;
  private final ChannelOutputInt clockReport;

  public DiningPhilosophersCollege (int nPhilosophers, int clockPeriod,
                                    PhilReport philReport,
                                    ForkReport forkReport,
                                    ChannelOutputInt securityReport,
                                    ChannelOutputInt clockReport) {
    this.nPhilosophers = nPhilosophers;
    this.clockPeriod = clockPeriod;
    this.philReport = philReport;
    this.forkReport = forkReport;
    this.securityReport = securityReport;
    this.clockReport = clockReport;
  }

  public void run () {

    final One2OneChannelInt[] left = Channel.one2oneIntArray (nPhilosophers);
    final One2OneChannelInt[] right = Channel.one2oneIntArray (nPhilosophers);

    final Any2OneChannelInt down = Channel.any2oneInt ();
    final Any2OneChannelInt up = Channel.any2oneInt ();

    final Fork[] fork = new Fork[nPhilosophers];
    for (int i = 0; i < nPhilosophers; i++) {
      fork[i] = new Fork (nPhilosophers, i,
                          left[i].in (), right[(i + 1)%nPhilosophers].in (), forkReport);
    }

    final Philosopher[] phil = new Philosopher[nPhilosophers];
    for (int i = 0; i < nPhilosophers; i++) {
      phil[i] = new Philosopher (i, left[i].out (), right[i].out (), down.out (), up.out (), philReport);
    }

    new Parallel (
      new CSProcess[] {
        new Parallel (phil),
        new Parallel (fork),
        new Security (down.in (), up.in (), securityReport, nPhilosophers - 1),
        new Clock (clockReport, clockPeriod)
      }
    ).run ();

  }

}
