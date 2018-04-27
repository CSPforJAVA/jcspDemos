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
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class CallCollege implements CSProcess {

  public static final String TITLE = "Wot No Chickens [call channels]";
  public static final String DESCR =
  	"Shows the JCSP solution to the 'Wot, No Chickens?' problem using call channels.\n\n" +

  	"The College consists of 5 Philosophers, three Chef and the Canteen. All are " +
    "\"active\" objects. The Canteen ALTs between a service Channel, shared by " +
    "all the Philosophers, and a supply Channel from the Chef.  Upon acceptance " +
    "of a service request, chickens are dispensed through a delivery Channel.\n\n" +

    "Despite the greedy behaviour of Philosopher 0, nobody starves. The Canteen " +
    "guards the service Channel so that Philosophers cannot blunder in when there " +
    "are no chickens, but are held waiting in the service queue.";

  public void run () {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    final String[] philId = {"Bill", "Hilary", "Gennifer", "Paula", "Monica"};
    // final int nPhilosophers = 5;

    final int thinkTime = 3000;
    final int eatTime = 100;

    final int serviceTime = 0;
    final int supplyTime = 3000;
    final int maxChickens = 50;

    final CallCanteen.Any2OneServiceChannel service = new CallCanteen.Any2OneServiceChannel ();
    // final CallCanteen.One2OneSupplyChannel supply = new CallCanteen.One2OneSupplyChannel ();
    final CallCanteen.Any2OneSupplyChannel supply = new CallCanteen.Any2OneSupplyChannel ();

    // final CallCanteen canteen = new CallCanteen (serviceTime, supplyTime);

    final CallPhil[] phils = new CallPhil[philId.length];
    for (int i = 0; i < phils.length; i++) {
      // String philId = new Integer (i).toString ();
      phils[i] = new CallPhil (philId[i], service, thinkTime, eatTime, i == 0);
      // phils[i] = new CallPhil (i, canteen.service, thinkTime, eatTime);
    }

    new Parallel (
      new CSProcess[] {
        new CallClock (),
        new CallCanteen (service, supply, serviceTime, supplyTime, maxChickens),
        // canteen,
        new Parallel (phils),
        new CallChef ("Pierre", 4, 2000, supply),       // chefId, batchSize, batchTime
        new CallChef ("Henri", 10, 20000, supply),      // chefId, batchSize, batchTime
        new CallChef ("Sid", 100, 150000, supply)       // chefId, batchSize, batchTime
        // new CallChef (canteen.supply),
      }
    ).run ();

  }

  public static void main (String argv[]) {
    new CallCollege ().run ();
  }

}
