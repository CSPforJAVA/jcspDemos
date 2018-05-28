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

package jcspDemos.mandelbrot.net;



import jcsp.lang.*;
import jcsp.net.*;
import jcsp.net.tcpip.*;
import jcsp.net.cns.*;
import jcsp.userIO.*;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 */
public class MandelWorkerNet implements CSProcess {

  private static final int NUM_THREADS = 10;

  public static final String TITLE = "Mandelbrot Set (net version)";
  public static final String DESCR =
  	"Mandelbrot worker process. Please give the IP address of the machine running the main program interface.";

  private final SharedChannelOutput toFarmer;
  private final NetChannelInput fromFarmer;
  private final NetChannelLocation id;
  private final SharedChannelOutput toHarvester;

  private MandelWorkerNet(final SharedChannelOutput toFarmer,
                          final NetChannelInput fromFarmer,
                          final NetChannelLocation id,
                          final SharedChannelOutput toHarvester) {
    this.toFarmer = toFarmer;
    this.fromFarmer = fromFarmer;
    this.id = id;
    this.toHarvester = toHarvester;
  }

  public void run () {

    final int radius = 2;

    final MandelPoint mandel = new MandelPoint (0, radius);

    ResultPacket result = new ResultPacket ();

    int count = 0;

    //System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());
    PriParallel.setPriority (Thread.MIN_PRIORITY);
    //System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());

    toFarmer.write (id); // request for work
    WorkPacket work = (WorkPacket) fromFarmer.read ();

    final int nPoints = work.X.length;
    result.points = new byte[nPoints];

    while (true) {
      if ((count % 100) == 0) System.out.println (Thread.currentThread ().getName () + " - Working ... " + count );
      count++;
      mandel.setMaxIterations (work.maxIterations);
      for (int i = 0; i < nPoints; i++) {
        final int iterations = mandel.compute (work.X[i], work.y);
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
      toHarvester.write (result);
      toFarmer.write (id);
      work = (WorkPacket) fromFarmer.read ();
    }

  }

  public static void main (String[] args) throws Exception {

  	// Start up
      if (args.length == 0) {
        Ask.app (TITLE, DESCR);
        Ask.addPrompt ("CNS Address");
        Ask.show ();
        Node.getInstance ().init (new TCPIPNodeFactory (Ask.readStr ("CNS Address")));
        Ask.blank ();
  	} else {
        Node.getInstance ().init (new TCPIPNodeFactory (args[0]));
  	}

  	// Connect to the farmer and harvester
  	NetChannelInput fromFarmer = NetChannelEnd.createNet2One ();
  	NetChannelLocation id = fromFarmer.getChannelLocation ();
  	System.out.println ("Connecting to farmer");
  	NetSharedChannelOutput toFarmer = CNS.createAny2Net ("org.jcsp.demos.mandelbrot.net.Farmer");
  	System.out.println ("Connecting to harvester");
  	NetSharedChannelOutput toHarvester = CNS.createAny2Net ("org.jcsp.demos.mandelbrot.net.Harvester");
  	System.out.println ("Ready");

  	// Create some workers
  	CSProcess workers[] = new CSProcess[NUM_THREADS];
  	for (int i = 0; i < workers.length; i++) {
  		workers[i] = new MandelWorkerNet(toFarmer, fromFarmer, id, toHarvester);
  	}
  	new Parallel (workers).run ();

  }

}
