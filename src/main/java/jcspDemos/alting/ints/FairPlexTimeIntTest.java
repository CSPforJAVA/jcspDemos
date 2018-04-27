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
import jcsp.plugNplay.ints.PrinterInt;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class FairPlexTimeIntTest {

  public static final String TITLE = "Fair multiplexing (fair-Alt)  [with integers]";
  public static final String DESCR =
  		"Shows a fair-Alt in action. Five processes are created which generate numbers at 5ms intervals. " +
  		"A multiplexer will use 'fairSelect' to ensure that each of the channels gets served. The output " +
  		"shows which data was accepted by the multiplexer. The fairness ensures that the higher numbered " +
  		"channels do not get starved. A timeout guard is also used to stop the demonstration after 10 " +
  		"seconds. Contrast this with the Pri-Alting demonstration.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final One2OneChannelInt[] a = Channel.one2oneIntArray (5);
    final One2OneChannelInt b = Channel.one2oneInt ();

    new Parallel (
      new CSProcess[] {
        new RegularInt (a[0].out(), 0, 5),
        new RegularInt (a[1].out(), 1, 5),
        new RegularInt (a[2].out(), 2, 5),
        new RegularInt (a[3].out(), 3, 5),
        new RegularInt (a[4].out(), 4, 5),
        new FairPlexTimeInt (Channel.getInputArray(a), b.out(), 10000),
        new PrinterInt (b.in(), "FairPlexTimeTest ==> ", "\n")
      }
    ).run ();

  }

}
