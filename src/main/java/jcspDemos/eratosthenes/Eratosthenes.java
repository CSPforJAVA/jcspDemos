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
import jcsp.plugNplay.ints.*;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class Eratosthenes {

  public static final String TITLE = "Sieve of Eratosthenes";
  public static final String DESCR =
  	"Demonstrates the pipe-line approach to parallelisation, generating prime numbers by sieving. The " +
  	"sieve of Eratosthenes works by a feeder process pushing numbers into a pipeline of sieve processes. " +
  	"Each sieve process is allocated a prime number and discards any numbers in the pipeline divisible " +
  	"by that number. If a number reaches the end of the pipe it must be prime as no factors were found. " +
  	"It is printed and a new sieve process added to the pipeline to carry on searching for higher primes.\n" +
  	"\n" +
  	"This demonstration is a good way of stress testing the system for running a lot of processes.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    One2OneChannelInt c = Channel.one2oneInt ();
    new Parallel (
      new CSProcess[] {
        new Primes (c.out ()),
        new PrinterInt (c.in (), "--> ", "\n")
      }
    ).run ();
  }

}
