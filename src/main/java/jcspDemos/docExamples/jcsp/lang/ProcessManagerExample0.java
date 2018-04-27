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

public class ProcessManagerExample0 {

  public static void pause (int time) {
    try {Thread.sleep (time);} catch (InterruptedException e) {}
  }

  public static void main (String[] argv) {

    System.out.println ("*** start the managed process");

    new ProcessManager (
      new CSProcess () {
        public void run () {
          while (true) {
            System.out.println (":-) managed process running in the background");
            pause (500);
          }
        }
      }
    ).start ();

    System.out.println ("*** I'm still executing as well");
    System.out.println ("*** I'm going to take 5 ...");
    pause (5000);
    System.out.println ("*** I'll take another 5 ...");
    pause (5000);
    System.out.println ("*** I'll finish now ... so the network should as well.");
  }
}