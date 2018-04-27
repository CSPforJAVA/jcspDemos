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

package jcspDemos.bounce;



import jcsp.lang.*;
import java.awt.event.*;

/**
 * @author P.H. Welch
 */
public class BounceController implements CSProcess {

  private final AltingChannelInput direction;
  private final AltingChannelInputInt speed;
  private final ChannelOutputInt control;
  private final int MAX_SCALE;

  public BounceController (final AltingChannelInput direction,
                           final AltingChannelInputInt speed,
                           final ChannelOutputInt control,
                           final int MAX_SCALE) {
    this.direction = direction;
    this.speed = speed;
    this.control = control;
    this.MAX_SCALE = MAX_SCALE;
  }

  public void run() {

    final Thread me = Thread.currentThread ();
    System.out.println ("BounceController " + " priority = " + me.getPriority ());
    me.setPriority (Thread.MIN_PRIORITY);
    System.out.println ("BounceController " + " priority = " + me.getPriority ());

    final int TICKS_PER_SECOND = 1000;
    final int MAX_FPS = 100;
    final int MIN_FPS = 1;
    final int SPAN_FPS = (MAX_FPS - MIN_FPS) + 1;
    final int MINFPS_MAXSCALE = MIN_FPS * MAX_SCALE;

    boolean forwards = true;
    int speedValue = 0;
    int interval = 0;
    long timeout = 0;

    final CSTimer tim = new CSTimer ();
    final Guard[] guard = {tim, speed, direction};
    final boolean[] preCondition = {false, true, true};
    final Alternative alt = new Alternative (guard);
    
    while (true) {
      switch (alt.fairSelect (preCondition)) {
        case 0:  // time-out
          // timeout += interval;
          timeout = tim.read () + interval;
          tim.setAlarm (timeout);
          if (forwards) {
            control.write (+1);
          } else {
            control.write (-1);
          }
        break;
        case 1:
          int value = MAX_SCALE - speed.read ();
          if (value > 0) {
            int fps = ((value - 1 ) * SPAN_FPS + MINFPS_MAXSCALE) / MAX_SCALE;
            interval = TICKS_PER_SECOND / fps;
            if (speedValue <= 0) {                 //  "<=" is work-around for slider bug in IE JVM
              timeout = tim.read () + interval;
              tim.setAlarm (timeout);
              preCondition[0] = true;
            }
          } else {
            preCondition[0] = false;
          }
          speedValue = value;
        break;  
        case 2:
          MouseEvent event = (MouseEvent) direction.read();
          if (event.getID () == MouseEvent.MOUSE_PRESSED) { 
            forwards = ! forwards;
          }
        break;
      }
    }

  }

}
