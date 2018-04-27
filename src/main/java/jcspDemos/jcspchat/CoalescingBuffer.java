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

public class CoalescingBuffer implements CSProcess {
  private ChannelOutput dataOut;
  private Guard[] inputArray;
  private ArrayList daList = new ArrayList();
  private int i;

  public CoalescingBuffer(AltingChannelInput dataIn, ChannelOutput dataOut,AltingChannelInputInt readyIn) {
    inputArray = new Guard[] {readyIn, dataIn};
    this.dataOut = dataOut;
  }

  public void run() {
    final Alternative alt = new Alternative(inputArray);
    boolean ready = false;
    while (true) {
      switch(alt.fairSelect()) {
        case 0:
          //System.out.println("buffer: chose report chan");
          i = ((AltingChannelInputInt)inputArray[0]).read();
          if (daList.size() > 0) {
            //System.out.println("buffer: stuff to send immediately - sending");
            daList.trimToSize();
            //System.out.println("buffer: list trimmed to " + daList.size());
            dataOut.write(daList);
            //System.out.println("buffer: sent data");
            daList = new ArrayList();
          }
          else {
            ready = true;
          }

          break;
        case 1:
          //System.out.println("buffer: chose message chan");
          Object o = ((AltingChannelInput)inputArray[1]).read();
          //System.out.println("buffer: received data - adding to list");
          daList.add(o);
          if (ready) {
            //System.out.println("buffer: ready - sending");
            daList.trimToSize();
            dataOut.write(daList);
            daList = new ArrayList();
            ready = false;
          }
          break;
      }
    }
  }
}
