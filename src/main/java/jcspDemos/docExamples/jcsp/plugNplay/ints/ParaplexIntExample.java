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

class ParaplexIntExample {

  public static void main (String[] args) {

    final One2OneChannelInt[] a = Channel.one2oneIntArray (3);
    final One2OneChannel b = Channel.one2one ();

    new Parallel (
      new CSProcess[] {
        new NumbersInt (a[0].out ()),
        new SquaresInt (a[1].out ()),
        new FibonacciInt (a[2].out ()),
        new ParaplexInt (Channel.getInputArray (a), b.out ()),
        new CSProcess () {
          public void run () {
            System.out.println ("\n\t\tNumbers\t\tSquares\t\tFibonacci\n");
            while (true) {
              int[] data = (int[]) b.in ().read ();
              for (int i = 0; i < data.length; i++) {
                System.out.print ("\t\t" + data[i]);
              }
              System.out.println ();
            }
          }
        }
      }
    ).run ();
  }

}
