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
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class BallsMain1 extends ActiveApplet {

  public static final int minWidth = 300;
  public static final int maxWidth = 1024;

  public static final int maxHeight = 768;
  public static final int minHeight = 100;

  public static final int minBalls = 1;
  public static final int maxBalls = 100;
  public static final int defaultBalls = 10;

  public static final int minSpeed = 1;
  public static final int maxSpeed = 100;
  public static final int defaultSpeed = 20;

  public static final int minLife = 1;
  public static final int maxLife = 100;
  public static final int defaultLife = 20;

  public void init () {
    final int nBalls = getAppletInt ("balls", minBalls, maxBalls, defaultBalls);
    final int speed = getAppletInt ("speed", minSpeed, maxSpeed, defaultSpeed);
    final int life = getAppletInt ("life", minLife, maxLife, defaultLife);
    setProcess (new BallsNetwork1 (nBalls, speed, life, this));
  }

  public static final String TITLE = "Bouncing Balls [static background]";
  public static final String DESCR =
  	"Shows the use of a DisplayList and the ActiveCanvas when animating a number of objects. This is " +
  	"the same as the main Bouncing Balls demonstration but without the control process changing the " +
  	"background colour.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.addPrompt ("width", minWidth, maxWidth, 640);
  	Ask.addPrompt ("height", minHeight, maxHeight, 480);
  	Ask.addPrompt ("balls", minBalls, maxBalls, defaultBalls);
  	Ask.addPrompt ("speed (movements/second)", minSpeed, maxSpeed, defaultSpeed);
  	Ask.addPrompt ("life (seconds/ball)", minLife, maxLife, defaultLife);
  	Ask.show ();
  	final int width = Ask.readInt ("width");
  	final int height = Ask.readInt ("height");
  	final int nBalls = Ask.readInt ("balls");
  	final int speed = Ask.readInt ("speed (movements/second)");
  	final int life = Ask.readInt ("life (seconds/ball)");
  	Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame (TITLE);
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final BallsNetwork1 ballsNetwork =
      new BallsNetwork1 (nBalls, speed, life, activeFrame);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        ballsNetwork
      }
    ).run ();

  }

}
