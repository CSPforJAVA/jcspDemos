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

package jcspDemos.wotNoChickens.channel;



//|
//| This program shows a use of the ALT mechanism.  It is a re-implementation
//| of the Starving Philosophers example but now has the Canteen programmed
//| properly as an active process.  Here is the College:
//|
//|
//|      0   1   2   3   4
//|      :)  :)  :)  :)  :)      ___________             ________
//|      |   |   |   |   |       |         |             |      |
//|    ---------------------<->--| Canteen |------<------| Cook |
//|       service/deliver        |_________|    supply   |______|
//|
//|
//|
//| This time, although Philsopher 0 is just as greedy, no one starves.
//|

import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class Canteen implements CSProcess {

  //The Canteen is an active object -- a pure SERVER process for its `supply'
  //and `service'/`deliver' Channels, giving priority to the former.
  //
  //Philosphers eat chickens.  They queue up at the Canteen on its `service'
  //Channel.  They only get served when chickens are available -- otherwise,
  //they just have to wait.  Once they have got `service', they are dispensed
  //a chicken down the `deliver' Channel.
  //
  //The Chef cooks chickens.  When a batch ready is ready, he/she queues up at
  //the Canteen on its `supply' Channel.  Setting down the batch takes around
  //3 seconds and the Chef is made to hang about this has happened.

  private final AltingChannelInputInt service;
    // shared from all Philosphers (any-1)
  private final ChannelOutputInt deliver;         // shared to all Philosphers (but only used 1-1)
  private final AltingChannelInputInt supply;     // from the Chef (1-1)

  public Canteen (AltingChannelInputInt service, ChannelOutputInt deliver,
                  AltingChannelInputInt supply) {
    this.service = service;
    this.deliver = deliver;
    this.supply = supply;
  }

  public void run () {

    final Alternative alt = new Alternative (new Guard[] {supply, service});
    final boolean[] precondition = {true, false};
    final int SUPPLY = 0;
    final int SERVICE = 1;

    final CSTimer tim = new CSTimer ();

    int nChickens = 0;

    System.out.println ("            Canteen : starting ... ");
    while (true) {
      precondition[SERVICE] = (nChickens > 0);
      switch (alt.fairSelect (precondition)) {
        case SUPPLY:
          int value = supply.read ();        // new batch of chickens from the Chef
          System.out.println ("            Canteen : ouch ... make room ... this dish is very hot ... ");
          tim.after (tim.read () + 3000);   // this takes 3 seconds to put down
          nChickens += value;
          System.out.println ("            Canteen : more chickens ... " +
                               nChickens + " now available ... ");
          supply.read ();                   // let the Chef get back to cooking
        break;
        case SERVICE:
          service.read ();                  // Philosopher wants a chicken
          System.out.println ("      Canteen : one chicken coming down ... " +
                               (nChickens - 1) + " left ... ");
          deliver.write (1);             // serve one chicken
          nChickens--;
        break;
       }
    }
  }

}
