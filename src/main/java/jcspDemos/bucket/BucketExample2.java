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

/**
 * @author P.H. Welch
 */
public class BucketExample2 {

  public static final String TITLE = "Flying Dingbats";
  public static final String DESCR =
    "Shows the use of a number of buckets to control the actions of a number of workers (dingbats). Each " +
      "dingbat will perform one unit of work and then fall into one of the buckets. The bucket keeper will " +
      "flush one bucket per cycle, possibly setting one or more dingbats to work again.";

  public static void main(String[] args) {

    final int minDingbat = 2;
    final int maxDingbat = 10;
    final int nDingbats = (maxDingbat - minDingbat) + 1;

    final int nBuckets = 2 * maxDingbat;

    final Bucket[] bucket = Bucket.create(nBuckets);

    final int second = 1000;     // JCSP timer units are milliseconds
    final int tick = second;
    final BucketKeeper bucketKeeper = new BucketKeeper(tick, bucket);

    final Dingbat[] dingbats = new Dingbat[nDingbats];
    for (int i = 0; i < dingbats.length; i++) {
      dingbats[i] = new Dingbat(i + minDingbat, bucket);
    }

    new Parallel(
      new CSProcess[]{
        bucketKeeper,
        new Parallel(dingbats)
      }
    ).run();

  }

}
