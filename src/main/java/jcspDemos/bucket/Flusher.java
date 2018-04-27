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
public class Flusher implements CSProcess {

  private final int interval;
  private final Bucket bucket;

  public Flusher (int interval, Bucket bucket) {
    this.interval = interval;
    this.bucket = bucket;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();
    long timeout = tim.read () + interval;

    while (true) {
      tim.after (timeout);
      System.out.println ("*** Flusher: about to flush ...");
      final int n = bucket.flush ();
      System.out.println ("*** Flusher: number flushed = " + n);
      timeout += interval;
    }
  }

}
