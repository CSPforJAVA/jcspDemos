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

public class ActiveCheckboxExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ("ActiveCheckbox Example");

    final String[] box = {"Hello World", "Rocket Science", "CSP",
                          "Monitors", "Ignore Me", "Goodbye World"};

    final Any2OneChannel event = Channel.any2one (new OverWriteOldestBuffer (10));

    final ActiveCheckbox[] check = new ActiveCheckbox[box.length];
    for (int i = 0; i < box.length; i++) {
      check[i] = new ActiveCheckbox (null, event.out (), box[i]);
    }

    root.setSize (300, 200);
    root.setLayout (new GridLayout (box.length, 1));
    for (int i = 0; i < box.length; i++) {
     root.add (check[i]);
    }
    root.setVisible (true);

    new Parallel (
      new CSProcess[] {
        new Parallel (check),
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              final ItemEvent e = (ItemEvent) event.in ().read ();
              final String item = (String) e.getItem ();
              if (e.getStateChange () == ItemEvent.SELECTED) {
                System.out.println ("Checked ==> `" + item + "'");
                running = (item != box[box.length - 1]);
              } else {
                System.out.println ("Unchecked ==> `" + item + "'");
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
