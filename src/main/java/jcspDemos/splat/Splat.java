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
import jcsp.util.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
class Splat implements CSProcess {

  private final ActiveCanvas activeCanvas;
  private final ActiveButton[] button;
  private final ActiveButton rearrange;
  private final SplatterControl control;
  private final Splatter splatter;
  private FrameZapper frameZapper = null;

  public Splat (final int nAcross, final int nDown, final int burst, final Container parent,
                final ChannelInputInt destroy, final ChannelOutputInt destroyAck) {

    parent.setLayout (new BorderLayout ());
    parent.setBackground (Color.green);

    System.out.println ("Splat creating channels ...");
    
    final One2OneChannel[] event =
      Channel.one2oneArray (SplatterControl.NUMBER, new OverWriteOldestBuffer (1));
    final One2OneChannel[] configure =
      Channel.one2oneArray (SplatterControl.NUMBER);
    
    final One2OneChannel rearrangeEvent =
      Channel.one2one (new OverWriteOldestBuffer (1));
    final One2OneChannel rearrangeConfigure = Channel.one2one ();

    final One2OneChannel report = Channel.one2one ();

    final One2OneChannel toGraphics = Channel.one2one ();
    final One2OneChannel fromGraphics = Channel.one2one ();

    System.out.println ("Splat created channels");
    System.out.println ("Splat creating ActiveButtons ...");

    button = new ActiveButton[SplatterControl.NUMBER];
    for (int i = 0; i < SplatterControl.NUMBER; i++) {
      button[i]
        = new ActiveButton (configure[i].in (), event[i].out (), "XXXXXXXXXXXXX");
      System.out.println ("  button " + i + " " + button[i].getPreferredSize ());
    }

    rearrange = new ActiveButton (rearrangeConfigure.in (), rearrangeEvent.out (), "XXXXXXXXXXXXX");

    System.out.println ("Splat created ActiveButtons ... now adding them to the parent ...");

    final Panel north = new Panel ();
    final Panel south = new Panel ();

    north.add (button[SplatterControl.RESTART]);
    north.add (button[SplatterControl.FREEZE]);
    north.add (button[SplatterControl.CLEAR]);
    south.add (button[SplatterControl.SPLAT]);
    south.add (rearrange);
    south.add (button[SplatterControl.UNSPLAT]);

    System.out.println ("  Panel north: " + north.getPreferredSize ());
    System.out.println ("  Panel south: " + south.getPreferredSize ());
    System.out.println ("  Container parent: " + parent.getPreferredSize ());

    parent.add ("North", north);
    parent.add ("South", south);
    System.out.println ("  Container parent: " + parent.getSize ());

    System.out.println ("Splat added buttons to the parent ... creating ActiveCanvas ...");
    activeCanvas = new ActiveCanvas ();
    activeCanvas.setGraphicsChannels (toGraphics.in (), fromGraphics.out ());
    activeCanvas.setSize (parent.getSize ());

    // If the parent is an applet, the above setSize has no effect and the activeCanvas
    // is fitted to the "Center" area (see below) of the applet's panel.

    // If the parent is a frame, the above *does* define the size of the activeCanvas
    // and the size of the parent is expanded to wrap around when it is packed.

    System.out.println ("Splat adding ActiveCanvas to the parent ...");
    parent.add ("Center", activeCanvas);

    System.out.println ("Splat now creating SplatterControl ...");
    control = new SplatterControl (Channel.getInputArray(event), Channel.getOutputArray(configure), report.out ());

    System.out.println ("Splat now creating Splatter ...");
    splatter = new Splatter (nAcross, nDown, burst, report.in (),
                             rearrangeConfigure.out (), rearrangeEvent.in (),
                             toGraphics.out (), fromGraphics.in ());
System.out.println ("Splat: parent's class is " + parent.getClass ());
    if ((parent instanceof Frame) && (destroy != null) && (destroyAck != null)) {
      frameZapper = new FrameZapper ((Frame) parent, destroy, destroyAck);
System.out.println ("Splat: just made FrameZapper");
    }

  }

  public void run () {

    System.out.println ("Splat starting up the network ...");

    new Parallel (
      new CSProcess[] {
        activeCanvas,
        rearrange,
        control,
        splatter,
        (frameZapper != null) ? (CSProcess) frameZapper : (CSProcess) new Skip (),
        new Parallel (button)
      }
    ).run ();

  }

}
