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
class Security implements CSProcess {

  // protected attributes

  protected AltingChannelInputInt down, up;
  protected ChannelOutputInt report;

  protected final int maxSitting;

  // constructors

  public Security (AltingChannelInputInt down, AltingChannelInputInt up,
                   ChannelOutputInt report, int maxSitting) {
    this.down = down;
    this.up = up;
    this.report = report;
    this.maxSitting = maxSitting;
  }

  // public methods

  public void run () {

    final Alternative alt = new Alternative (new Guard[] {down, up});
    boolean[] precondition = {true, true};
    final int DOWN = 0;
    final int UP = 1;

    int nSitting = 0;

    while (true) {
      report.write (nSitting);
      precondition[DOWN] = (nSitting < maxSitting);
      switch (alt.fairSelect (precondition)) {
        case DOWN:
          down.read ();
          nSitting++;
        break;
        case UP:
          up.read ();
          nSitting--;
        break;
      }
    }
  }

}
