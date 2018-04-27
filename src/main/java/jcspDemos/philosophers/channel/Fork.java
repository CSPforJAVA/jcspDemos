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
class Fork implements CSProcess {

  // protected attributes

  protected int id;
  protected Integer Id;
  protected AltingChannelInput left, right;
  protected ChannelOutput report;

  protected ForkReport leftUp, leftDown, rightUp, rightDown;

  // constructors

  public Fork (int nPhilosophers, int id,
               AltingChannelInput left, AltingChannelInput right,
               ChannelOutput report) {
    this.id = id;
    Id = new Integer(id);
    this.left = left;
    this.right = right;
    this.report = report;
    leftUp = new ForkReport (id, id, ForkReport.UP);
    leftDown = new ForkReport (id, id, ForkReport.DOWN);
    rightUp = new ForkReport ((id + 1) % nPhilosophers, id, ForkReport.UP);
    rightDown = new ForkReport ((id + 1) % nPhilosophers, id, ForkReport.DOWN);
  }

  // public methods

  public void run () {
    Alternative alt = new Alternative (new Guard[] {left, right});
    final int LEFT = 0;
    final int RIGHT = 1;
    while (true) {
      switch (alt.fairSelect ()) {
        case LEFT:
          left.read ();
          report.write (leftUp);
          left.read ();
          report.write (leftDown);
        break;
        case RIGHT:
          right.read ();
          report.write (rightUp);
          right.read ();
          report.write (rightDown);
        break;
      }
    }
  }

}
