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

package jcspDemos.philosophers.callChannel;



import jcsp.lang.*;
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class PhilCall {

  public static final String TITLE = "Dining Philosophers [call channels]";
  public static final String DESCR =
  	"Shows the 'dining philosophers' deadlock problem and solution implemented using JCSP call channels. " +
  	"Each of the philosophers attempts to claim the shared resources (forks) via calls made on the fork " +
  	"call channels. Deadlock is prevented by the security guard which each philosopher must communicate " +
  	"with (via call channel) to gain permission to claim a fork.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.addPrompt ("philsophers", 1, 100, 10);
  	Ask.show ();
  	final int nPhilosophers = Ask.readInt ("philosophers");
  	Ask.blank ();

    final int seconds = 1000;
    final int clockPeriod = 1*seconds;

    PhilChannel philChannel = new PhilChannel ();
    ForkChannel forkChannel = new ForkChannel ();
    One2OneChannelInt securityChannel = Channel.one2oneInt ();
    One2OneChannelInt clockChannel = Channel.one2oneInt ();

    new Parallel (
      new CSProcess[] {
        new DiningPhilosophersCollege (
          nPhilosophers, clockPeriod,
          philChannel, forkChannel,
          securityChannel.out (), clockChannel.out ()
        ),
        new TextDisplay (
          nPhilosophers,
          philChannel, forkChannel,
          securityChannel.in (), clockChannel.in ()
        )
      }
    ).run ();
  }

}
