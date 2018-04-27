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
 
public class AltingOutputExample implements CSProcess {

  private final AltingChannelOutput out0, out1;
  
  public AltingOutputExample (final AltingChannelOutput out0,
                              final AltingChannelOutput out1) {
    this.out0 = out0;
    this.out1 = out1;
  }

  public void run () {

    final Guard[] altChans = {out0, out1};
    final Alternative alt = new Alternative (altChans);

    while (true) {
      switch (alt.select ()) {
        case 0:
          out0.write (new Integer(0));
          System.out.println ("out0 written");
        break;
        case 1:
          out1.write (new Integer(1));
          System.out.println ("out1 written");
        break;
      }
    }

  }

}
