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

package jcspDemos.balls.balls2;


import jcsp.lang.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
public class BallsControl2 implements CSProcess {

  private final AltingChannelInputInt fromBalls;
  private final ChannelOutput[] toBalls;
  private final Barrier barrier;
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;
  private final DisplayList displayList;
  private final Ball2.Info[] oldInfo;
  private final Ball2.Info[] newInfo;
  private final int speed;

  public BallsControl2 (final AltingChannelInputInt fromBalls,
                        final ChannelOutput[] toBalls,
                        final Barrier barrier,
                        final ChannelOutput toGraphics,
                        final ChannelInput fromGraphics,
                        final DisplayList displayList,
                        final Ball2.Info[] oldInfo,
                        final Ball2.Info[] newInfo,
                        final int speed) {
    this.fromBalls = fromBalls;
    this.toBalls = toBalls;
    this.barrier = barrier;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
    this.displayList = displayList;
    this.oldInfo = oldInfo;
    this.newInfo = newInfo;
    this.speed = speed;
  }

  private final static class Graphic implements GraphicsCommand.Graphic {
    public Dimension graphicsDim;
    public Ball2.Info[] info;
    public boolean someAlive = true;
    public void doGraphic (Graphics g, Component c) {
      g.clearRect (0, 0, graphicsDim.width, graphicsDim.height);
      someAlive = false;
      for (int i = 0; i < info.length; i++) {
        final Ball2.Info myInfo = info[i];
        if (myInfo.alive) {
          someAlive = true;
          g.setColor (myInfo.colour);
          g.fillOval (myInfo.x, myInfo.y, myInfo.width, myInfo.height);
        }
      }
    }
  }

  public void run() {

    toGraphics.write (GraphicsProtocol.GET_DIMENSION);
    final Dimension graphicsDim = (Dimension) fromGraphics.read ();
    System.out.println ("BallsControl2: graphics dimension = " + graphicsDim);

    final CSTimer tim = new CSTimer ();
    final long seed = tim.read ();

    barrier.enroll ();                    // we do this before any of the balls

    for (int i = 0; i < toBalls.length; i++) {
      toBalls[i].write (graphicsDim);
      toBalls[i].write (new Long (seed));
    }

    Graphic oldGraphic = new Graphic ();
    oldGraphic.graphicsDim = graphicsDim;
    oldGraphic.info = oldInfo;

    Graphic newGraphic = new Graphic ();
    newGraphic.graphicsDim = graphicsDim;
    newGraphic.info = newInfo;

    GraphicsCommand oldCommand = new GraphicsCommand.General (oldGraphic);
    GraphicsCommand newCommand = new GraphicsCommand.General (newGraphic);

    final long second = 1000;             // JCSP Timer units are milliseconds
    long interval = (int) (((float) second)/((float) speed) + 0.5);
    System.out.println ("BallsControl1 : interval = " + interval);

    long timeout;                         // timeouts will drift ... but never mind ...
    // long timeout = tim.read ();        // timeouts won't drift ... not wanted here ...

    boolean evenCycle = true;

    while (true) {

      timeout = tim.read () + interval;   // timeouts will drift ... but never mind ...
      // timeout += interval;             // timeouts won't drift ... not wanted here ...

      if (evenCycle) {
        while (fromBalls.pending ()) {
          int ball = fromBalls.read ();
          newGraphic.someAlive = true;
          oldGraphic.someAlive = true;
          toBalls[ball].write (null);
        }
        evenCycle = false;
      } else {
        evenCycle = true;
      }

      barrier.sync ();                    // wait for all participating balls
                                          // to update their newInfo slots
      if (newGraphic.someAlive) {
        displayList.set (newCommand);
      }

      final GraphicsCommand tmp = oldCommand;
      oldCommand = newCommand;
      newCommand = tmp;

      tim.after (timeout);

    }

  }

}
