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

package jcspDemos.docExamples.jcsp.lang;

import jcsp.lang.*;

public class ProcessManagerExample1 {

  public static void main (String[] argv) {

    final ProcessManager manager = new ProcessManager (
      new CSProcess () {
        public void run () {
          final CSTimer tim = new CSTimer ();
          long timeout = tim.read ();
          int count = 0;
          while (true) {
            System.out.println (count + " :-) managed process running ...");
            count++;
            timeout += 100;
            tim.after (timeout);   // every 1/10th of a second ...
          }
        }
      }
    );

    CSTimer tim = new CSTimer ();
    long timeout = tim.read ();

    System.out.println ("\n\n\t\t\t\t\t*** start the managed process");
    manager.start ();

    for (int i = 0; i < 10; i++) {
      System.out.println ("\n\n\t\t\t\t\t*** I'm still executing as well");
      timeout += 1000;
      tim.after (timeout);         // every second ...
    }

    System.out.println ("\n\n\t\t\t\t\t*** I'm finishing now!");

  }

}
