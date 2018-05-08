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


import jcsp.lang.CSProcess;
import jcsp.lang.Parallel;
import jcsp.lang.PriParallel;
import jcsp.lang.SharedChannelOutput;
import jcsp.net2.*;
import jcsp.net2.tcpip.TCPIPNodeAddress;
import jcsp.userIO.Ask;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 * @author Jon Kerridge - net2 version
 */
public class MandelWorker implements CSProcess {

    private static final int NUM_CORES = 4;

    public static final String TITLE = "Mandelbrot Set (distributed)";

    private final SharedChannelOutput toFarmer;
    private final NetChannelInput fromFarmer;
    private final NetLocation id;
    private final SharedChannelOutput toHarvester;

    private MandelWorker(final SharedChannelOutput toFarmer,
                         final NetChannelInput fromFarmer,
                         final NetChannelLocation id,
                         final SharedChannelOutput toHarvester) {
        this.toFarmer = toFarmer;
        this.fromFarmer = fromFarmer;
        this.id = id;
        this.toHarvester = toHarvester;
    }

    public void run() {

        final int radius = 2;

        final MandelPoint mandel = new MandelPoint(0, radius);

        ResultPacket result = new ResultPacket();

        int count = 0;

        //System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());
        PriParallel.setPriority(Thread.MIN_PRIORITY);
        //System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());

        toFarmer.write(id); // request for work
        WorkPacket work = (WorkPacket) fromFarmer.read();

        final int nPoints = work.X.length;
        result.points = new byte[nPoints];

        while (true) {
            if ((count % 100) == 0) System.out.println(Thread.currentThread().getName() + " - Working ... " + count);
            count++;
            mandel.setMaxIterations(work.maxIterations);
            for (int i = 0; i < nPoints; i++) {
                final int iterations = mandel.compute(work.X[i], work.y);
                if ((iterations == work.maxIterations) || (iterations == 0)) {
                    result.points[i] = 0;
                } else {
                    final byte biterations = (byte) iterations;
                    if (biterations == 0) {
                        result.points[i] = 1;  // OK for smooth colouring (rough ==> 127 ???)
                    } else {
                        result.points[i] = biterations;
                    }
                }
            }
            result.j = work.j;
            toHarvester.write(result);
            toFarmer.write(id);
            work = (WorkPacket) fromFarmer.read();
        }

    }

    public static void main(String[] args) throws Exception {

        // Start up
        // create Worker node instance
        String mainIPAddress;
        TCPIPNodeAddress workerNodeAddress = null;

        if ((args.length == 0) || (args[0] == "-internal")) {
            mainIPAddress = "127.0.0.1";
            String workerIPAddress = Ask.string("Worker IP Address (127.0.0.N  s.t. 1 < N < 255)? ");
            workerNodeAddress = new TCPIPNodeAddress(workerIPAddress, 3000);
        } else {
            // operating over a real network
            mainIPAddress = Ask.string("Main IP Address? ");
            // create local network node address at most global IP address
            workerNodeAddress = new TCPIPNodeAddress(3000);
        }
        // create main process node address
        TCPIPNodeAddress mainNodeAddress = new TCPIPNodeAddress(mainIPAddress, 3000);
        // create the local worker node
        Node.getInstance().init(workerNodeAddress);

        // Connect to the farmer and harvester
//      NetChannelInput fromFarmer = NetChannelEnd.createNet2One ();
        NetChannelInput fromFarmer = NetChannel.net2one();
        NetChannelLocation fromFarmerLocation = (NetChannelLocation) fromFarmer.getLocation();
        System.out.println("Connecting to farmer at vcn 100");
        NetSharedChannelOutput toFarmer = NetChannel.any2net(mainNodeAddress, 100);
        System.out.println("Connecting to harvester at vcn 101");
        NetSharedChannelOutput toHarvester = NetChannel.any2net(mainNodeAddress, 101);
        System.out.println("Ready");

        // Create some workers
        CSProcess workers[] = new CSProcess[NUM_CORES + 1];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new MandelWorker(toFarmer, fromFarmer, fromFarmerLocation, toHarvester);
        }
        new Parallel(workers).run();

    }

}
