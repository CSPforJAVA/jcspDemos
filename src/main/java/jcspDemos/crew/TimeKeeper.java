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

package jcspDemos.crew;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class TimeKeeper implements CSProcess {

  private final ChannelOutputInt display, displayInfo;

  public TimeKeeper (final ChannelOutputInt display,
                     final ChannelOutputInt displayInfo) {
    this.display = display;
    this.displayInfo = displayInfo;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();
    long timeout = tim.read ();

    final long oneSecond = 1000;

    int count = 0;

    while (true) {

      display.write (PhilState.TIME);
      displayInfo.write (count);

      timeout += oneSecond;
      tim.after (timeout);

      count++;

    }

  }

}
