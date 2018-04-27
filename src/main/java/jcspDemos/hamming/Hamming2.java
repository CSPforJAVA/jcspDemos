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
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public final class Hamming2 {

  public static final String TITLE = "Hamming Codes";
  public static final String DESCR =
    "Uses an interesting network of processes to generate all positive integers with given sets of prime factors.";

  public static final long SECONDS = 1000;
  public static final long PAUSE = 5*SECONDS;

  public static void main (String[] args) {

    Ask.app (TITLE, DESCR);
    Ask.show ();
    Ask.blank ();

    CSTimer tim = new CSTimer ();

    final One2OneChannelInt trap = Channel.one2oneInt ();

    final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    Parallel.setUncaughtErrorDisplay (false);

    while (true) {

      for (int i = 2; i <= primes.length; i++)  {

        System.out.println ("\nAll positive ints whose prime factors contain only:\n");
        System.out.print ("  " + primes[0]);
        for (int j = 1; j < i; j++)  {
          System.out.print (", ");
          System.out.print (primes[j]);
        }
        System.out.println ("\n");
        System.out.println ("Pausing 5 seconds ...");
        tim.sleep (PAUSE);
        System.out.println ();

        final ProcessManager manager =
          new ProcessManager (new PrimeMultiples2 (primes, i, trap.out ()));

        manager.start ();                       // start up the managed process

        final int count = trap.in ().read ();   // wait for hamming numbers to overflow

        manager.interrupt ();                   // crude kill of the managed process

        System.out.println ("\n\nThere were " + count + " of them ...");

      }
    }

  }

}
