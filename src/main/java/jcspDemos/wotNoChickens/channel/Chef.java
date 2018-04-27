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



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class Chef implements CSProcess {

  //The Chef is an active object.  He/she cooks chickens in batches of four --
  //taking around 2 seconds per batch -- and then sends them to the Canteen.
  //The Chef is delayed in the Canteen, waiting for an acknowledge that the
  //batch has been set down OK.
  //
  //This cycle continues indefinitely.

  private final ChannelOutputInt supply;

  public Chef (ChannelOutputInt supply) {
    this.supply = supply;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();

    int n_chickens;

    System.out.println ("            Chef    : starting ... ");
    while (true) {
      // cook 4 chickens
      System.out.println ("            Chef    : cooking ... ");
      tim.after (tim.read () + 2000);       // this takes 3 seconds to cook
      n_chickens = 4;
      System.out.println ("            Chef    : " + n_chickens + " chickens, ready-to-go ... ");
      supply.write (n_chickens);            // supply the chickens
      supply.write (0);                     // wait till they're set down
    }
  }

}
