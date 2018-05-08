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


import jcsp.lang.CSProcess;
import jcsp.lang.ChannelInput;
import jcsp.lang.ChannelOutput;

import java.util.ArrayList;

/**
 * @author Quickstone Technologies Limited
 */
public class SorterProcess implements CSProcess {
  private ChannelInput in;
  private ChannelOutput whiteboardOut;
  private ChannelOutput chatOut;
  public SorterProcess(ChannelInput in, ChannelOutput whiteboardOut, ChannelOutput chatOut) {
    this.in = in;
    this.whiteboardOut = whiteboardOut;
    this.chatOut = chatOut;
  }
  public void run () {
    while (true) {
      Object o = in.read();
      //System.out.println("Sorter: received data");
      if (o instanceof ArrayList && ((ArrayList)o).size() != 0) {
        ArrayList al = (ArrayList)o;
        for (int x=0; x < al.size(); x++ ) {
          Object o2 = al.get(x);
          if (o2 instanceof WhiteboardDataBundle) {
            whiteboardOut.write(o2);
          }
          else {
            chatOut.write(o2);
          }
        }
      }
      else {
        System.out.println("uh-oh - somethings awry... :( ");
      }
    }
  }
}
