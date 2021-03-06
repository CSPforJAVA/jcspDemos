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
 
public class FairPlex implements CSProcess {

  private final AltingChannelInput[] in;
  private final ChannelOutput out;
 
  public FairPlex (final AltingChannelInput[] in, final ChannelOutput out) {
    this.in = in;
    this.out = out;
  }
 
  public void run () {
 
    final Alternative alt = new Alternative (in);
 
    while (true) {
      final int index = alt.fairSelect ();
      out.write (in[index].read ());
    }
 
  }
 
}
