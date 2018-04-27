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

package jcspDemos.docExamples.jcsp.plugNplay;

import jcsp.lang.*;
import jcsp.util.*;
import jcsp.plugNplay.*;

public class FramedButtonGridExample {

  public static void main (String argv[]) {
  
    // labels for the grid of buttons

    final String[][] label = {
      new String[] {"Java", "occam-pi", "Handel-C"},
      new String[] {"C", "C++", "C#"},
      new String[] {"Haskell", "Modula", "Goodbye World"}
    };

    final int nDown = label.length;
    final int nAcross = label[0].length;

    // initial pixel sizes for the frame for the grid of buttons

    final int pixDown = 20 + (nDown*100);
    final int pixAcross = nAcross*120;
  
    // all button events are wired (for this example) to the same channel ...

    final Any2OneChannel allEvents =
      Channel.any2one (new OverWriteOldestBuffer (10));

    final Any2OneChannel[][] event = new Any2OneChannel[nDown][nAcross];
    
    for (int i = 0; i < nDown; i++) {
      for (int j = 0; j < nAcross; j++) {
        event[i][j] = allEvents;
      }
    }

    // make the grid of buttons (each one separately configured) ...

    final One2OneChannel[][] configure = new One2OneChannel[nDown][nAcross];
    
    for (int i = 0; i < nDown; i++) {
      configure[i] = Channel.one2oneArray (nAcross);
    }

    final ChannelInput[][] configureIn = new ChannelInput[nDown][nAcross];
    final ChannelOutput[][] eventOut = new ChannelOutput[nDown][nAcross];
    
    for (int i = 0; i < nDown; i++) {
      configureIn[i] = Channel.getInputArray (configure[i]);
      eventOut[i] = Channel.getOutputArray (event[i]);
    }

    final FramedButtonGrid grid =
      new FramedButtonGrid (
        "FramedButtonGrid Demo", nDown, nAcross,
        pixDown, pixAcross, configureIn, eventOut
      );

    // testrig ...

    new Parallel (
    
      new CSProcess[] {
      
        grid,
        
        new CSProcess () {
        
          public void run () {
    
            for (int i = 0; i < nDown; i++) {
              for (int j = 0; j < nAcross; j++) {
                configure[i][j].out ().write (label[i][j]);
              }
            }
            
            boolean running = true;
            while (running) {
              final String s = (String) allEvents.in ().read ();
              System.out.println ("Button `" + s + "' pressed ...");
              running = (s != label[nDown - 1][nAcross - 1]);
            }
            
            System.exit (0);
            
          }
          
        }
        
      }
    ).run ();

  }

}
