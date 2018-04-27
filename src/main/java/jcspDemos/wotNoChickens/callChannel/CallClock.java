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

package jcspDemos.wotNoChickens.callChannel;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class CallClock implements CSProcess {

  public void run () {

    final CSTimer tim = new CSTimer ();
    final long startTime = tim.read ();

    while (true) {
      int tick = (int) (((tim.read () - startTime) + 500)/1000);
      System.out.println ("[TICK] " + tick);
      tim.sleep (1000);
    }

  }

}
