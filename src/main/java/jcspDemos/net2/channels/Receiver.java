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

package jcspDemos.net2.channels;

import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelInput;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

/**
 * This is the receiver process of the Producer-Consumer program. Run this program first
 * 
 * @author Kevin Chalmers
 */
public class Receiver
{
    public static void main(String[] args)
    {
        // Initialise the Node. We are listening on port 4000
        Node.getInstance().init(new TCPIPNodeAddress(4000));

        // Now create a channel numbered 100
        NetChannelInput in = NetChannel.numberedNet2One(100);

        // Loop forever, printing our input
        while (true)
        {
            System.out.println(in.read());
        }
    }
}
