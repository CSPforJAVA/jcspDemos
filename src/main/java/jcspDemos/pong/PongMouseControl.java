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

package jcspDemos.pong;
//////////////////////////////////////////////////////////////////////



import jcsp.lang.*;
import jcsp.awt.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class PongMouseControl implements CSProcess {

  public static final int FLASH_INTERVAL = 500;  // milli-seconds
  public static final int FLASH_OFF = -1;

  private final ChannelOutputInt toFlasher;
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;
  final private ChannelInput mouseEvent;

  public PongMouseControl (final ChannelOutputInt toFlasher,
                           final ChannelOutput toGraphics,
                           final ChannelInput fromGraphics,
                           final ChannelInput mouseEvent) {
    this.toFlasher = toFlasher;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
    this.mouseEvent = mouseEvent;
  }

  public void run() {
  
    // System.out.println ("PongMouseControl running ...");

    PriParallel.setPriority (Thread.MAX_PRIORITY);
    
    boolean mousePresent = false;

    while (true) {
    
      switch (((MouseEvent) mouseEvent.read ()).getID ()) {
      
        case MouseEvent.MOUSE_ENTERED:
          // System.out.println ("PongMouseControl: MouseEvent.MOUSE_ENTERED");
          toFlasher.write (FLASH_INTERVAL);
        break;
        
        case MouseEvent.MOUSE_EXITED:
          // System.out.println ("PongMouseControl: MouseEvent.MOUSE_EXITED");
          toFlasher.write (FLASH_OFF);
        break;
        
      }

      toGraphics.write (GraphicsProtocol.REQUEST_FOCUS);
      fromGraphics.read ();
    
    }

  }

}
