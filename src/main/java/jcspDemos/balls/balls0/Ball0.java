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

package jcspDemos.balls.balls0;


import jcsp.lang.*;
import jcsp.awt.*;
import java.util.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
class Ball0 implements CSProcess {

  private final int id;
  private final int speed;
  private final int life;
  private final Barrier dead;
  private final ChannelInput fromControl;
  private final DisplayList displayList;

  private Random random;

  public Ball0 (int id, int speed, int life, Barrier dead,
                ChannelInput fromControl, DisplayList displayList) {
    this.id = id;
    this.speed = speed;
    this.life = life*speed;
    this.dead = dead;
    this.fromControl = fromControl;
    this.displayList = displayList;
  }

  private final static class Graphic implements GraphicsCommand.Graphic {
    public Color colour;
    public int x, y, width, height;
    public void doGraphic (Graphics g, Component c) {
      g.setColor (colour);
      g.fillOval (x, y, width, height);
    }
  }

  /**
   * returns a random integer in the range [0, n - 1]
   */
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

  /**
   * returns a random integer in the range [-n, n] excluding 0
   */
  private final int delta (int n) {
    int i = range (2*n) - n;
    if (i == 0) i = n;
    return i;
  }

  public void run () {

    // System.out.println ("Ball " + id + " running ...");

    final Dimension graphicsDim = (Dimension) fromControl.read ();
    final long seed = id + ((Long) fromControl.read ()).longValue ();
    this.random = new Random (seed);
    // System.out.println ("Ball " + id + ": " + graphicsDim);
    // System.out.println ("Ball " + id + ": seed = " + seed);

    final int displaySlot = displayList.extend (GraphicsCommand.NULL);
    // System.out.println ("Ball " + id + ": displaySlot = " + displaySlot);

    Graphic oldGraphic = new Graphic ();
    Graphic newGraphic = new Graphic ();

    GraphicsCommand oldCommand = new GraphicsCommand.General (oldGraphic);
    GraphicsCommand newCommand = new GraphicsCommand.General (newGraphic);

    final CSTimer tim = new CSTimer ();

    final Thread me = Thread.currentThread ();
    // System.out.println ("Ball " + id + ": priority = " + me.getPriority ());
    me.setPriority (Thread.MAX_PRIORITY);
    // System.out.println ("Ball " + id + ": priority = " + me.getPriority ());

    final long second = 1000;  // JCSP Timer units are milliseconds
    long interval = (long) (((float) second)/((float) speed) + 0.5);
    // System.out.println ("Ball " + id + ": interval = " + interval);

    final long blackout = 5*second;

    while (true) {

      // initialise data for new ball ...

      newGraphic.colour = new Color (random.nextInt ());
      newGraphic.width = range (30) + 10;
      newGraphic.height = range (30) + 10;
      newGraphic.x = range (graphicsDim.width - newGraphic.width);
      newGraphic.y = range (graphicsDim.height - newGraphic.height);

      oldGraphic.colour = newGraphic.colour;
      oldGraphic.width = newGraphic.width;
      oldGraphic.height = newGraphic.height;

      int deltaX = delta (10);
      int deltaY = delta (10);

      // System.out.println ("Ball " + id + ": initialX,Y = " + newGraphic.x + ", " + newGraphic.y);
      // System.out.println ("Ball " + id + ": initialW,H = " + newGraphic.width + ", " + newGraphic.height);
      // System.out.println ("Ball " + id + ": (deltaX, deltaY) = (" + deltaX + ", " + deltaY + ")");

      tim.sleep (id*second);                // bring the balls up one by one

      // System.out.println ("Ball " + id + ": alive");

      int countdown = life;

      long timeout;                         // timeouts will drift ... but never mind ...
      // long timeout = tim.read ();        // timeouts won't drift ... not wanted here ...

      while (countdown > 0) {

        timeout = tim.read () + interval;   // timeouts will drift ... but never mind ...
        //timeout += interval;             // timeouts won't drift ... not wanted here ...

        displayList.change (newCommand, displaySlot);

        final Graphic tmpA = oldGraphic;
        oldGraphic = newGraphic;
        newGraphic = tmpA;

        final GraphicsCommand tmpB = oldCommand;
        oldCommand = newCommand;
        newCommand = tmpB;

        int x = oldGraphic.x + deltaX;
        if ((x < 0) || ((x + oldGraphic.width) > graphicsDim.width)) {
          deltaX = -deltaX;
        }
        int y = oldGraphic.y + deltaY;
        if ((y < 0) || ((y + oldGraphic.height) > graphicsDim.height)) {
          deltaY = -deltaY;
        }
        newGraphic.x = x;
        newGraphic.y = y;

        countdown--;

        tim.after (timeout);

      }

      // System.out.println ("Ball " + id + ": dead");

      displayList.change (GraphicsCommand.NULL, displaySlot);

      dead.sync ();           // wait for all the other balls to die

      tim.sleep (blackout);   // blackout for 5 seconds ...

    }

  }

}
