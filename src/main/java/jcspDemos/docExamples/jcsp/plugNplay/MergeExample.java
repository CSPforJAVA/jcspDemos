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

package jcspDemos.docExamples.jcsp.plugNplay;

import jcsp.lang.*;
import jcsp.util.*;
import jcsp.plugNplay.*;

public class MergeExample {

  public static void main (String[] argv) {

    final One2OneChannel[] a = Channel.one2oneArray (5);
    final One2OneChannel[] b = Channel.one2oneArray (4, new InfiniteBuffer ());
    final One2OneChannel c = Channel.one2one ();
    final One2OneChannel d = Channel.one2one ();

    new Parallel (
      new CSProcess[] {
        new Mult (2, a[0].in (), b[0].out ()),
        new Mult (3, a[1].in (), b[1].out ()),
        new Mult (5, a[2].in (), b[2].out ()),
        new Mult (7, a[3].in (), b[3].out ()),
        new Merge (Channel.getInputArray (b), c.out ()),
        new Prefix (1, c.in (), d.out ()),
        new Delta (d.in (), Channel.getOutputArray (a)),
        new Printer (a[4].in (), "--> ", "\n")
      }
    ).run ();

  }

}
