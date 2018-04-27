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
import jcsp.plugNplay.*;

public class SignExample {

  public static void main (String[] argv) {

    final One2OneChannel[] a = Channel.one2oneArray (3);
    final One2OneChannel[] b = Channel.one2oneArray (3);
    final One2OneChannel c = Channel.one2one ();

    new Parallel (
      new CSProcess[] {
        new Numbers (a[0].out ()),
        new Fibonacci (a[1].out ()),
        new Squares (a[2].out ()),
        new Sign ("Numbers ", a[0].in (), b[0].out ()),
        new Sign ("            Fibonacci ", a[1].in (), b[1].out ()),
        new Sign ("                          Squares ", a[2].in (), b[2].out ()),
        new Plex (Channel.getInputArray (b), c.out ()),
        new Printer (c.in (), "", "\n")
      }
    ).run ();

  }

}
