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
import java.io.*;

/**
 * @author Quickstone Technologies Limited
 */

public class ConnectionBundle implements Serializable  {
  private ChannelOutput returnChan;
  private String user;
  private boolean connect;

  public ConnectionBundle(String user, ChannelOutput returnChan, boolean connect) {
    this.user = user;
    this.returnChan = returnChan;
    this.connect = connect;
  }
  public ConnectionBundle(String user, boolean connect) {
    this.user = user;
    this.connect = connect;
    this.returnChan = null;
  }
  public ChannelOutput getReturnChan() {
    return returnChan;
  }
  public String getUser() {
    return user;
  }
  public boolean connect() {
    return connect;
  }
}
