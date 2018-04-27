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

public class PongScorer implements CSProcess {

  private final AltingChannelInputInt[] fromPaddle;
  private final ChannelOutput[] configureLabel;

  public PongScorer (final AltingChannelInputInt[] fromPaddle,
                     final ChannelOutput[] configureLabel) {
    this.fromPaddle = fromPaddle;
    this.configureLabel = configureLabel;
  }

  public void run () {

    final Alternative alt = new Alternative (fromPaddle);
  
    int left = 0;
    int right = 0;

    configureLabel[0].write ("0");
    configureLabel[1].write ("0");

    while (true) {
      switch (alt.fairSelect ()) {
        case 0:
          final int scoreLeft = fromPaddle[0].read ();
          left = (scoreLeft == 0) ? 0 : left + scoreLeft;
          configureLabel[0].write ((new Integer (left)).toString ());
        break;    
        case 1:
          final int scoreRight = fromPaddle[1].read ();
          right = (scoreRight == 0) ? 0 : right + scoreRight;
          configureLabel[1].write ((new Integer (right)).toString ());
        break;
      }
    }

  }

}
    
