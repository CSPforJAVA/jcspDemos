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

package jcspDemos.jcspchat;


import java.io.*;

/**
 * @author Quickstone Technologies Limited
 */
public class MessageObject implements Serializable {
  public static final int USERMESSAGE = 0;
  public static final int CONNECT = 1;
  public static final int DISCONNECT = 2;
  public String user;
  public String message;
  public int sysCommand;

  public MessageObject(String user, String message) {
    this.user = user;
    this.message = message;
    this.sysCommand = USERMESSAGE;
  }
  public MessageObject(String user, String message, int sysCommand) {
    this.user = user;
    this.message = message;
    this.sysCommand = sysCommand;


  }
}
