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



import jcsp.lang.*;
import java.util.*;

/**
 * @author Quickstone Technologies Limited
 */
public class ReadyReportingIdentity implements CSProcess {
  private ChannelInput dataIn;
  private ChannelOutput dataOut;
  private ChannelOutputInt readyOut;

  public ReadyReportingIdentity(ChannelInput dataIn, ChannelOutput dataOut, ChannelOutputInt readyOut) {
    this.dataIn = dataIn;
    this.dataOut = dataOut;
    this.readyOut = readyOut;

  }
  public void run() {
    while (true) {
      //System.out.println("id: in true loop");
      readyOut.write(0);
      //System.out.println("id: sent ready message");
      ArrayList data = (ArrayList)dataIn.read();
      //System.out.println("id: received data " + data);
      if (data.size() != 0) {
        //System.out.println("id: resending data " + data.size());
        dataOut.write(data);
      }
      //System.out.println("id: written data");
    }
  }
}
