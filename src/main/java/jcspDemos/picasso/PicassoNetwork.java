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
import jcsp.util.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
public class PicassoNetwork implements CSProcess {

  private final ActiveCanvas activeCanvas;

  private final Picasso picasso;

  public PicassoNetwork (final Container parent) {

    final Dimension size = parent.getSize ();

    parent.setLayout (new BorderLayout ());
    parent.setBackground (Color.blue);

    final One2OneChannel mouseEvent = Channel.one2one (new OverWriteOldestBuffer (10));
    final One2OneChannel mouseMotionEvent = Channel.one2one (new OverWriteOldestBuffer (1));

    final DisplayList display = new DisplayList ();

    final One2OneChannel toGraphics = Channel.one2one ();
    final One2OneChannel fromGraphics = Channel.one2one ();

    activeCanvas = new ActiveCanvas ();
    activeCanvas.setBackground (Color.black);
    activeCanvas.addMouseEventChannel (mouseEvent.out ());
    activeCanvas.addMouseMotionEventChannel (mouseMotionEvent.out ());
    activeCanvas.setPaintable (display);
    activeCanvas.setGraphicsChannels (toGraphics.in (), fromGraphics.out ());
    activeCanvas.setSize (size);

    // If the parent is an applet, the above setSize has no effect and the activeCanvas
    // is fitted to the "Center" area (see below) of the applet's panel.

    // If the parent is a frame, the above *does* define the size of the activeCanvas
    // and the size of the parent is expanded to wrap around when it is packed.

    System.out.println ("PicassoNetwork adding ActiveCanvas to the parent ...");
    parent.add ("Center", activeCanvas);

    picasso = new Picasso (size, mouseEvent.in (), mouseMotionEvent.in (), display);
    // picasso = new Picasso (size, mouseEvent, mouseMotionEvent, toGraphics, fromGraphics);

  }

  public void run () {

    System.out.println ("PicassoNetwork starting up ...");

    new Parallel (
      new CSProcess[] {
        activeCanvas,
        picasso
      }
    ).run ();

  }

}

