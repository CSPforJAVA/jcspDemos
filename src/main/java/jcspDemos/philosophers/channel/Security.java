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
class Security implements CSProcess {

  // protected attributes

  protected AltingChannelInput down, up;
  protected ChannelOutput report;

  protected int maxSitting;

  protected SecurityReport[] seated;

  // constructors

  public Security (AltingChannelInput down, AltingChannelInput up,
                   ChannelOutput report, int nMaxSitting) {
    this.down = down;
    this.up = up;
    this.report = report;
    this.maxSitting = nMaxSitting;
    this.seated = new SecurityReport [nMaxSitting + 1];
    for (int i = 0; i < (maxSitting + 1); i++) {
      seated[i] = new SecurityReport (i);
    }
  }

  // public methods

  public void run () {

    Alternative alt = new Alternative (new Guard[] {down, up});
    boolean[] precondition = {true, true};
    final int DOWN = 0;
    final int UP = 1;

    int nSitting = 0;

    while (true) {
      precondition[DOWN] = (nSitting < maxSitting);
      switch (alt.fairSelect (precondition)) {
        case DOWN:
          down.read ();
          nSitting++;
          report.write (seated[nSitting]);
        break;
        case UP:
          up.read ();
          nSitting--;
          report.write (seated[nSitting]);
        break;
      }
    }
  }

}
