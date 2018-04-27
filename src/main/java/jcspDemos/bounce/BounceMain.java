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

package jcspDemos.bounce;



import jcsp.lang.*;
import jcsp.awt.*;
import java.awt.*;
import jcsp.userIO.Ask;

/**
 * @author P.H. Welch
 */
public class BounceMain extends ActiveApplet {

  public static final int minWidth = 300;
  public static final int minHeight = 100;

  public static final int maxWidth = 1024;
  public static final int maxHeight = 768;

  public static final Params[] params =
    {new Params ("images/cube/", 1, 5, ".gif", Color.white),
     new Params ("images/duke/", 1, 17, ".gif", Color.lightGray),
     new Params ("images/earth/", 1, 30, ".gif", Color.white)};

  public void init () {
    setProcess (new BounceNetwork (params, getDocumentBase (), this));
  }

  public static final String TITLE = "Bounce";
  public static final String DESCR =
  	"Demonstrates animation of images using the ActiveCanvas. Three animation processes are created, each " +
  	"with an image that it will move around. ActiveScroll processes allow the speed of the animation to be " +
  	"controlled by sending messages to the animator process.";

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

    final BounceNetwork bounceNetwork = new BounceNetwork (params, null, activeFrame);

    activeFrame.pack ();
    activeFrame.setLocation ((maxWidth - width)/2, (maxHeight - height)/2);
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingframe,
        bounceNetwork
      }
    ).run ();

  }

  public static class Params {

    public final String path, suffix;
    public final int from, to;
    public final Color background;

    public Params (String path, int from, int to, String suffix,
                   Color background) {
      this.path = path;
      this.from = from;
      this.to = to;
      this.suffix = suffix;
      this.background = background;
    }

 }

}
