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

package jcspDemos.outputGuards;

import jcsp.lang.*;
import java.util.Random;

class SymmetricB implements CSProcess {

  private final AltingChannelInputInt in;
  private final AltingChannelOutputInt out;

  public SymmetricB (AltingChannelInputInt in, AltingChannelOutputInt out) {
    this.in = in;
    this.out = out;
  }

  public void run () {
    
    final Alternative alt = new Alternative (new Guard[] {in , out});
    final int IN = 0, OUT = 1;

    final Random rand = new Random ();
    final CSTimer tim = new CSTimer ();
    
    int a = -1, b = -1;
    
    while (true) {
      int period = (rand.nextInt ()) % 16;
      tim.sleep (period);
      b++;
      switch (alt.fairSelect ()) {
        case IN:
          a = in.read ();
	  System.out.println ("\t\t\t" + a);
        break;
        case OUT:
          out.write (b);
	  System.out.println ("\t" + b);
        break;
      }
    }
  }

}
