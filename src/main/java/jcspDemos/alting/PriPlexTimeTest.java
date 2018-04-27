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
public class PriPlexTimeTest {

  public static final String TITLE = "Prioritised multiplexing (pri-Alt)";
  public static final String DESCR =
  		"Shows a pri-Alt in action. Five processes are created which generate numbers at 5ms intervals. " +
  		"A multiplexer will use 'priSelect' to serve lower numbered processes first. Contrast this with " +
  		"the fair multiplexor. Higher numbered processes will be starved and the timeout to stop the " +
  		"demonstration after 10 seconds may never be serviced.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final One2OneChannel[] a = Channel.one2oneArray(5);
    final One2OneChannel b = Channel.one2one();

    new Parallel (                         // this won't see the higher
      new CSProcess[] {                    // indexed guards, including
        new Regular (a[0].out(), 0, 5),          // the timeout ... probably.
        new Regular (a[1].out(), 1, 5),
        new Regular (a[2].out(), 2, 5),
        new Regular (a[3].out(), 3, 5),
        new Regular (a[4].out(), 4, 5),
        new PriPlexTime (Channel.getInputArray(a), b.out(), 10000),
        new Printer (b.in(), "PriPlexTimeTest ==> ", "\n")
      }
    ).run ();

  }

}
