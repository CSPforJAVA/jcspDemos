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
import jcsp.util.*;
import jcsp.util.ints.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
public class Bounce implements CSProcess {

  private final Image[] images;
  private final Color background;

  private final Panel panel;

  private final ActiveCanvas activeCanvas;
  private final ActiveScrollbar activeScrollBar;

  private final BounceController bounceController;
  private final ImageAnimator imageAnimator;

  public Bounce (Image[] images, Color background, final Container parent, final int nSiblings) {

    this.images = images;
    this.background = background;

    // channels ...

    final One2OneChannel mouseEvent = Channel.one2one (new OverWriteOldestBuffer (10));
    final One2OneChannel toGraphics = Channel.one2one ();
    final One2OneChannel fromGraphics = Channel.one2one ();
    final One2OneChannelInt control = Channel.one2oneInt ();
    final One2OneChannelInt scroll = Channel.one2oneInt (new OverWriteOldestBufferInt (1));

    final DisplayList displayList = new DisplayList ();

    // awt processes ...

    activeCanvas = new ActiveCanvas ();
    activeCanvas.addMouseEventChannel (mouseEvent.out ());
    activeCanvas.setGraphicsChannels (toGraphics.in (), fromGraphics.out ());
    activeCanvas.setPaintable (displayList);
    activeCanvas.setBackground (background);
    final Dimension parentSize = parent.getSize ();
    activeCanvas.setSize (parentSize.width, parentSize.height/nSiblings);

    // If the parent is an applet, the above setSize has no effect and the activeCanvas
    // is fitted to the "Center" area (see below) of the applet's panel.
    // If the parent is a frame, the above *does* define the size of the activeCanvas
    // and the size of the parent is expanded to wrap around when it is packed.

    final int MAX_SCALE = 200;
    final int SLIDER = 30;
    activeScrollBar = new ActiveScrollbar (
      null, scroll.out (), Scrollbar.VERTICAL,
      MAX_SCALE, SLIDER, 0, MAX_SCALE + SLIDER
    );

    panel = new Panel ();
    panel.setLayout (new BorderLayout ());
    panel.add ("West", activeScrollBar);
    panel.add ("Center", activeCanvas);

    // application-specific processes ...

    bounceController = new BounceController (
      mouseEvent.in (), scroll.in (), control.out (), MAX_SCALE
    );

    imageAnimator = new ImageAnimator (
      control.in (), toGraphics.out (), fromGraphics.in (), displayList, images, 1, 1
    );

  }

  public Panel getPanel () {
    return panel;
  }


  public void run () {

    new Parallel (
      new CSProcess[] {
        activeScrollBar,
        activeCanvas,
        bounceController,
        imageAnimator
      }
    ).run ();

  }

}
