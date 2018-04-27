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
public class PriPlexTimeIntTest {

  public static final String TITLE = "Prioritised multiplexing (pri-Alt) [with integers]";
  public static final String DESCR =
  		"Shows a pri-Alt in action. Five processes are created which generate numbers at 5ms intervals. " +
  		"A multiplexer will use 'priSelect' to serve lower numbered processes first. Contrast this with " +
  		"the fair multiplexor. Higher numbered processes will be starved and the timeout to stop the " +
  		"demonstration after 10 seconds may never be serviced.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final One2OneChannelInt[] a = Channel.one2oneIntArray(5);
    final One2OneChannelInt b = Channel.one2oneInt();

    new Parallel (                         // this won't see the higher
      new CSProcess[] {                    // indexed guards, including
        new RegularInt (a[0].out(), 0, 5),       // the timeout ... probably.
        new RegularInt (a[1].out(), 1, 5),
        new RegularInt (a[2].out(), 2, 5),
        new RegularInt (a[3].out(), 3, 5),
        new RegularInt (a[4].out(), 4, 5),
        new PriPlexTimeInt (Channel.getInputArray(a), b.out(), 10000),
        new PrinterInt (b.in(), "PriPlexTimeTest ==> ", "\n")
      }
    ).run ();

  }

}
