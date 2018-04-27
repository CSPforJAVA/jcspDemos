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
class BucketKeeper implements CSProcess {

  private final long interval;
  private final Bucket[] bucket;

  public BucketKeeper (long interval, Bucket[] bucket) {
    this.interval = interval;
    this.bucket = bucket;
  }

  public void run () {

    String[] spacer = new String[bucket.length];
    spacer[0] = "";
    for (int i = 1; i < spacer.length; i++) spacer[i] = spacer[i - 1] + "  ";

    final CSTimer tim = new CSTimer ();
    long timeout = tim.read ();
    int index = 0;

    while (true) {
      final int n = bucket[index].flush ();
      if (n == 0) {
        System.out.println (spacer[index] + "*** bucket " + index + " was empty ...");
      }
      index = (index + 1) % bucket.length;
      timeout += interval;
      tim.after (timeout);
    }
  }

}
