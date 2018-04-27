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

package jcspDemos.eratosthenes;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class Primes implements CSProcess {

  private final ChannelOutputInt out;
  
  public Primes (final ChannelOutputInt out) {
    this.out = out;
  }
  
  public void run () {
    out.write (2);
    One2OneChannelInt c = Channel.one2oneInt ();
    new Parallel (
      new CSProcess[] {
        new NumbersFrom (3, 2, c.out ()),
        new Sieve (c.in (), out)
      }
    ).run ();
  }

}
