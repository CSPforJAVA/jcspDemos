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

package jcspDemos.altingBarriers;

import jcsp.userIO.*;
import jcsp.lang.*;
import jcsp.plugNplay.*;

public class AltingBarrierGadget0Demo0 {

  public static void main (String[] argv) {

    // final int nUnits = 8;

    final int nUnits = Ask.Int ("\nnUnits = ", 3, 10);
    
    // make the buttons

    final One2OneChannel[] event = Channel.one2oneArray(nUnits);
    
    final One2OneChannel[] configure = Channel.one2oneArray (nUnits);

    final boolean horizontal = true;

    final FramedButtonArray buttons =
      new FramedButtonArray (
        "AltingBarrier: Gadget 0, Demo 0", nUnits, 120, nUnits*100,
         horizontal, Channel.getInputArray(configure), Channel.getOutputArray(event)
      );

    // construct an array of front-ends to a single alting barrier
    
    final AltingBarrier[] group = AltingBarrier.create (nUnits);

    // make the gadgets

    final AltingBarrierGadget0[] gadgets = new AltingBarrierGadget0[nUnits];
    for (int i = 0; i < gadgets.length; i++) {
      gadgets[i] = new AltingBarrierGadget0 (event[i].in(), group[i], configure[i].out());
    }

    // run everything

    new Parallel (
      new CSProcess[] {
        buttons, new Parallel (gadgets)
      }
    ).run ();

  }

}
