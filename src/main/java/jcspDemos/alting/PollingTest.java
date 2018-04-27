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
import jcsp.plugNplay.Printer;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class PollingTest {

  public static final String TITLE = "Polling Multiplexor";
  public static final String DESCR =
 		"Shows a pri-Alt with a skip guard being used to poll the inputs. Five processes generate numbers " +
  		"at 1s, 2s, 3s, 4s and 5s intervals. The number generated indicates the process generating it. If " +
  		"no data is available on a polling cycle the polling process will wait for 400ms before polling " +
  		"again. It could however be coded to perform some useful computation between polling cycles.\n" +
  		"\n" +
  		"The polling is unfair although this is not noticeable with these timings. If the interval at " +
  		"which the numbers are generated is shortened then the higher numbered processes may become starved.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final One2OneChannel[] a = Channel.one2oneArray(5);
    final One2OneChannel b = Channel.one2one();

    new Parallel (
      new CSProcess[] {
        new Regular (a[0].out(), 1, 1000),
        new Regular (a[1].out(), 2, 2000),
        new Regular (a[2].out(), 3, 3000),
        new Regular (a[3].out(), 4, 4000),
        new Regular (a[4].out(), 5, 5000),
        new Polling (a[0].in(), a[1].in(), a[2].in(), a[3].in(), a[4].in(), b.out()),
        new Printer (b.in(), "PollingTest ==> ", "\n")
      }
    ).run ();

  }

}
