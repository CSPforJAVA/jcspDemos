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
public class Dingbat implements CSProcess {

  private final int id;
  private final Bucket[] bucket;

  public Dingbat (int id, Bucket[] bucket) {
    this.id = id;
    this.bucket = bucket;
  }

  public void run () {

    int logicalTime = 0;

    String[] spacer = new String[bucket.length];
    spacer[0] = "";
    for (int i = 1; i < spacer.length; i++) spacer[i] = spacer[i - 1] + "  ";

    String message = "Hello world from " + id + " ==> time = ";

    while (true) {
      logicalTime += id;
      final int slot = logicalTime % bucket.length;     // assume: id <= bucket.length
      bucket[slot].fallInto ();
      System.out.println (spacer[slot] + message + logicalTime);   // one unit of work
    }
  }

}
