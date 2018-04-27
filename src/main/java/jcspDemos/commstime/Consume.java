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

package jcspDemos.commstime;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class Consume implements CSProcess {

  private int nLoops;
  private ChannelInputInt in;

  public Consume (int nLoops, ChannelInputInt in) {
    this.nLoops = nLoops;
    this.in = in;
  }

  public void run () {

    int x = -1;
    int warm_up = 1000;
    System.out.print ("warming up ... ");
    for (int i = 0; i < warm_up; i++) {
      x = in.read ();
    }
    System.out.println ("last number received = " + x);

    System.out.println ("1000 cycles completed ... timing now starting ...");

    while (true) {

      long t0 = System.currentTimeMillis ();
      for (int i = 0; i < nLoops; i++) {
        x = in.read ();
      }
      long t1 = System.currentTimeMillis ();

      System.out.println ("last number received = " + x);
      long microseconds   = (t1 - t0) * 1000;
      long timePerLoop_us = (microseconds / ((long) nLoops));
      System.out.println ("   " + timePerLoop_us + " microseconds / iteration");
      timePerLoop_us = (microseconds / ((long) (4*nLoops)));
      System.out.println ("   " + timePerLoop_us + " microseconds / communication");
      timePerLoop_us = (microseconds / ((long) (8*nLoops)));
      System.out.println ("   " + timePerLoop_us + " microseconds / context switch");

    }

  }

}
