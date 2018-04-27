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

package jcspDemos.missionControl;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class MultiLaunchControl implements CSProcess {
 
  private final int interval;
  private final int start;
  private final AltingChannelInputInt abort;
  private final ChannelOutputInt cancel;
  private final AltingChannelInputInt hold;
  private final ChannelOutputInt countdown;
  private final ChannelOutputInt fire;
 
  public MultiLaunchControl (final int interval,
                             final int start,
                             final AltingChannelInputInt abort,
                             final ChannelOutputInt cancel,
                             final AltingChannelInputInt hold,
                             final ChannelOutputInt countdown,
                             final ChannelOutputInt fire) {
    this.interval = interval;
    this.start = start;
    this.abort = abort;
    this.cancel = cancel;
    this.hold = hold;
    this.countdown = countdown;
    this.fire = fire;
  }

  public void run () {

    final LaunchControl launchControl =
      new LaunchControl (start, abort, hold, countdown, fire);

    final CSTimer tim = new CSTimer ();
    final long seconds = 1000;

    final Alternative alt = new Alternative (new Guard[] {tim, hold});
    final int TIMEOUT = 0;
    final int HOLD = 1;

    hold.read ();                                         // start signal

    while (true) {

      cancel.write (0);                                   // enable abort

      launchControl.run ();
      int status = launchControl.getStatus ();

      cancel.write (status);                              // graceful
      if (status == LaunchControl.FIRED) abort.read ();   // reset

      boolean waiting = true;
      tim.setAlarm (tim.read () + (interval*seconds));

      while (waiting) {
        switch (alt.priSelect ()) {
          case TIMEOUT:
            waiting = false;
          break;
          case HOLD:
            hold.read ();
            fire.write (LaunchControl.HOLDING);
            hold.read ();
            fire.write (status);
          break;
        }
      }

    }

  }

}
