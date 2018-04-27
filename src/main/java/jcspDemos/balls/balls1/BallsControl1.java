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

package jcspDemos.balls.balls1;


import jcsp.lang.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
public class BallsControl1 implements CSProcess {

  private final ChannelOutput[] toBalls;
  private final Barrier barrier;
  private final DisplayList displayList;
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;
  private final int speed;

  public BallsControl1 (final ChannelOutput[] toBalls,
                        final Barrier barrier,
                        final DisplayList displayList,
                        final ChannelOutput toGraphics,
                        final ChannelInput fromGraphics,
                        final int speed) {
    this.toBalls = toBalls;
    this.barrier = barrier;
    this.displayList = displayList;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
    this.speed = speed;
  }

  public void run() {

    toGraphics.write (GraphicsProtocol.GET_DIMENSION);
    final Dimension graphicsDim = (Dimension) fromGraphics.read ();
    System.out.println ("BallsControl: graphics dimension = " + graphicsDim);

    final GraphicsCommand baseCommand =
      new GraphicsCommand.ClearRect (0, 0, graphicsDim.width, graphicsDim.height);

    displayList.set (baseCommand);

    final CSTimer tim = new CSTimer ();
    final long seed = tim.read ();

    barrier.enroll ();                    // we do this before any of the balls

    for (int i = 0; i < toBalls.length; i++) {
      toBalls[i].write (graphicsDim);
      toBalls[i].write (new Long (seed));
    }

    final long second = 1000;              // JCSP Timer units are milliseconds
    long interval = (int) (((float) second)/((float) speed) + 0.5);
    System.out.println ("BallsControl1 : interval = " + interval);

    long timeout;                         // timeouts will drift ... but never mind ...
    // long timeout = tim.read ();        // timeouts won't drift ... not wanted here ...

    while (true) {

      timeout = tim.read () + interval;   // timeouts will drift ... but never mind ...
      // timeout += interval;             // timeouts won't drift ... not wanted here ...

      barrier.sync ();

      tim.after (timeout);

    }

  }

}
