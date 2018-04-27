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
import jcsp.plugNplay.*;

class PollingTest {

  public static void main (String[] args) {

    final One2OneChannel[] a = Channel.one2oneArray (3);
    final One2OneChannel b = Channel.one2one ();

    new Parallel (
      new CSProcess[] {
        new Regular (a[0].out (), 100, 100),
        new Regular (a[1].out (), 250, 250),
        new Regular (a[2].out (), 1000, 1000),
        new Polling (a[0].in (), a[1].in (), a[2].in (), b.out ()),
        new Printer (b.in ())
      }
    ).run ();

  }

}
