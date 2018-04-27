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

package jcspDemos.net2.async;

import jcsp.net2.JCSPNetworkException;
import jcsp.net2.NetAltingChannelInput;
import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelOutput;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

/**
 * This program highlights the usage of async messages, as well as locally connected channels
 * 
 * @author Kevin Chalmers
 */
public class AsynchronousChannels
{
    public static void main(String[] args)
    {
        // Initialise the Node. We are listening on port 4000
        Node.getInstance().init(new TCPIPNodeAddress(4000));

        // Now create a channel numbered 100
        NetAltingChannelInput in = NetChannel.numberedNet2One(100);

        // Now connect an output end to this input end
        NetChannelOutput out = NetChannel.one2net(Node.getInstance().getNodeID(), 100);

        // Now send 100 integers asynchronously to the local input end
        for (int i = 0; i < 100; i++)
            out.asyncWrite(new Integer(i));

        // Now read them all back
        while (in.pending())
        {
            System.out.println(in.read());
        }

        // All messages read. Destroy the input end.
        in.destroy();

        // Now attempt an output. Should fail
        try
        {
            out.write(new Object());
        }
        catch (JCSPNetworkException jne)
        {
            System.out.println("The local input end was destroyed");
        }
    }
}
