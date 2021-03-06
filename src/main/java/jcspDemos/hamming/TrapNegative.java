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

/**
 * @author P.H. Welch
 */
public final class TrapNegative implements CSProcess {

  private final ChannelInputInt in;
  private final ChannelOutputInt out;
  private final ChannelOutputInt trap;

  public TrapNegative (final ChannelInputInt in, final ChannelOutputInt out,
                       final ChannelOutputInt trap) {
    this.in = in;
    this.out = out;
    this.trap = trap;
  }

  public void run () {
    int count = 0;
    int i = in.read ();
    while (i >= 0) {
      count++;
      out.write (i);
      i = in.read ();
    }
    trap.write (count);
  }

}
