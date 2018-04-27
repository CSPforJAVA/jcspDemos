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
import jcsp.lang.*;
import jcsp.util.*;
import jcsp.awt.*;

public class ActiveLabelExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ("ActiveLabel Example");

    final int nLabels = 8;
    final int countdown = 10;

    final One2OneChannel[] configureLabel = Channel.one2oneArray (nLabels);

    final ActiveLabel[] label = new ActiveLabel[nLabels];
    for (int i = 0; i < label.length; i++) {
      label[i] = new ActiveLabel (configureLabel[i].in (), "==>  " + countdown + "  <==");
      label[i].setAlignment (Label.CENTER);
    }

    final One2OneChannel configureButton = Channel.one2one ();
    final One2OneChannel event = Channel.one2one (new OverWriteOldestBuffer (10));

    final ActiveButton button = new ActiveButton (configureButton.in (), event.out (), "Start");

    root.setSize (300, 200);
    root.setLayout (new GridLayout (3, 3));
    for (int i = 0; i < nLabels + 1; i++) {
      if (i < 4) {
        root.add (label[i]);
      } else if (i == 4) {
        root.add (button);
      } else if (i > 4) {
        root.add (label[i - 1]);
      }
    }
    root.setVisible (true);

    new Parallel (
      new CSProcess[] {
        new Parallel (label),
        button,
        new CSProcess () {
          public void run () {
            final long second = 1000;
            CSTimer tim = new CSTimer ();
            Alternative alt = new Alternative (new Guard[] {event.in (), tim});
            event.in ().read ();              // wait for the start signal
            configureButton.out ().write ("Restart");
            int count = countdown;
            long timeout = tim.read () + second;
            while (count > 0) {
              tim.setAlarm (timeout);
              switch (alt.priSelect ()) {
                case 0:                 // reset signal
                  event.in ().read ();        // clear the reset
                  timeout = tim.read () + second;
                  count = countdown;
                break;
                case 1:                 // timeout signal
                  timeout += second;
                  count--;
                break;
              }
              final String newLabel = "==>  " + count + "  <==";
              for (int i = 0; i < nLabels; i++) {
                configureLabel[i].out ().write (newLabel);
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
