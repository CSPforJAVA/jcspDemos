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

package jcspDemos.hamming;



import jcsp.lang.*;
import jcsp.util.ints.*;
import jcsp.plugNplay.ints.*;

/**
 * @author P.H. Welch
 */
public final class PrimeMultiples2 implements CSProcess {

  private final ChannelOutputInt trap;

  private final int[] primes;
  private final int minPrimes = 2;

  private final int howMany;

  public PrimeMultiples2 (final int[] primes, final int howMany,
                          final ChannelOutputInt trap) {
    // assume these are distinct primes (and at least minPrimes of them)
    this.primes = primes;
    if (howMany < minPrimes) {
      this.howMany = minPrimes;
    } else if (howMany > primes.length) {
      this.howMany = primes.length;
    } else {
      this.howMany = howMany;
    }
    this.trap = trap;
  }

  public void run () {

    final One2OneChannelInt[] a = Channel.one2oneIntArray (howMany + 1);
    final One2OneChannelInt[] b = Channel.one2oneIntArray (howMany, new InfiniteBufferInt ());
    final One2OneChannelInt c = Channel.one2oneInt ();
    final One2OneChannelInt d = Channel.one2oneInt ();
    final One2OneChannelInt e = Channel.one2oneInt ();

    final CSProcess[] Multipliers = new CSProcess[howMany];
    for (int i = 0; i < howMany; i++) {
      Multipliers[i] = new MultInt (primes[i], a[i].in (), b[i].out ());
    }

    new Parallel (
      new CSProcess[] {
        new Parallel (Multipliers),
        new MergeInt (Channel.getInputArray (b), c.out ()),
        new PrefixInt (1, c.in (), d.out ()),
        new DeltaInt (d.in (), Channel.getOutputArray (a)),
        new TrapNegative (a[howMany].in (), e.out (), trap),
        new PrinterInt (e.in (), "", " ")
      }
    ).run ();
  }

}
