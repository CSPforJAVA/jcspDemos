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

package jcspDemos.plasma;



import jcsp.lang.*;
import jcsp.awt.*;

import jcsp.userIO.Ask;

/**
 * This example is based on
 * <A HREF="http://rsb.info.nih.gov/plasma/">Sam's Java Plasma Applet</A>
 * by <A HREF="mailto:t-sammar@microsoft.com">Sam Marshall</A>.  It was modified to use
 * 8-bit images by <A HREF="mailto:M.vanGangelen@element.nl">Menno van Gangelen</A>.
 * This JCSP demonstration is by <A HREF="mailto:P.H.Welch@kent.ac.uk">Peter Welch</A>.
 *
 * @author P.H. Welch
 */

public class PlasmaMain extends ActiveApplet {

  public static final String TITLE = "Plasma";
  public static final String DESCR =
  	"A benchmark for measuring the frame rate of the ActiveCanvas for a simple animation. The thread " +
  	"generating the images responds to changes in the parameters from events sent by the user interface " +
  	"controls.";

  public static final int minWidth = 256;
  public static final int minHeight = 256;

  public static final int maxWidth = 1024;
  public static final int maxHeight = 768;

  public void init () {
    setProcess (new PlasmaNetwork (this));
  }

  public static void main (String[] args) {

    Ask.app (TITLE, DESCR);
    Ask.addPrompt ("width", minWidth, maxWidth, 640);
    Ask.addPrompt ("height", minHeight, maxHeight, 480);
    Ask.show ();
    final int width = Ask.readInt ("width");
    final int height = Ask.readInt ("height");
    Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame (TITLE);
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final PlasmaNetwork plasmaNetwork = new PlasmaNetwork (activeFrame);

    //activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        plasmaNetwork
      }
    ).run ();

  }

}
