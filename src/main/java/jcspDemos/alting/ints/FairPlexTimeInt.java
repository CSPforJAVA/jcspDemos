//////////////////////////////////////////////////////////////////////
//                                                                  //
//  jcspDemos Demonstrations of the JCSP ("CSP for Java") Library   //
//  Copyright (C) 1996-2018 Peter Welch, Paul Austin and Neil Brown //
//                2001-2004 Quickstone Technologies Limited         //
//                2005-2018 Kevin Chalmers                          //
//                                                                  //
//  You may use this work under the terms of either                 //
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

package jcspDemos.alting.ints;

import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class FairPlexTimeInt implements CSProcess {

  private final AltingChannelInputInt[] in;
  private final ChannelOutputInt out;
  private final long timeout;

  public FairPlexTimeInt (final AltingChannelInputInt[] in, final ChannelOutputInt out,
                          final long timeout) {
    this.in = in;
    this.out = out;
    this.timeout = timeout;
  }

  public void run () {

    final Guard[] guards = new Guard[in.length + 1];
    System.arraycopy (in, 0, guards, 0, in.length);

    final CSTimer tim = new CSTimer ();
    final int timerIndex = in.length;
    guards[timerIndex] = tim;

    final Alternative alt = new Alternative (guards);

    boolean running = true;
    tim.setAlarm (System.currentTimeMillis () + timeout);
    while (running) {
      final int index = alt.fairSelect ();
      if (index == timerIndex) {
        running = false;
      } else {
        out.write (in[index].read ());
      }
    }
    System.out.println ("Goodbye from FairPlexTime ...");
    System.exit(0);
  }

}
