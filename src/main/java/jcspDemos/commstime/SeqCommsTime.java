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

package jcspDemos.commstime;



import jcsp.lang.*;
import jcsp.plugNplay.ints.*;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class SeqCommsTime {

  public static final String TITLE = "CommsTime";
  public static final String DESCR =
  	"Test of communication speed between JCSP processes. Based on OCCAM CommsTime.occ by Peter Welch, " +
  	"University of Kent at Canterbury. Ported into Java by Oyvind Teig. Now using the JCSP library.\n" +
  	"\n" +
  	"A small network of four processes is created which will generate a sequence of numbers, measuring " +
  	"the time taken to generate each 10000. This time is then divided to calculate the time per iteration, " +
  	"the time per communication (one integer over a one-one channel) and the time for a context switch. " +
  	"There are four communications per iteration and two context switches per communication. This test " +
  	"forms a benchmark for the overheads involved.\n" +
  	"\n" +
	"This version uses a SEQuential delta2 component.";

  public static void  main (String argv []) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    int nLoops = 10000;
    System.out.println (nLoops + " loops ...\n");

    One2OneChannelInt a = Channel.one2oneInt ();
    One2OneChannelInt b = Channel.one2oneInt ();
    One2OneChannelInt c = Channel.one2oneInt ();
    One2OneChannelInt d = Channel.one2oneInt ();

    new Parallel (
      new CSProcess[] {
        new PrefixInt (0, c.in (), a.out ()),
        new SeqDelta2Int (a.in (), d.out (), b.out ()),
        new SuccessorInt (b.in (), c.out ()),
        new Consume (nLoops, d.in ())
      }
    ).run ();

  }

}
