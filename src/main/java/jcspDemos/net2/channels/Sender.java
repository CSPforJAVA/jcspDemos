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
import jcsp.net2.NetChannelOutput;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

/**
 * This program is the sending end (i.e. client) of a simple networked Producer-Consumer program. This program shows how
 * to create a numbered channel connection without going through the CNS
 * 
 * @author Kevin Chalmers
 */
public class Sender
{
    public static void main(String[] args)
    {
        // First we need to initialise the Node. Start listening process on port 5000 of this machine
        Node.getInstance().init(new TCPIPNodeAddress(5000));

        // We now need to create a channel to the receiver process. It is listening on port 4000 on this machine, and is
        // using channel 100
        NetChannelOutput out = NetChannel.one2net(new TCPIPNodeAddress(4000), 100);

        // Now send Integers for ever
        int i = 0;

        while (true)
        {
            out.write(new Integer(i));
            i++;
        }
    }
}
