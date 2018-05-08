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

package jcspDemos.jcspchat.net2;


import jcsp.lang.ChannelOutput;
import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelLocation;
import jcsp.net2.NetChannelOutput;

import java.io.Serializable;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Merridge for net2 version
 */
  // the net2 version showing that net channel addresses have to be
  // transferred as NetChannelLocations and then initiated at the new node
public class ConnectionBundle implements Serializable  {
  private NetChannelLocation returnChanLoc;
  private String user;
  private boolean connect;

  public ConnectionBundle(String user, NetChannelLocation returnChanLoc, boolean connect) {
    this.user = user;
    this.returnChanLoc = returnChanLoc;
    this.connect = connect;
  }
  public ConnectionBundle(String user, boolean connect) {
    this.user = user;
    this.connect = connect;
    this.returnChanLoc = null;
  }
  // assumed to be running on a node other than the one upon which the net channel was created
  public ChannelOutput getReturnChan() {
    NetChannelOutput returnChan = NetChannel.one2net(returnChanLoc);
    System.out.println("created return channel to client at " + returnChanLoc.toString());
    return returnChan;
  }
  public String getUser() {
    return user;
  }
  public boolean connect() {
    return connect;
  }
}
