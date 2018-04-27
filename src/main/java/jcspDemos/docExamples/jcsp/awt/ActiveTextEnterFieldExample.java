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

import jcsp.lang.*;
import jcsp.util.*;
import jcsp.awt.*;
import java.awt.*;

public class ActiveTextEnterFieldExample {

  public static void main (String argv[]) {

    final ActiveClosingFrame frame =
      new ActiveClosingFrame ("ActiveTextEnterField Example");

    final Any2OneChannel event = Channel.any2one (new OverWriteOldestBuffer (10));

    final String[] string =
      {"Entia Non Sunt Multiplicanda Praeter Necessitatem",
       "Less is More ... More or Less",
       "Everything we do, we do it to you",
       "Race Hazards - What Rice Hozzers?",
       "Cogito Ergo Occam"};

    final String goodbye = "Goodbye World";

    final ActiveTextEnterField[] activeText =
      new ActiveTextEnterField[string.length];

    for (int i = 0; i < string.length; i++) {
      activeText[i] = new ActiveTextEnterField (null, event.out (), string[i]);
    }

    Panel panel = new Panel (new GridLayout (string.length, 1));
    for (int i = 0; i < string.length; i++) {
      panel.add (activeText[i].getActiveTextField ());
    }

    final Frame realFrame = frame.getActiveFrame ();
    realFrame.setBackground (Color.green);
    realFrame.add (panel);
    realFrame.pack ();
    realFrame.setVisible (true);

    new Parallel (
      new CSProcess[] {
        frame,
        new Parallel (activeText),
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              String s = (String) event.in ().read ();
              System.out.println (s);
              running = (! s.equals (goodbye));
            }
            realFrame.setVisible (false);
            System.exit (0);
          }
        }
      }
    ).run ();
  }

}
