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

public class ActiveChoiceExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ("ActiveChoice Example");

    final One2OneChannel close = Channel.one2one (new OverWriteOldestBuffer (1));

    final ActiveChoice choice = new ActiveChoice (null, close.out ());

    final String[] menu = {"Hello World", "Rocket Science", "CSP",
                           "Monitors", "Ignore Me", "Goodbye World"};

    for (int i = 0; i < menu.length; i++) {
      choice.add (menu[i]);
    }

    root.setSize (200, 100);
    root.add (choice);
    root.setVisible (true);

    new Parallel (
      new CSProcess[] {
        choice,
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              ItemEvent e = (ItemEvent) close.in ().read ();
              String item = (String) e.getItem ();
              System.out.println ("Selected ==> `" + item + "'");
              running = (item != menu[menu.length - 1]);
            }
            root.setVisible (false);
            System.exit (0);
          }
        }
      }
    ).run ();

  }

}
