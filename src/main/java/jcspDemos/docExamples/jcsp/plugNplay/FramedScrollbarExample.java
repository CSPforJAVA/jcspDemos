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
import jcsp.util.ints.*;
import jcsp.plugNplay.*;

public class FramedScrollbarExample {

  public static void main (String argv[]) {
  
    // initial pixel sizes for the scrollbar frame
    
    final boolean horizontal = true;
  
    final int pixDown = horizontal ? 300 : 400;
    final int pixAcross = horizontal ? 400 : 300;
  
    // the event channel is wired up to the scrollbar & reports all slider movements ...

    final One2OneChannelInt event =
      Channel.one2oneInt (new OverWriteOldestBufferInt (10));

    // the configure channel is wired up to the scrollbar  ...

    final One2OneChannel configure = Channel.one2one ();

    // make the framed scrollbar (connecting up its wires) ...

    final FramedScrollbar scrollbar =
      new FramedScrollbar (
        "FramedScrollbar Demo", pixDown, pixAcross,
        configure.in (), event.out (),
        horizontal, 0, 10, 0, 100
      );

    // testrig ...

    new Parallel (
    
      new CSProcess[] {
      
        scrollbar,
        
        new CSProcess () {        
          public void run () {            
            while (true) {
              final int n = event.in ().read ();
              System.out.println ("FramedScrollbar ==> " + n);
            }            
          }          
        },
        
        new CSProcess () {        
          public void run () {
            final int second = 1000;                // time is in millisecs
            final int enabledTime = 10*second;
            final int disabledCountdown = 5;
            final CSTimer tim = new CSTimer ();
            while (true) {
              tim.sleep (enabledTime);
              configure.out ().write (Boolean.FALSE);
              for (int i = disabledCountdown; i > 0; i--) {
                System.out.println ("\t\t\t\tScrollbar disabled ... " + i);
                tim.sleep (second);
              }
              configure.out ().write (Boolean.TRUE);
              System.out.println ("\t\t\t\tScrollbar enabled ...");
            }            
          }          
        }
        
      }
    ).run ();

  }

}
