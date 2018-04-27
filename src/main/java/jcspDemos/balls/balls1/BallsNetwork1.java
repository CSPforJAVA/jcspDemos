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
public class BallsNetwork1 implements CSProcess {

  private final ActiveCanvas activeCanvas;

  private final BallsControl1 control;

  private final Ball1[] balls;

  public BallsNetwork1 (final int nBalls, final int speed, final int life,
                        final Container parent) {

    parent.setLayout (new BorderLayout ());

    final DisplayList displayList = new DisplayList ();
    // displayList.setMinRefreshInterval (10);
    // System.out.println ("BallsNetwork: displayList.setMinRefreshInterval (10) ...");

    final Barrier barrier = new Barrier ();
    final Barrier dead = new Barrier (nBalls);

    final One2OneChannel[] toBalls = Channel.one2oneArray (nBalls);

    final One2OneChannel toGraphics = Channel.one2one ();
    final One2OneChannel fromGraphics = Channel.one2one ();

    activeCanvas = new ActiveCanvas ();
    activeCanvas.setBackground (Color.black);
    activeCanvas.setPaintable (displayList);
    activeCanvas.setGraphicsChannels (toGraphics.in (), fromGraphics.out ());
    activeCanvas.setSize (parent.getSize ());

    // If the parent is an applet, the above setSize has no effect and the activeCanvas
    // is fitted to the "Center" area (see below) of the applet's panel.

    // If the parent is a frame, the above *does* define the size of the activeCanvas
    // and the size of the parent is expanded to wrap around when it is packed.

    System.out.println ("BallsNetwork adding ActiveCanvas to the parent ...");
    parent.add ("Center", activeCanvas);

    balls = new Ball1[nBalls];
    for (int i = 0; i < nBalls; i++) {
      balls[i] = new Ball1 (i, speed, life, dead, toBalls[i].in (), displayList, barrier);
    }

    control = new BallsControl1 (Channel.getOutputArray (toBalls), barrier, displayList,
                                 toGraphics.out (), fromGraphics.in (), speed);

  }

  public void run () {

    System.out.println ("BallsNetwork starting up ...");

    new Parallel (
      new CSProcess[] {
        activeCanvas,
        new Parallel (balls),
        control
      }
    ).run ();

  }

}

