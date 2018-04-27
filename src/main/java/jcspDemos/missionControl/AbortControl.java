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
public class AbortControl implements CSProcess {
 
  private final AltingChannelInput fromButton;
  private final ChannelOutput toButton;
  private final AltingChannelInputInt cancel;
  private final ChannelOutputInt abort;
 
  public AbortControl (final AltingChannelInput fromButton,
                       final ChannelOutput toButton,
                       final AltingChannelInputInt cancel,
                       final ChannelOutputInt abort) {
    this.fromButton = fromButton;
    this.toButton = toButton;
    this.cancel = cancel;
    this.abort = abort;
  }

  public void run () {

    final Alternative alt = new Alternative (new Guard[] {fromButton, cancel});
    final int BUTTON = 0;
    final int CANCEL = 1;

    while (true) {

      cancel.read ();                       // get ready for next launch

      while (fromButton.pending ()) fromButton.read ();  // debounce
      toButton.write ("ABORT");
      toButton.write (Boolean.TRUE);        // enable the button

      switch (alt.priSelect ()) {
        case BUTTON:                        // abort button pressed
          abort.write (0);                  // try to abort the rocket
          toButton.write (Boolean.FALSE);   // disable the button
          toButton.write ("abort");
          fromButton.read ();               // clear the signal
          cancel.read ();                   // acknowledgement of the abort
        break;
        case CANCEL:                        // the rocket has been fired
          cancel.read ();                   // clear the signal
          abort.write (0);                  // acknowledge the firing
          toButton.write (Boolean.FALSE);   // disable the button
          toButton.write ("abort");
        break;
      }

    }

  }

}
