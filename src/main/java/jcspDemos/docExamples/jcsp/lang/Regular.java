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

package jcspDemos.docExamples.jcsp.lang;

import jcsp.lang.*;
 
public class Regular implements CSProcess {
 
  final private ChannelOutput out;
  final private Integer N;
  final private long interval;
 
  public Regular (final ChannelOutput out, final int n, final long interval) {
    this.out = out;
    this.N = new Integer (n);
    this.interval = interval;
  }
 
  public void run () {
 
    final CSTimer tim = new CSTimer ();
    long timeout = tim.read ();       // read the (absolute) time once only
 
    while (true) {
      out.write (N);
      timeout += interval;            // set the next (absolute) timeout
      tim.after (timeout);            // wait until that (absolute) timeout
    }
  }
 
}
