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

package jcspDemos.net2.barriers;

import java.util.Random;

import jcsp.lang.ProcessManager;
import jcsp.net2.NetBarrier;
import jcsp.net2.NetBarrierEnd;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

/**
 * This program creates a local NetBarrier with two processes syncing upon it
 * 
 * @author Kevin
 */
public class LocalBarrier
{
    public static void main(String[] args)
    {
        // Initialise the Node
        Node.getInstance().init(new TCPIPNodeAddress());

        // Create a new NetBarrier server end, index 100. 1 locally enrolled process, 1 remote
        NetBarrier server = NetBarrierEnd.numberedNetBarrier(100, 1, 1);

        // Now start up the Server process
        new ProcessManager(new NetBarrierTestProcess(server, 1)).start();

        // Now enroll with the NetBarrier
        NetBarrier client = NetBarrierEnd.netBarrier(Node.getInstance().getNodeID(), 100, 1);

        // Randomly sync with the Barrier
        Random rand = new Random();

        while (true)
        {
            try
            {
                // Wait randomly upto 5 seconds
                Thread.sleep(Math.abs(rand.nextLong() % 5000));
                System.out.println("Syncing client end...");
                client.sync();
                System.out.println("Client end synced...");
            }
            catch (InterruptedException ie)
            {
                // Do nothing. Shouldn't happen
            }
        }
    }
}
