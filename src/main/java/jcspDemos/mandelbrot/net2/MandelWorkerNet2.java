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
public class MandelWorkerNet2 implements CSProcess {

    private static final int NUM_THREADS = 10;

    public static final String TITLE = "Mandelbrot Set (net2 version)";
    public static final String DESCR =
      "Mandelbrot worker process. Please give the IP address of the machine running the main program interface. " +
      "If the worker is running on the same machine as the main process and other workers" +
        " then the port number must be different" +
      "from any other ports used.  The main process uses port 1000 by default." +
        "If the worker is running on a different machine any port number can be used." +
      "When using a loopback IP address then the port number is irrelevant, it defaults to 2000" +
      "Each worker IP address and the main process must use different IP addresses such that" +
      "a worker must have and IP address of the form 127.0.0.N  s.t. 1 < N < 255";

    private final SharedChannelOutput toFarmer;
    private final NetChannelInput fromFarmer;
    private final NetLocation id;
    private final SharedChannelOutput toHarvester;

    private MandelWorkerNet2(final SharedChannelOutput toFarmer,
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
      String mainIPAddress = "127.0.0.1";
      String workerIPAddress = "127.0.0.2";
        int workerPortNumber = 2000;
        if (args.length == 0) {
          Ask.app(TITLE, DESCR);
          Ask.addPrompt("Main IP address");
          Ask.addPrompt("Worker Port Number");
          Ask.show();
          mainIPAddress = Ask.readStr("Main IP address");
          workerPortNumber = Ask.readInt("Worker Port Number");
          Ask.blank();
        }
        else { // assume the single arg is -internal)
          if (args[0] != "-internal")  {
            System.out.println("Not found expected arg[0] value: -internal ");
            System.exit(0);
          } else {
            Ask.app(TITLE, DESCR);
            Ask.addPrompt("Worker IP address");
            Ask.show();
            workerIPAddress = Ask.readStr("Worker IP address");
            Ask.blank();
          }
        }

        // create Worker node instance
        TCPIPNodeAddress workerNodeAddress = null;

        if (mainIPAddress == "127.0.0.1") {
            workerNodeAddress = new TCPIPNodeAddress(workerIPAddress, 2000);
        } else {
            // operating over a real network
            workerNodeAddress = new TCPIPNodeAddress(workerPortNumber);
        }
        // create main process node address
        TCPIPNodeAddress mainNodeAddress = new TCPIPNodeAddress(mainIPAddress, 1000);
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
        CSProcess workers[] = new CSProcess[NUM_THREADS];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new MandelWorkerNet2(toFarmer, fromFarmer, fromFarmerLocation, toHarvester);
        }
        new Parallel(workers).run();

    }

}
