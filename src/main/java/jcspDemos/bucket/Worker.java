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

package jcspDemos.bucket;



import jcsp.lang.*;
import java.util.*;

/**
 * @author P.H. Welch
 */
public class Worker implements CSProcess {

  private final int id;
  private final long seed;
  private final int maxWork;
  private final Bucket bucket;

  public Worker (int id, long seed, int maxWork, Bucket bucket) {
    this.id = id;
    this.seed = seed;
    this.maxWork = maxWork;
    this.bucket = bucket;
  }

  public void run () {

    final Random random = new Random (seed);        // each process gets a different seed

    final CSTimer tim = new CSTimer ();

    final String working = "\t... Worker " + id + " working ...";
    final String falling = "\t\t\t     ... Worker " + id + " falling ...";
    final String flushed = "\t\t\t\t\t\t  ... Worker " + id + " flushed ...";

    while (true) {
      System.out.println (working);               // these lines represent
      int sleepTime = (random.nextInt() & 0x7FFFFFFF) % maxWork;
      tim.sleep (sleepTime);       // one unit of work
      
      System.out.println (falling);
      bucket.fallInto ();
      System.out.println (flushed);
    }
  }

}
