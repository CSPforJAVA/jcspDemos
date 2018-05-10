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
class MandelFarmer implements CSProcess {

  private final ChannelInput fromControl;

  private final ChannelInput fromHarvester;
  private final ChannelOutput toHarvester;

  private final AltingChannelInput fromCancel;
  private final ChannelOutput toCancel;

  private final AltingChannelInput fromWorkers;
  private final ChannelOutput toWorkers;

  public MandelFarmer (final ChannelInput fromControl,
                       final ChannelInput fromHarvester,
                       final ChannelOutput toHarvester,
                       final AltingChannelInput fromCancel,
                       final ChannelOutput toCancel,
                       final AltingChannelInput fromWorkers,
                       final ChannelOutput toWorkers) {
    this.fromControl = fromControl;
    this.fromHarvester = fromHarvester;
    this.toHarvester = toHarvester;
    this.fromCancel = fromCancel;
    this.toCancel = toCancel;
    this.fromWorkers = fromWorkers;
    this.toWorkers = toWorkers;
  }

  public void run () {

    final Alternative alt = new Alternative (
      new AltingChannelInput[] {
        fromCancel, fromWorkers
      }
    );

    final int CANCEL = 0;
    final int WORK = 1;

    Object object = fromControl.read ();
    final int width = ((Integer) object).intValue ();
    final double[] X = new double[width];
    toHarvester.write (object);

    object = fromControl.read ();
    final int height = ((Integer) object).intValue ();
    toHarvester.write (object);

    object = fromControl.read ();
    // mis
    toHarvester.write (object);

    object = fromControl.read ();
    // display
    toHarvester.write (object);

    while (true) {

      final FarmPacket packet = (FarmPacket) fromControl.read ();
      toHarvester.write (packet);

      System.out.println ("\nMandelFarmer: Left = " + packet.left);
      System.out.println ("MandelFarmer: Top = " + packet.top);
      System.out.println ("MandelFarmer: Size = " + packet.size);
      System.out.println ("MandelFarmer: Maximum iterations = " + packet.maxIterations);

      final double shrink = packet.size/((double) (width - 1));
      for (int i = 0; i < width; i++) {
        X[i] = packet.left + (((double) i) * shrink);
      }

      while (fromCancel.pending ()) fromCancel.read ();
      toCancel.write (Boolean.TRUE);

      loop: for (int j = 0; j < height; j++) {
        final double y = packet.top - (((double) j) * shrink);
        switch (alt.priSelect ()) {
          case CANCEL:
            fromCancel.read ();
System.out.println ("MandelFarmer.CANCEL: " + j);
            toCancel.write (Boolean.FALSE);
            toHarvester.write (new Integer (j));  // say how many work packets generated
            fromHarvester.read ();
          break loop;
          case WORK:
            final WorkPacket work = (WorkPacket) fromWorkers.read ();
            work.X = X; work.y = y; work.j = j;
            work.maxIterations = packet.maxIterations;
            toWorkers.write (work);
          break;
        }
      }

      toCancel.write (Boolean.FALSE);

    }

  }

}
