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
class CallChef implements CSProcess {

  //The Chef is an active object.  He/she cooks chickens in batches of four --
  //taking around 2 seconds per batch -- and then sends them to the Canteen.
  //The Chef is delayed in the Canteen, waiting for an acknowledge that the
  //batch has been set down OK.
  //
  //This cycle continues indefinitely.

  private final String id;
  private final int batchSize;
  private final int batchTime;
  private final CallCanteen.Supply supply;

  public CallChef (String id, int batchSize, int batchTime, CallCanteen.Supply supply) {
    this.id = id;
    this.batchSize = batchSize;
    this.batchTime = batchTime;
    this.supply = supply;
  }

  public void run () {

    final CSTimer tim = new CSTimer ();

    int nReturned = 0;
    int nSupplied = 0;

    while (true) {
      // cook batchSize chickens
      System.out.println ("   Chef " + id + " : cooking ... " + (batchSize - nReturned) + " chickens");
      tim.sleep (batchTime);                    // this takes batchTime milliseconds to cook
      System.out.println ("   Chef " + id + " : " + batchSize + " chickens, ready-to-go ... ");
      nReturned = supply.freshChickens (id, batchSize);
    // supply the chickens and wait till set down
      nSupplied += (batchSize - nReturned);
      System.out.println ("   Chef " + id + " : " + nReturned + " returned [" + nSupplied + " supplied]");
    }
  }

}
