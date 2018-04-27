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
import java.awt.*;

/**
 * @author P.H. Welch
 */
class FrameZapper implements CSProcess {

  private final Frame frame;
  private final ChannelInputInt destroy;
  private final ChannelOutputInt destroyAck;

  public FrameZapper (final Frame frame,
                      final ChannelInputInt destroy,
                      final ChannelOutputInt destroyAck) {
    this.frame = frame;
    this.destroy = destroy;
    this.destroyAck = destroyAck;
  }

  public void run () {

    destroy.read ();
    System.out.println ("FrameZapper: destroy.read () ... zapping frame ...");

    // Test cheat for continued operation of detached frame (Sun's Java Plug-in)
    // Timer tim = new Timer ();
    // for (int i = 45; i >= 0; i--) {
    //   tim.after (tim.read () + 1000);
    //   System.out.println ("FrameZapper: counting ... " + i);
    //  }

    frame.setVisible (false);
    frame.dispose ();
    System.out.println ("FrameZapper: frame zapped ... acknowledging destroy ...");
    destroyAck.write (0);

  }

}
