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

package jcspDemos.mandelbrot.net2;


import jcsp.awt.ActiveClosingFrame;
import jcsp.awt.ActiveFrame;
import jcsp.lang.CSProcess;
import jcsp.lang.Parallel;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 * @author Jon Kerridge net2 version
 */
public class MandelbrotMainNet2 {

    public static final String TITLE = "Mandelbrot Set (net2)";
    public static final String DESCR =
            "Demonstrates a distributed farmer/worker/harvester parallelisation. \nThe farmer and harvester processes " +
                    "will run on this Node. \nWorkers can run on this machine or elsewhere to generate the actual image." +
                    "\nA real network can be used or by using the loop-back IP address range 127.0.0.N where N > 1." +
                    "\nWorker nodes can be added dynamically but cannot be stopped mid-process; that is left as a challenge!" +
                    "\nThe use of an loop-back TCP/IP network is indicated by running the main process with the argument -internal.\n";

    private static final int DEFAULT_WIDTH = 640, DEFAULT_HEIGHT = 480;

    public static void main(String[] args) throws Exception {
        System.out.println(DESCR);
        // Get arguments
        boolean realNetwork = true; // assume running on real network
        int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-width")) {
                width = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-height")) {
                height = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-internal")) {
                realNetwork = false;
            }
        }
        TCPIPNodeAddress mainNodeAddress = null;
        if (realNetwork) {
            // get most global IP address available
            mainNodeAddress = new TCPIPNodeAddress(1000);
        } else {
            String mainIPAddress = "127.0.0.1";
            mainNodeAddress = new TCPIPNodeAddress(mainIPAddress, 1000);
        }
        // now create the main node either as 127.0.0.1 (internal)
        // or as a real node at whetever IP address the program is running
        Node.getInstance().init(mainNodeAddress);

        final ActiveClosingFrame activeClosingFrame = new ActiveClosingFrame("Distributed Mandelbrot - net2 version");
        final ActiveFrame activeFrame = activeClosingFrame.getActiveFrame();
        activeFrame.setSize(width, height);

        final MandelNetwork mandelbrot = new MandelNetwork(activeFrame);

        activeFrame.pack();
        activeFrame.setVisible(true);
        activeFrame.toFront();

        System.out.println("Main Node IP address = " + mainNodeAddress.getIpAddress() + " now running\n");

        new Parallel(
                new CSProcess[]{
                        activeClosingFrame,
                        mandelbrot
                }
        ).run();

    }

}
