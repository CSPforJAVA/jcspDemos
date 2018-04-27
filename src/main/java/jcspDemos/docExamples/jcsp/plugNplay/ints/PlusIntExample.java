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
import jcsp.plugNplay.ints.*;

public class PlusIntExample {

  public static void main (String[] argv) {

    final One2OneChannelInt a = Channel.one2oneInt ();
    final One2OneChannelInt b = Channel.one2oneInt ();
    final One2OneChannelInt c = Channel.one2oneInt ();

    new Parallel (
      new CSProcess[] {
        new NumbersInt (a.out ()),
        new NumbersInt (b.out ()),
        new PlusInt (a.in (), b.in (), c.out ()),
        new PrinterInt (c.in (), "--> ", "\n")
      }
    ).run ();

  }

}
