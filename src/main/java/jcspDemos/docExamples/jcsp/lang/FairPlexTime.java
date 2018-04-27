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

package jcspDemos.docExamples.jcsp.lang;

import jcsp.lang.*;

public class FairPlexTime implements CSProcess {

  private final AltingChannelInput[] in;
  private final ChannelOutput out;
  private final long timeout;

  public FairPlexTime (final AltingChannelInput[] in, final ChannelOutput out,
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
    tim.setAlarm (tim.read () + timeout);
    while (running) {
      final int index = alt.fairSelect ();
      // final int index = alt.priSelect ();
      if (index == timerIndex) {
        running = false;
      } else {
        out.write (in[index].read ());
      }
    }
    System.out.println ("\n\r\tFairPlexTime: timed out ... poisoning all channels ...");
    for (int i = 0; i < in.length; i++) {
      in[i].poison (42);
    }
    out.poison (42);

  }

}
