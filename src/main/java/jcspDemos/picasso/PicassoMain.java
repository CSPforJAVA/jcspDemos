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

package jcspDemos.picasso;



import jcsp.lang.*;
import jcsp.awt.*;
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class PicassoMain extends ActiveApplet {

  public static final String TITLE = "Picasso";
  public static final String DESCR =
  	"Demonstrates the handling of AWT mouse events by a basic drawing process.";

  public static final int minWidth = 100;
  public static final int maxWidth = 1024;

  public static final int maxHeight = 768;
  public static final int minHeight = 100;

  public void init () {
    setProcess (new PicassoNetwork (this));
  }

  public static void main (String argv[]) {

    Ask.app (TITLE, DESCR);
	Ask.addPrompt ("width", minWidth, maxWidth, 640);
	Ask.addPrompt ("height", minHeight, maxHeight, 480);
	Ask.show ();
    final int width = Ask.readInt ("width");
    final int height = Ask.readInt ("height");
    Ask.blank ();

    final ActiveClosingFrame activeClosingframe = new ActiveClosingFrame ("Picasso");
    final ActiveFrame activeFrame = activeClosingframe.getActiveFrame ();
    activeFrame.setSize (width, height);

    final PicassoNetwork picassoNetwork = new PicassoNetwork (activeFrame);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        picassoNetwork
      }
    ).run ();

  }

}
