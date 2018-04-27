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
import java.awt.*;
import java.awt.event.*;

/**
 * @author P.H. Welch
 */
public class PanelControl implements CSProcess {
 
  private final AltingChannelInput fromPanel;
  private final ChannelOutput toPanel;
  private final ChannelOutputInt hold;
 
  public PanelControl (final AltingChannelInput fromPanel,
                       final ChannelOutput toPanel,
                       final ChannelOutputInt hold) {
    this.fromPanel = fromPanel;
    this.toPanel = toPanel;
    this.hold = hold;
  }

  public void run () {

    final PanelColour panelOff = new PanelColour (Color.lightGray);
    final PanelColour panelOn = new PanelColour (Color.green);

    boolean mousePresent = false;
    toPanel.write (panelOff);

    while (true) {

      switch (((MouseEvent) fromPanel.read ()).getID ()) {
        case MouseEvent.MOUSE_PRESSED:
          if (! mousePresent) {
            mousePresent = true;
            hold.write (0);
            toPanel.write (panelOn);
          } else {
            mousePresent = false;
            hold.write (0);
            toPanel.write (panelOff);
          }
        break;
      }

    }

  }

}
