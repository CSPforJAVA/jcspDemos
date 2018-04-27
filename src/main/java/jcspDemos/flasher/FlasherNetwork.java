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

package jcspDemos.flasher;
//////////////////////////////////////////////////////////////////////



import jcsp.lang.*;
import jcsp.awt.*;

public class FlasherNetwork implements CSProcess {

  final private long period;
  final private ActiveApplet activeApplet;

  public FlasherNetwork (final long period,
                         final ActiveApplet activeApplet) {
    this.period = period;
    this.activeApplet = activeApplet;
  }
    
  public void run () {

    final One2OneChannel mouseEvent = Channel.one2one ();
    final One2OneChannel appletConfigure = Channel.one2one ();

    activeApplet.addMouseEventChannel (mouseEvent.out());
    activeApplet.setConfigureChannel (appletConfigure.in());

    new Parallel (
      new CSProcess[] {
        activeApplet,
        new FlasherControl (period, mouseEvent.in(), appletConfigure.out())
      }
    ).run ();

  }

}
