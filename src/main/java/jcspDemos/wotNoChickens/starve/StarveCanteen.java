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

package jcspDemos.wotNoChickens.starve;



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
//|           service            |_________|    supply   |______|
//|
//|
//|
//| This time, although Philsopher 0 is just as greedy, no one starves.
//|

import jcsp.lang.*;

/**
 * This Canteen is an active object -- a pure SERVER process for its `service'
 * and `supply' CALL channels.  The service channel is any-1 since, hopefully,
 * there will be many customers.  The supply channel may be 1-1 (one chef) or
 * any-1 (many chefs).
 *
 * @author P.H. Welch
 *
 */

class StarveCanteen {

  // call interfaces and channels

  public static interface Service {
    public int takeChicken(String philId);
  }

  public static interface Supply {
    public int freshChickens(String chefId, int value);
  }

  // fields and constructors

  private final Service service;				// called by the philosophers
  private final Supply supply;					// called by the chefs
  private final int serviceTime;                // how long a philosopher spends in the canteen
  private final int supplyTime;                 // how long the chef spends in the canteen
  private final int maxChickens;                // maximum number of chickens in the canteen
  private final CSTimer tim;					// for delays

  public StarveCanteen (int serviceTime, int supplyTime, int maxChickens) {
    this.service = new Service () {
    	public int takeChicken (String philId) {
    		return doTakeChicken (philId);
    	}
    };
    this.supply = new Supply () {
    	public int freshChickens (String chefId, int value) {
    		return doFreshChickens (chefId, value);
    	}
    };
    this.serviceTime = serviceTime;
    this.supplyTime = supplyTime;
    this.maxChickens = maxChickens;
    this.tim = new CSTimer ();
  }

  public Service getService () {
  	return service;
  }

  public Supply getSupply () {
  	return supply;
  }

  private int nChickens = 0;
  private int nSupplied = 0;

  private synchronized int doTakeChicken (String philId) {
    System.out.println ("   Canteen -> " + philId + " : one chicken ordered ... "
                                         + nChickens + " left");
  	if (nChickens > 0) {
        tim.sleep (serviceTime);         // this takes serviceTime to deliver
        nChickens--;
        nSupplied++;
        System.out.println ("   Canteen -> " + philId + " : one chicken coming down ... "
                                             + nChickens + " left [" + nSupplied + " supplied]");
	    return 1;
  	} else {
  		return 0;
  	}
  }

  private synchronized int doFreshChickens (String chefId, int value) {
    System.out.println ("   Canteen <- " + chefId
                                         + " : ouch ... make room ... this dish is very hot ...");
    tim.sleep (supplyTime);          // this takes supplyTime to put down
    nChickens += value;
    int sendBack = nChickens - maxChickens;
    if (sendBack > 0) {
      nChickens = maxChickens;
      System.out.println ("   Canteen <- " + chefId
                                           + " : full up ... sending back " + sendBack);
    } else {
      sendBack = 0;
    }
    System.out.println ("   Canteen <- " + chefId + " : more chickens ... "
                                         + nChickens + " now available");
    return sendBack;
  }

}
