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

public class ActiveFileDialogExample {

  public static void main (String argv[]) {

    final Frame root = new Frame ();

    final One2OneChannel configure = Channel.one2one ();

    final One2OneChannel event = Channel.one2one (new OverWriteOldestBuffer (10));

    final ActiveFileDialog fileDialog =
      new ActiveFileDialog (configure.in (), event.out (), root, "ActiveFileDialog Example");

    new Parallel (
      new CSProcess[] {
        fileDialog,
        new CSProcess () {
          public void run () {
            String dir = ".";           // start directory for the file dialogue
            String file = "";
            while (file != null) {
              configure.out ().write (dir);
              configure.out ().write (Boolean.TRUE);
              dir = (String) event.in ().read ();
              file = (String) event.in ().read ();
              if (file != null)
                System.out.println ("Chosen file = `" + dir + file + "'");
            }
          System.exit (0);
          }
        }
      }
    ).run ();
  }

}
