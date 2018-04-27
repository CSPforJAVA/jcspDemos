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

package jcspDemos.splat;



import jcsp.lang.*;
import jcsp.util.ints.*;
import jcsp.awt.*;
import java.awt.*;
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class SplatMain extends ActiveApplet {

  public static final String TITLE = "Splat";
  public static final String DESCR = "Demonstrates pixel access using the active AWT components.";

  public static final int minWidth = 335;
  public static final int minHeight = 100;
  public static final int minBurst = 20;

  public static final int maxWidth = 1024;
  public static final int maxHeight = 768;
  public static final int maxBurst = 1000;

  public static final int squareFactor = 30;

  public void init () {

    final Dimension size = getSize ();

    final int standbyBurst = 100;
    final int standbyAcross = 4;
    final int standbyDown = 1;

    final int nAcross = getAppletInt ("nAcross", 1, size.width/squareFactor, standbyAcross);
    final int nDown = getAppletInt ("nDown", 1, size.height/squareFactor, standbyDown);
    final int burst = getAppletInt ("burst", minBurst, maxBurst, standbyBurst);

    final boolean detach = getAppletBoolean ("detach", false);

    if (detach) {

      // final One2OneChannelInt stopStart = Channel.one2oneInt (new OverWriteOldestBufferInt (1));
      // setStopStartChannel (stopStart);
      final One2OneChannelInt destroy = Channel.one2oneInt (new OverWriteOldestBufferInt (1));
      final One2OneChannelInt destroyAck = Channel.one2oneInt ();
      setDestroyChannels (destroy.out (), destroyAck.in ());
      // setDestroyChannels (destroy, destroyAck, -1);
    // cheat for Sun's Java Plug-in

      final Frame frame = new Frame ("Splat");
      frame.setSize (size);
      final Splat splat = new Splat (nAcross, nDown, burst, frame, destroy.in (), destroyAck.out ());
      frame.pack ();
      frame.setLocation ((maxWidth - size.width)/2, (maxHeight - size.height)/2);
      frame.setVisible (true);
      frame.toFront ();
      setProcess (splat);
    } else {
      setProcess (new Splat (nAcross, nDown, burst, this, null, null));
    }

  }

  public static void main (String[] args) {

    Ask.app (TITLE, DESCR);
    Ask.addPrompt ("width", minWidth, maxWidth, 640);
    Ask.addPrompt ("height", minHeight, maxHeight, 480);
    Ask.addPrompt ("squares across", 1, 50, 4);
    Ask.addPrompt ("squares down", 1, 50, 4);
    Ask.addPrompt ("burst", minBurst, maxBurst, 600);
	Ask.show ();
    final int width = Ask.readInt ("width");
    final int height = Ask.readInt ("height");
    final int nAcross = Ask.readInt ("squares across");
    final int nDown = Ask.readInt ("squares down");
    final int burst = Ask.readInt ("burst");
    Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame ("Splat");
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final Splat splat = new Splat (nAcross, nDown, burst, activeFrame, null, null);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        splat
      }
    ).run ();

  }

}
