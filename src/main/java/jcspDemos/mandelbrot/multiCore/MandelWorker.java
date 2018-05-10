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

package jcspDemos.mandelbrot.multiCore;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class MandelWorker implements CSProcess {

  private final int id;

  private final int minMaxIterations;
  private final int maxMaxIterations;

  private final ChannelInput fromFarmer;
  private final ChannelOutput toFarmer;

  private final ChannelInput fromHarvester;
  private final ChannelOutput toHarvester;

  public MandelWorker (final int id,
                       final int minMaxIterations,
                       final int maxMaxIterations,
                       final ChannelInput fromFarmer,
                       final ChannelOutput toFarmer,
                       final ChannelInput fromHarvester,
                       final ChannelOutput toHarvester) {
    this.id = id;
    this.minMaxIterations = minMaxIterations;
    this.maxMaxIterations = maxMaxIterations;
    this.fromFarmer = fromFarmer;
    this.toFarmer = toFarmer;
    this.fromHarvester = fromHarvester;
    this.toHarvester = toHarvester;
  }

  public void run () {

    final int radius = 2;

    final MandelPoint mandel = new MandelPoint (0, radius);

    WorkPacket work = new WorkPacket ();
    ResultPacket result = new ResultPacket ();

    int count = 0;

    System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());
    PriParallel.setPriority (Thread.MIN_PRIORITY);
    System.out.println ("Worker " + id + " priority = " + PriParallel.getPriority ());

    toFarmer.write (work);
    work = (WorkPacket) fromFarmer.read ();

    final int nPoints = work.X.length;
    result.points = new byte[nPoints];

    while (true) {
      count++;
      if ((count % 100) == 0) System.out.println ("Worker " + id + " working ... " + count );
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
      toHarvester.write (result);  // these communication-pairs could be in PAR
      fromHarvester.read ();
      toFarmer.write (work);
      work = (WorkPacket) fromFarmer.read ();
      Thread.yield ();
    }

  }

}
