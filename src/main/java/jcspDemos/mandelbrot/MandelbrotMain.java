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

package jcspDemos.mandelbrot;



import jcsp.lang.*;
import jcsp.awt.*;
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class MandelbrotMain extends ActiveApplet {

  public static final String TITLE = "Mandelbrot";
  public static final String DESCR =
    	"Generates the mandelbrot set in an interactive browser window. The browser allows the color scheme to be modified, " +
    	"the number of iterations varied and to zoom into the generated image. This demonstration shows the " +
    	"farmer/worker/harvester approach to parallelisation and the handling of AWT events within the farmer to control " +
    	"the image.\n\n" +
    	"The parameters below specify the size of the generated image in pixels.\n\n" +
    	"To zoom into the image, left click and a box will appear to select an area. Use the up/down cursor keys to adjust " +
    	"the size of this area. Click again to zoom.";

  public static final int minWidth = 640;
  public static final int minHeight = 350;

  public static final int maxWidth = 1024;
  public static final int maxHeight = 768;

  public void init () {
    setProcess (new MandelNetwork (this));
  }

  public static void main (String[] args) {

    Ask.app (TITLE, DESCR);
    Ask.addPrompt ("width", minWidth, maxWidth, 800);
    Ask.addPrompt ("height", minHeight, maxHeight, 600);
    Ask.show ();
    final int width = Ask.readInt ("width");
    final int height = Ask.readInt ("height");
    Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame ("Mandelbrot Set");
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final MandelNetwork mandelbrot = new MandelNetwork (activeFrame);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        mandelbrot
      }
    ).run ();

  }

}
