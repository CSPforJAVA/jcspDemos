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
import jcsp.net.*;
import java.util.*;


/**
 * @author Quickstone Technologies Limited
 */

public class ConnectionAuthenticator implements CSProcess {
  private ChannelInput auth;
  private ChannelOutput out;
  private ChannelOutput back;
  private NetAltingChannelInput messageChan;
  private ArrayList users = new ArrayList();
 
  public ConnectionAuthenticator(NetAltingChannelInput auth, ChannelOutput out, NetAltingChannelInput messageChan) {
    this.auth = auth;
    this.out = out;
    this.messageChan = messageChan;

  }

  public void run() {
    while (true) {
      Object o = auth.read();
      System.out.println("connectAuth got "+ o);
      if (o instanceof ConnectionBundle) {
        String newUser = ((ConnectionBundle)o).getUser();
        if (((ConnectionBundle)o).connect()) { //connecting
          if (users.contains(newUser)) {
            ((ConnectionBundle)o).getReturnChan().write(Boolean.FALSE);
            //auth.close(Boolean.FALSE);
          }
          else {
            users.add(newUser);
            System.out.println("added user " + newUser);
            out.write(o);
            //auth.close(Boolean.TRUE);
						((ConnectionBundle)o).getReturnChan().write(messageChan.getChannelLocation());
          }
        }
        else { //disconnecting
          out.write(o);
        }        
      }
      else if (o instanceof ChannelOutput) {
        System.out.println("outputting to dynamic delta to disconnect");
        out.write(o);
      }
      else {
        System.out.println("uh-oh - not a string or ChannelOutput received by ConnectionAuthenticator");
      }
    }
  }
}
