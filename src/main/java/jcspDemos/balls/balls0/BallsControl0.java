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
import java.awt.*;

/**
 * @author P.H. Welch
 */
class BallsControl0 implements CSProcess {

  private static final int RED_SPEED = 7,
                           GREEN_SPEED = 13,
                           BLUE_SPEED = 19;

  private final ChannelOutput[] toBalls;
  private final DisplayList displayList;
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;

  public BallsControl0 (final ChannelOutput[] toBalls,
                        final DisplayList displayList,
                        final ChannelOutput toGraphics,
                        final ChannelInput fromGraphics) {
    this.toBalls = toBalls;
    this.displayList = displayList;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
  }

  private final static class Graphic implements GraphicsCommand.Graphic {
    public Color colour;
    public void doGraphic (Graphics g, Component c) {
      Dimension dim = c.getSize();
      g.setColor (colour);
      g.fillRect (0, 0, dim.width, dim.height);
    }
  }

  Graphic oldGraphic = new Graphic ();
  Graphic newGraphic = new Graphic ();

  GraphicsCommand oldCommand = new GraphicsCommand.General (oldGraphic);
  GraphicsCommand newCommand = new GraphicsCommand.General (newGraphic);

  public void run() {

    toGraphics.write (GraphicsProtocol.GET_DIMENSION);
    final Dimension graphicsDim = (Dimension) fromGraphics.read ();
    System.out.println ("BallsControl: graphics dimension = " + graphicsDim);

    final CSTimer tim = new CSTimer ();
    final long seed = tim.read ();

    // initialise data for background colour ...

    int colRed = 0, colGreen = 0, colBlue = 0,
        cvRed = RED_SPEED,  cvGreen = GREEN_SPEED,  cvBlue = BLUE_SPEED;

    newGraphic.colour = Color.black;

    displayList.set (newCommand);

    for (int i = 0; i < toBalls.length; i++) {
      toBalls[i].write (graphicsDim);
      toBalls[i].write (new Long (seed));
    }

    //final Thread me = Thread.currentThread ();
    //me.setPriority (Thread.MAX_PRIORITY);

    final long second = 1000;  // JCSP Timer units are milliseconds
    long interval = second / 20;

    long timeout;                         // timeouts will drift ... but never mind ...
    // long timeout = tim.read ();        // timeouts won't drift ... not wanted here ...

    while (true) {

        timeout = tim.read () + interval;   // timeouts will drift ... but never mind ...
        // timeout += interval;             // timeouts won't drift ... not wanted here ...

        final Graphic tmpA = oldGraphic;
        oldGraphic = newGraphic;
        newGraphic = tmpA;

        final GraphicsCommand tmpB = oldCommand;
        oldCommand = newCommand;
        newCommand = tmpB;

        colRed += cvRed;
        if (colRed > 255 - RED_SPEED) cvRed = -RED_SPEED; else if (colRed < RED_SPEED) cvRed = RED_SPEED;
        colGreen += cvGreen;
        if (colGreen > 255 - GREEN_SPEED) cvGreen = -GREEN_SPEED; else if (colGreen < GREEN_SPEED) cvGreen = GREEN_SPEED;
        colBlue += cvBlue;
        if (colBlue > 255 - BLUE_SPEED) cvBlue = -BLUE_SPEED; else if (colBlue < BLUE_SPEED) cvBlue = BLUE_SPEED;
        newGraphic.colour = new Color (colRed, colGreen, colBlue);

        tim.after (timeout);

        displayList.change (newCommand, 0);

     }

  }

}
