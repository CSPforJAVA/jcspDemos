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

package jcspDemos.docExamples.jcsp.awt;

import java.awt.*;
import java.awt.event.*;
import jcsp.lang.*;
import jcsp.util.*;
import jcsp.awt.*;

public class ActiveFrameButtonExample {

  public static void main (String argv[]) {

    final Any2OneChannel windowEvent = Channel.any2one (new OverWriteOldestBuffer (10));

    final ActiveFrame frame =
      new ActiveFrame (null, windowEvent.out (), "ActiveButton Example");

    final String[] label = {"Hello World", "Rocket Science", "CSP",
                            "Monitors", "Ignore Me", "Goodbye World"};

    final Any2OneChannel buttonEvent = Channel.any2one (new OverWriteOldestBuffer (10));

    final ActiveButton[] button = new ActiveButton[label.length];
    for (int i = 0; i < label.length; i++) {
      button[i] = new ActiveButton (null, buttonEvent.out (), label[i]);
    }

    frame.setSize (300, 200);
    frame.setLayout (new GridLayout (label.length/2, 2));
    for (int i = 0; i < label.length; i++) {
      frame.add (button[i]);
    }
    frame.setVisible (true);

    new Parallel (
      new CSProcess[] {
        new Parallel (button),
        new CSProcess () {                 // respond to window events
          public void run () {
            boolean running = true;
            while (running) {
              final WindowEvent w = (WindowEvent) windowEvent.in ().read ();
              System.out.println ("Window event: " + w);
              running = (w.getID () != WindowEvent.WINDOW_CLOSING);
            }
            frame.setVisible (false);
            System.exit (0);
          }
        },
        new CSProcess () {                 // respond to button events
          public void run () {
            boolean running = true;
            while (running) {
              final String s = (String) buttonEvent.in ().read ();
              System.out.println ("Button `" + s + "' pressed ...");
              running = (s != label[label.length - 1]);
            }
            frame.setVisible (false);
            System.exit (0);
          }
        }
      }
    ).run ();

  }

}
