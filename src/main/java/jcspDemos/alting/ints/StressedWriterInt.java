//////////////////////////////////////////////////////////////////////
//                                                                  //
//  jcspDemos Demonstrations of the JCSP ("CSP for Java") Library   //
//  Copyright (C) 1996-2018 Peter Welch, Paul Austin and Neil Brown //
//                2001-2004 Quickstone Technologies Limited         //
//                2005-2018 Kevin Chalmers                          //
//                                                                  //
//  You may use this work under the terms of either                 //
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

package jcspDemos.alting.ints;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class StressedWriterInt implements CSProcess {

  private final ChannelOutputInt out;
  private final int channel;
  private final int writer;
  private final int nWritersPerChannel;
  private final String id;

  public StressedWriterInt (ChannelOutputInt out, int channel, int writer,
                            final int nWritersPerChannel) {
    this.out = out;
    this.channel = channel;
    this.writer = writer;
    this.nWritersPerChannel = nWritersPerChannel;
    this.id = "channel " + channel + " writer " + writer;
  }

  public void run () {
    int n = writer;
    while (true) {
      // for (int i = 0; i < writer; i++) System.out.print ("  ");
      // System.out.println (id + " " + (n / nWritersPerChannel));
      out.write (n);
      n += nWritersPerChannel;
    }
  }

}
