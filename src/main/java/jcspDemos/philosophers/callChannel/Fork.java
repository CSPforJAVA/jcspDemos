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
class Fork implements CSProcess {

  // protected attributes

  protected int id;
  protected AltingChannelInputInt left, right;
  protected ForkReport report;

  // constructors

  public Fork (int nPhilosophers, int id,
               AltingChannelInputInt left, AltingChannelInputInt right,
               ForkReport report) {
    this.id = id;
    this.left = left;
    this.right = right;
    this.report = report;
  }

  // public methods

  public void run () {
    final Alternative alt = new Alternative (new Guard[] {left, right});
    final int LEFT = 0;
    final int RIGHT = 1;
    int philId;
    while (true) {
      switch (alt.fairSelect ()) {
        case LEFT:
          philId = left.read ();
          report.forkUp (id, philId);
          philId = left.read ();
          report.forkDown (id, philId);
        break;
        case RIGHT:
          philId = right.read ();
          report.forkUp (id, philId);
          philId = right.read ();
          report.forkDown (id, philId);
        break;
      }
    }
  }

}
