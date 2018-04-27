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

public class ActiveWindowExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ("ActiveWindow Example");

    final One2OneChannel event = Channel.one2one (new OverWriteOldestBuffer (10));

    final ActiveWindow window = new ActiveWindow (null, event.out (), root);

    root.setSize (400, 400);
    root.setVisible (true);
    window.setVisible (true);

    new Parallel (
      new CSProcess[] {
        window,
        new CSProcess () {
          public void run () {
            while (true) {
              WindowEvent w = (WindowEvent) event.in ().read ();
              System.out.println (w);
            }
          }
        }
      }
    ).run ();
  }

}
