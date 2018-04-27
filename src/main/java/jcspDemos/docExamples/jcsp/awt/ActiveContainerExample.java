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
import jcsp.util.*;
import jcsp.lang.*;
import jcsp.awt.*;

public class ActiveContainerExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ("ActiveContainer Example");

    final One2OneChannel mouseEvent = Channel.one2one (new OverWriteOldestBuffer (10));

    final ActiveContainer container = new ActiveContainer ();
    container.addMouseEventChannel (mouseEvent.out ());

    root.add (container);
    root.setSize (400, 400);
    root.setVisible (true);

    new Parallel (
      new CSProcess[] {
        container,
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              final MouseEvent event = (MouseEvent) mouseEvent.in ().read ();
              switch (event.getID ()) {
                case MouseEvent.MOUSE_ENTERED:
                  System.out.println ("MOUSE_ENTERED");
                break;
                case MouseEvent.MOUSE_EXITED:
                  System.out.println ("MOUSE_EXITED");
                break;
                case MouseEvent.MOUSE_PRESSED:
                  System.out.println ("MOUSE_PRESSED");
                break;
                case MouseEvent.MOUSE_RELEASED:
                  System.out.println ("MOUSE_RELEASED");
                break;
                case MouseEvent.MOUSE_CLICKED:
                  if (event.getClickCount() > 1) {
                    System.out.println ("MOUSE_DOUBLE_CLICKED ... goodbye!");
                    running = false;
                  } else {
                    System.out.println ("MOUSE_CLICKED ... *double* click to quit!");
                  }
                break;
              }
            }
            root.setVisible (false);
            System.exit (0);
          }
        }
      }
    ).run ();
  }

}
