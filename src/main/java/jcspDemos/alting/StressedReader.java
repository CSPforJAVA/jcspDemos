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

package jcspDemos.alting;

import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class StressedReader implements CSProcess {

  private final AltingChannelInput[] c;
  private final int nWritersPerChannel;

  public StressedReader (final AltingChannelInput[] c,
                         final int nWritersPerChannel) {
    this.c = c;
    this.nWritersPerChannel = nWritersPerChannel;
  }

  public void run () {
    final int seconds = 1000;
    final int initialWait = 5;
    System.out.println ("\nWait (" + initialWait +
                        " seconds) for all the writers to get going ...");
    CSTimer tim = new CSTimer ();
    long timeout = tim.read () + (initialWait*seconds);
    tim.after (timeout);
    System.out.println ("OK - that should be long enough ...\n");
    int[][] n = new int[c.length][nWritersPerChannel];
    for (int channel = 0; channel < c.length; channel++) {
      for (int i = 0; i < nWritersPerChannel; i++) {
        StressedPacket stressedPacket = (StressedPacket) c[channel].read ();
        n[channel][stressedPacket.writer] = stressedPacket.n;
        for (int chan = 0; chan < channel; chan++) System.out.print ("  ");
        System.out.println ("channel " + channel +
                            " writer " + stressedPacket.writer +
                            " read " + stressedPacket.n);
      }
    }
    Alternative alt = new Alternative (c);
    int counter = 0, tock = 0;
    while (true) {
      if (counter == 0) {
        System.out.print ("Tock " + tock + " : ");
        int total = 0;
        for (int channel = 0; channel < n.length; channel++) {
          System.out.print (n[channel][tock % nWritersPerChannel] + " ");
          for (int i = 0; i < nWritersPerChannel; i++) {
            total += n[channel][i];
          }
        }
        System.out.println (": " + total);
        tock++;
        counter = 10000;
      }
      counter--;
      int channel = alt.fairSelect ();
      StressedPacket stressedPacket = (StressedPacket) c[channel].read ();
      n[channel][stressedPacket.writer] = stressedPacket.n;
      // for (int chan = 0; chan < channel; chan++) System.out.print ("  ");
      // System.out.println ("channel " + channel +
      //                     " writer " + stressedPacket.writer +
      //                     " read " + stressedPacket.n);
    }
  }

}
