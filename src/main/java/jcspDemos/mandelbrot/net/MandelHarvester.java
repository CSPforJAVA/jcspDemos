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
import jcsp.awt.*;

import java.awt.image.*;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 */
class MandelHarvester implements CSProcess {

  private final ChannelOutput toControl;

  private final AltingChannelInput fromFarmer;
  private final ChannelOutput toFarmer;

  private final AltingChannelInput fromWorkers;

  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;

  public MandelHarvester (final ChannelOutput toControl,
                          final AltingChannelInput fromFarmer,
                          final ChannelOutput toFarmer,
                          final AltingChannelInput fromWorkers,
                          final ChannelOutput toGraphics,
                          final ChannelInput fromGraphics) {
    this.toControl = toControl;
    this.fromFarmer = fromFarmer;
    this.toFarmer = toFarmer;
    this.fromWorkers = fromWorkers;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
  }

  public void run () {

    final Alternative alt = new Alternative (
      new AltingChannelInput[] {
        fromFarmer, fromWorkers
      }
    );

    final int CANCEL = 0;
    final int RESULT = 1;

    final GraphicsCommand[] commands = {null, null};

    // final GraphicsProtocol gpChange = new GraphicsProtocol.Change (1, commands);

    final int width = ((Integer) fromFarmer.read ()).intValue ();
    final int height = ((Integer) fromFarmer.read ()).intValue ();
    final MemoryImageSource[] mis = (MemoryImageSource[]) fromFarmer.read ();
    final Display display = (Display) fromFarmer.read ();

    final GraphicsCommand[] drawLine = new GraphicsCommand [height];
    for (int j = 0; j < height; j++) {
      drawLine[j] = new GraphicsCommand.DrawLine (0, j, width - 1, j);
    }
    
    final CSTimer tim = new CSTimer ();
    long time1 = tim.read ();

    while (true) {

      final FarmPacket packet = (FarmPacket) fromFarmer.read ();
      commands[0] = packet.colouring;
      packet.ok = true;
      int lastLineArrived = -1;

      loop: for (int count = 0; count < height; count++) {
        switch (alt.priSelect ()) {
          case CANCEL:
            int generated = ((Integer) fromFarmer.read ()).intValue ();
            System.out.println ("MandelHarvester.CANCEL: " + count + " " + generated);
            for (int i = count; i < generated; i++) {
              fromWorkers.read ();
            }
            System.out.println ("MandelHarvester.CANCEL: " + count + " " + generated);
            toFarmer.write (Boolean.TRUE);  // all work packets cleared
            packet.ok = false;
          break loop;
          case RESULT:
            final ResultPacket result = (ResultPacket) fromWorkers.read ();
            switch (packet.scrolling) {
              case FarmPacket.SCROLL_SILENT:
                System.arraycopy (result.points, 0, packet.pixels, result.j*width, width);
                if ((count % FarmPacket.STRIDE_SILENT) == 0) {
                  commands[1] = drawLine[count];
                  display.change (commands, 1);
                }
              break;
              case FarmPacket.SCROLL_UP:
                if (result.j > lastLineArrived) {
                  // need to scroll at least 1 line
                  final int lines = result.j - lastLineArrived;
                  System.arraycopy (packet.pixels, width * lines, packet.pixels, 0, width * (height - lines));
                  lastLineArrived = result.j;
                }
                // data blit
                System.arraycopy (result.points, 0, packet.pixels, width * (height - 1 - (lastLineArrived - result.j)), width);
                long t = tim.read ();
                if (t > time1 + 40) {
	                mis[packet.colourModel].newPixels ();
	                time1 = t;
                }
              break;
              case FarmPacket.SCROLL_DOWN:
                System.arraycopy (result.points, 0, packet.pixels, result.j*width, width);
                mis[packet.colourModel].newPixels (0, result.j, width, 1);
              break;
              case FarmPacket.NO_SCROLL:
                System.arraycopy (result.points, 0, packet.pixels, result.j*width, width);
              break;
            }
          break;
        }
      }

      toControl.write (packet);

    }

  }

}
