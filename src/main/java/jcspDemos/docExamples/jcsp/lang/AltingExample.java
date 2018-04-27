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

public class AltingExample implements CSProcess {

  private final AltingChannelInput in0, in1;
  
  public AltingExample (final AltingChannelInput in0,
                        final AltingChannelInput in1) {
    this.in0 = in0;
    this.in1 = in1;
  }

  public void run () {

    final Guard[] altChans = {in0, in1};
    final Alternative alt = new Alternative (altChans);

    while (true) {
      switch (alt.select ()) {
        case 0:
          System.out.println ("in0 read " + in0.read ());
        break;
        case 1:
          System.out.println ("in1 read " + in1.read ());
        break;
      }
    }

  }

}
