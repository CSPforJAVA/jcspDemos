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
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class BucketExample1 {

  public static final String TITLE = "Bucket Example 1";
  public static final String DESCR =
      "Shows the use of a single bucket to control a group of worker processes. A worker process will take " +
          "action for some time and then fall into the bucket. Another process will periodically flush the " +
          "bucket, setting any workers in it working again. Such a system can be used to simulate actions which " +
          "must start on a clock cycle (ie when the bucket is flushed). An individual action from a worker may " +
          "take any length of time but the next action will not start until the next clock cycle.";

  public static void main(String[] args) {

    Ask.app(TITLE, DESCR);
    Ask.show();
    Ask.blank();

    final int nWorkers = 10;

    final int second = 1000;                // JCSP timer units are milliseconds
    final int interval = 5 * second;
    final int maxWork = 10 * second;

    final long seed = new CSTimer().read();

    final Bucket bucket = new Bucket();

    final Flusher flusher = new Flusher(interval, bucket);

    final Worker[] workers = new Worker[nWorkers];
    for (int i = 0; i < workers.length; i++) {
      workers[i] = new Worker(i, i + seed, maxWork, bucket);
    }

    System.out.println("*** Flusher: interval = " + interval + " milliseconds");

    new Parallel(
        new CSProcess[]{
            flusher,
            new Parallel(workers)
        }
    ).run();

  }

}
