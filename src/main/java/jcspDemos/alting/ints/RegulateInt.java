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
public final class RegulateInt implements CSProcess {

  private final AltingChannelInputInt in, reset;
  private final ChannelOutputInt out;
  private final long initialInterval;

  public RegulateInt (final AltingChannelInputInt in, final AltingChannelInputInt reset,
                      final ChannelOutputInt out, final long initialInterval) {
    this.in = in;
    this.reset = reset;
    this.out = out;
    this.initialInterval = initialInterval;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();

    final Guard[] guards = {reset, tim, in};              // prioritised order
    final int RESET = 0;                                  // index into guards
    final int TIM = 1;                                    // index into guards
    final int IN = 2;                                     // index into guards

    final Alternative alt = new Alternative (guards);

    int x = 0;                                      // holding object

    long interval = initialInterval;

    long timeout = tim.read () + interval;
    tim.setAlarm (timeout);

    while (true) {
      switch (alt.priSelect ()) {
        case RESET:
          interval = (long) reset.read ();
          timeout = tim.read ();                          // fall through
        case TIM:
          out.write (x);
          timeout += interval;
          tim.setAlarm (timeout);
        break;
        case IN:
          x = in.read ();
        break;
      }
    }

  }

}
