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
import jcsp.plugNplay.Printer;

class FairPlexTest {

  public static void main (String[] args) {

    final One2OneChannel[] a = Channel.one2oneArray (5);
    final One2OneChannel b = Channel.one2one ();

    new Parallel (
      new CSProcess[] {
        new Regular (a[0].out (), 0, 5),
        new Regular (a[1].out (), 1, 5),
        new Regular (a[2].out (), 2, 5),
        new Regular (a[3].out (), 3, 5),
        new Regular (a[4].out (), 4, 5),
        new FairPlex (Channel.getInputArray (a), b.out ()),
        new Printer (b.in (), "FairPlexTest ==> ", "\n")
      }
    ).run ();

  }

}
