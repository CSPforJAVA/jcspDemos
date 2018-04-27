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

package jcspDemos.docExamples.jcsp.plugNplay.ints;

import jcsp.lang.*;
import jcsp.util.ints.*;
import jcsp.plugNplay.ints.*;

public class Merge2IntExample {

  public static void main (String[] argv) {

    final One2OneChannelInt[] a = Channel.one2oneIntArray (4);
    final One2OneChannelInt[] b = Channel.one2oneIntArray (3, new InfiniteBufferInt ());
    final One2OneChannelInt c = Channel.one2oneInt ();
    final One2OneChannelInt d = Channel.one2oneInt ();
    final One2OneChannelInt e = Channel.one2oneInt ();

    new Parallel (
      new CSProcess[] {
        new MultInt (2, a[0].in (), b[0].out ()),
        new MultInt (3, a[1].in (), b[1].out ()),
        new MultInt (5, a[2].in (), b[2].out ()),
        new Merge2Int (b[0].in (), b[1].in (), c.out ()),
        new Merge2Int (c.in (), b[2].in (), d.out ()),
        new PrefixInt (1, d.in (), e.out ()),
        new DeltaInt (e.in (), Channel.getOutputArray (a)),
        new PrinterInt (a[3].in (), "--> ", "\n")
      }
    ).run ();

  }

}
