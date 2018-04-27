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

package jcspDemos.pong;
//////////////////////////////////////////////////////////////////////



import jcsp.lang.*;
import java.awt.event.*;

public class PongKeyControl implements CSProcess {

  private final ChannelInput keyboard;
  private final ChannelOutputInt leftMove;
  private final ChannelOutputInt rightMove;

  public PongKeyControl (final ChannelInput keyboard,
                         final ChannelOutputInt leftMove,
                         final ChannelOutputInt rightMove) {
    this.keyboard = keyboard;
    this.leftMove = leftMove;
    this.rightMove = rightMove;
  }

  public void run () {
System.out.println ("PongKeyControl starting ...");
    while (true) {
      final KeyEvent keyEvent = (KeyEvent) keyboard.read ();
      if (keyEvent.getID () == KeyEvent.KEY_PRESSED) {
        switch (keyEvent.getKeyCode ()) {
          case KeyEvent.VK_A:
            leftMove.write (PongPaddle.UP);
          break;
          case KeyEvent.VK_Z:
            leftMove.write (PongPaddle.DOWN);
          break;
          case KeyEvent.VK_K:
            rightMove.write (PongPaddle.UP);
          break;
          case KeyEvent.VK_M:
            rightMove.write (PongPaddle.DOWN);
          break;
        }
      }
    }
  }

}
