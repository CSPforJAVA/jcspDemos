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

/**
 * @author P.H. Welch
 */
class Clock implements CSProcess {

  // protected attributes

  protected static final int seconds = 1000;
  protected static final int period = 1*seconds;

  protected ChannelOutput report;

  // constructors

  public Clock (ChannelOutput report) {
    this.report = report;
  }

  // public methods

  public void run () {
    int tick = 0;
    while (true) {
      report.write (new ClockReport (tick));
      sleep (period);
      tick++;
    }
  }

  // protected methods

  protected void sleep (long t) {
    try {
      Thread.sleep (t);
    } catch (InterruptedException e) {};
  }

}
