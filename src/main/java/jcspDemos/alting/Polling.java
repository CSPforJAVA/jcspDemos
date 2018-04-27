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

package jcspDemos.alting;


import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class Polling implements CSProcess {

  private final AltingChannelInput in0;
  private final AltingChannelInput in1;
  private final AltingChannelInput in2;
  private final AltingChannelInput in3;
  private final AltingChannelInput in4;
  private final ChannelOutput out;

  public Polling (final AltingChannelInput in0, final AltingChannelInput in1,
                  final AltingChannelInput in2, final AltingChannelInput in3,
                  final AltingChannelInput in4, final ChannelOutput out) {
    this.in0 = in0;
    this.in1 = in1;
    this.in2 = in2;
    this.in3 = in3;
    this.in4 = in4;
    this.out = out;
  }

  public void run() {

    final Skip skip = new Skip ();
    final Guard[] guards = {in0, in1, in2, in3, in4, skip};
    final Alternative alt = new Alternative (guards);

    while (true) {
      switch (alt.priSelect ()) {
        case 0:
          // ...  process data pending on channel in0
          out.write (in0.read ());
        break;
        case 1:
          // ...  process data pending on channel in1
          out.write (in1.read ());
        break;
        case 2:
          // ...  process data pending on channel in2
          out.write (in2.read ());
        break;
        case 3:
          // ...  process data pending on channel in2
          out.write (in3.read ());
        break;
        case 4:
          // ...  process data pending on channel in2
          out.write (in4.read ());
        break;
        case 5:
          // ...  nothing available for the above ...
          // ...  so get on with something else for a while ...
          // ...  then loop around and poll again ...
          try {Thread.sleep (400);} catch (InterruptedException e) {}
          out.write ("...  so getting on with something else for a while ...");
        break;
      }
    }
  }
}
