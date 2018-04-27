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

public class Polling implements CSProcess {

  private final AltingChannelInput in0;
  private final AltingChannelInput in1;
  private final AltingChannelInput in2;
  private final ChannelOutput out;

  public Polling (final AltingChannelInput in0, final AltingChannelInput in1,
                  final AltingChannelInput in2, final ChannelOutput out) {
    this.in0 = in0;
    this.in1 = in1;
    this.in2 = in2;
    this.out = out;
  }

  public void run() {

    final Skip skip = new Skip ();
    final Guard[] guards = {in0, in1, in2, skip};
    final Alternative alt = new Alternative (guards);
    final CSTimer tim = new CSTimer ();

    Object o;

    while (true) {
      switch (alt.priSelect ()) {
        case 0:
          o = in0.read ();
          out.write ("\tin0 ==> " + o + "\n");
        break;
        case 1:
          o = in1.read ();
          out.write ("\t\tin1 ==> " + o + "\n");
        break;
        case 2:
          o = in2.read ();
          out.write ("\t\t\tin2 ==> " + o + "\n");
        break;
        case 3:
          out.write ("skip ...\n");
          tim.after (tim.read () + 50);
        break;
      }
    }

  }

}
