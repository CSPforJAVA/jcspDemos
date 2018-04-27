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

package jcspDemos.docExamples.jcsp.lang;

import jcsp.lang.*;

public class Canteen implements CSProcess {

  private final AltingChannelInput supply;
    // from the cook
  private final AltingChannelInput request;   // from a philosopher
  private final ChannelOutput deliver;        // to a philosopher

  public Canteen (final AltingChannelInput supply,
                  final AltingChannelInput request, final ChannelOutput deliver) {
    this.supply = supply;
    this.request = request;
    this.deliver = deliver;
  }

  public void run() {

    final Guard[] guard = {supply, request};
    final boolean[] preCondition = new boolean[guard.length];
    final int SUPPLY = 0;
    final int REQUEST = 1;

    final Alternative alt = new Alternative (guard);

    final int maxChickens = 20;
    final int maxSupply = 4;
    final int limitChickens = maxChickens - maxSupply;

    final Integer oneChicken = new Integer (1);                 // ready to go!

    int nChickens = 0;             // invariant : 0 <= nChickens <= maxChickens

    while (true) {
      preCondition[SUPPLY] = (nChickens <= limitChickens);
      preCondition[REQUEST] = (nChickens > 0);
      switch (alt.priSelect (preCondition)) {
        case SUPPLY:
          nChickens += ((Integer) supply.read ()).intValue ();  // <= maxSupply
        break;
        case REQUEST:
          Object dummy = request.read ();  // we have to still input the signal
          deliver.write (oneChicken);      // preCondition ==> (nChickens > 0)
          nChickens--;
        break;
      }
    }

  }

}
