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

public class ActiveDialogExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ();

    final One2OneChannel event = Channel.one2one (new OverWriteOldestBuffer (10));

    final ActiveDialog dialog = new ActiveDialog (null, event.out (), root, "ActiveDialog Example");

    // root.setSize (400, 400);
    // root.setVisible (true);
    dialog.setSize (300, 200);
    dialog.setVisible (true);

    new Parallel (
      new CSProcess[] {
        dialog,
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
