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
import java.util.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
public class Ball2 implements CSProcess {

  public final static class Info {
    public Color colour;
    public int x, y, width, height;
    public boolean alive;
  }

  private final int id;
  private final int life;
  private final Barrier dead;
  private final ChannelOutputInt toControl;
  private final ChannelInput fromControl;
  private final Barrier barrier;
  private Info oldInfo, newInfo;

  private Random random;

  public Ball2 (int id, int speed, int life, Barrier dead,
                ChannelOutputInt toControl, ChannelInput fromControl,
                Barrier barrier, Info oldInfo, Info newInfo) {
    this.id = id;
    this.life = life*speed;
    this.dead = dead;
    this.toControl = toControl;
    this.fromControl = fromControl;
    this.barrier = barrier;
    this.oldInfo = oldInfo;
    this.newInfo = newInfo;
  }

  private final int range (int n) {
    int i = random.nextInt ();
    if (i < 0) {
      if (i == Integer.MIN_VALUE) {      // guard against minint !
        i = 42;
      } else {
        i = -i;
      }
    }
    return i % n;
  }

  private final int delta (int n) {
    int i = range ((2*n) + 1) - n;
    while (i == 0) i = range ((2*n) + 1) - n;
    return i;
  }

  public void run () {

    // System.out.println ("Ball " + id + " running ...");

    final Thread me = Thread.currentThread ();
    // System.out.println ("Ball " + id + ": priority = " + me.getPriority ());
    me.setPriority (Thread.MAX_PRIORITY);
    // System.out.println ("Ball " + id + ": priority = " + me.getPriority ());

    final Dimension graphicsDim = (Dimension) fromControl.read ();
    final long seed = id + ((Long) fromControl.read ()).longValue ();
    this.random = new Random (seed);
    // System.out.println ("Ball " + id + ": " + graphicsDim);
    // System.out.println ("Ball " + id + ": seed = " + seed);

    final CSTimer tim = new CSTimer ();
    final long second = 1000;  // JCSP Timer units are milliseconds
    final long blackout = 5*second;

    while (true) {

      // initialise data for new ball ...

      newInfo.colour = new Color (random.nextInt ());
      newInfo.width = range (30) + 10;
      newInfo.height = range (30) + 10;
      newInfo.x = range (graphicsDim.width - newInfo.width);
      newInfo.y = range (graphicsDim.height - newInfo.height);

      oldInfo.colour = newInfo.colour;
      oldInfo.width = newInfo.width;
      oldInfo.height = newInfo.height;

      int deltaX = delta (10);
      int deltaY = delta (10);

      // System.out.println ("Ball " + id + ": initialX,Y = " + newGraphic.x + ", " + newGraphic.y);
      // System.out.println ("Ball " + id + ": initialW,H = " + newGraphic.width + ", " + newGraphic.height);
      // System.out.println ("Ball " + id + ": (deltaX, deltaY) = (" + deltaX + ", " + deltaY + ")");

      tim.sleep (id*second);                // bring the balls up one by one
      toControl.write (id);                 // bring our newInfo/oldInfo into sync with control
      newInfo.alive = true;
      oldInfo.alive = true;
      barrier.enroll ();
      fromControl.read ();                  // now we are in sync with control

      // System.out.println ("Ball " + id + ": alive");

      int countdown = life;

      while (countdown > 0) {

        barrier.sync ();                    // let control know newInfo is ready ...

        final Info tmp = oldInfo;
        oldInfo = newInfo;
        newInfo = tmp;

        int x = oldInfo.x + deltaX;
        if ((x < 0) || ((x + oldInfo.width) > graphicsDim.width)) {
          deltaX = -deltaX;
        }
        int y = oldInfo.y + deltaY;
        if ((y < 0) || ((y + oldInfo.height) > graphicsDim.height)) {
          deltaY = -deltaY;
        }
        newInfo.x = x;
        newInfo.y = y;

        countdown--;

      }

      // System.out.println ("Ball " + id + ": dead");

      newInfo.alive = false;
      oldInfo.alive = false;

      barrier.resign ();                    // don't need to sync with control for this

      dead.sync ();                         // wait for all the other balls to die

      tim.sleep (blackout);                 // blackout for 5 seconds ...

    }

  }

}
