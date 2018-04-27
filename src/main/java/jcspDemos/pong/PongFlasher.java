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

public class PongFlasher implements CSProcess {

  private final ChannelInput fromControl;
  private final AltingChannelInputInt trigger;
  private final DisplayList displayList;

  public PongFlasher (final ChannelInput fromControl,
                      final AltingChannelInputInt trigger,
                      final DisplayList displayList) {
    this.fromControl = fromControl;
    this.trigger = trigger;
    this.displayList = displayList;
  }

  private final static class Graphic implements GraphicsCommand.Graphic {
    public Color colour= Color.black;
    public void doGraphic (Graphics g, Component c) {
      Dimension dim = c.getSize();
      g.setColor (colour);
      g.fillRect (0, 0, dim.width, dim.height);
    }
  }

  public void run() {

    Graphic oldGraphic = new Graphic ();
    Graphic newGraphic = new Graphic ();

    GraphicsCommand oldCommand = new GraphicsCommand.General (oldGraphic);
    GraphicsCommand newCommand = new GraphicsCommand.General (newGraphic);

    displayList.set (newCommand);
    
    final Random random = new Random ();
  
    System.out.println ("Flasher running ...");

    fromControl.read ();
    // let control process continue (and let the balls pick displaylist slots)

    final CSTimer tim = new CSTimer ();

    final Thread me = Thread.currentThread ();
    me.setPriority (Thread.MAX_PRIORITY);

    final long second = 1000;  // JCSP Timer units are milliseconds
    long interval = -1;        // negative ==> not flashing

    final Alternative alt = new Alternative (new Guard[] {trigger, tim});
    final boolean[] preCondition = {true, interval >= 0};
    final int TRIGGER = 0;
    final int TIMER = 1;

    long timeout = 0;
    
    boolean mousePresent = false;
    boolean running = true;

    while (running) {
    
      final Graphic tmpGraphic = oldGraphic;
      oldGraphic = newGraphic;
      newGraphic = tmpGraphic;
      
      final GraphicsCommand tmpCommand = oldCommand;
      oldCommand = newCommand;
      newCommand = tmpCommand;

      switch (alt.priSelect (preCondition)) {

        case TRIGGER:
          interval = trigger.read ();
          if (interval >= 0) {
            timeout = tim.read () + interval;
            tim.setAlarm (timeout);
            newGraphic.colour = new Color (random.nextInt ());
            preCondition[TIMER] = true;
          } else {
            newGraphic.colour = Color.black;
            preCondition[TIMER] = false;
          }
        break;

        case TIMER:
          timeout += interval;
          tim.setAlarm (timeout);
          newGraphic.colour = new Color (random.nextInt ());
        break;

      }

      displayList.change (newCommand, 0);

    }

  }

}
