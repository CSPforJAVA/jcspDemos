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
public class RegulateTest {

  public static final String TITLE = "Regulated Process Behaviour";
  public static final String DESCR =
  		"Shows how a process with periodic behaviour can use an alt to have its actions regulated externally. " +
  		"The Regulate process creates numbers at regular intervals. Instead of sleeping for those intervals " +
  		"it makes use of a timeout guard within an Alt. Other guards allow the timeout interval to be reset " +
  		"and the number it outputs to be modified.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final One2OneChannel a = Channel.one2one();
    final One2OneChannel b = Channel.one2one();
    final One2OneChannel reset = Channel.one2one();

    new Parallel (
      new CSProcess[] {
        new Variate (a.out(), 5000, 10, 2),
        new Regulate (a.in(), reset.in(), b.out(), 500),
        new Printer (b.in(), "RegulateTest ==> ", "\n"),
        new CSProcess () {
          // this controls the Regulate process, switching its firing
          // rate between a half and one second.  The switches occur
          // every five seconds.
          public void run () {
            final Long halfSecond = new Long (500);
            final Long second = new Long (1000);
            final CSTimer tim = new CSTimer ();
            long timeout = tim.read ();
            while (true) {
              timeout += 5000;
              tim.after (timeout);
              System.out.println ("                    <== now every second");
              reset.out().write (second);
              timeout += 5000;
              tim.after (timeout);
              System.out.println ("                    <== now every half second");
              reset.out().write (halfSecond);
            }
          }
        }
      }
    ).run ();

  }

}
