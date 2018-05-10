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

package jcspDemos.jcspchat.net;



import jcsp.lang.*;

/**
 * @author Quickstone Technologies Limited
 */
public class OutPlex implements CSProcess {
  private AltingChannelInput usernameIn;
  private AltingChannelInput messageIn;
  private ChannelOutput out;
  private String username = "Anon";

  public OutPlex(AltingChannelInput usernameIn, AltingChannelInput messageIn, ChannelOutput out) {
    this.usernameIn = usernameIn;
    this.messageIn = messageIn;
    this.out = out;

  }
  public void run() {
    final AltingChannelInput[] altChans = { usernameIn, messageIn};
    final Alternative alt = new Alternative (altChans);
    String output;

    while (true) {
      switch (alt.select()) {
        case 0:
          username = (String)usernameIn.read();

        break;
        case 1:
          output = username + ": " + (String)messageIn.read() + "\n";
//          System.out.println("OutPlex: sending message = " + output);
          out.write(output);
        break;
      }
    }
  }
}
