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
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class College {

  public static final String TITLE = "Wot No Chickens";
  public static final String DESCR =
  	"Shows the JCSP solution to the 'Wot, No Chickens?' problem.\n\n" +

  	"The College consists of 5 Philosophers, a Chef and the Canteen. All are " +
    "\"active\" objects. The Canteen ALTs between a service Channel, shared by " +
    "all the Philosophers, and a supply Channel from the Chef.  Upon acceptance " +
    "of a service request, chickens are dispensed through a delivery Channel.\n\n" +

    "Despite the greedy behaviour of Philosopher 0, nobody starves. The Canteen " +
    "guards the service Channel so that Philosophers cannot blunder in when there " +
    "are no chickens, but are held waiting in the service queue.";

  public static void main (String argv[]) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    int n_philosophers = 5;

    final Any2OneChannelInt service = Channel.any2oneInt ();
    final One2OneChannelInt deliver = Channel.one2oneInt ();
    final One2OneChannelInt supply = Channel.one2oneInt ();

    final Phil[] phil = new Phil[n_philosophers];
    for (int i = 0; i < n_philosophers; i++) {
      phil[i] = new Phil (i, service.out (), deliver.in ());
    }

    new Parallel (
      new CSProcess[] {
        new Clock (),
        new Canteen (service.in (), deliver.out (), supply.in ()),
        new Chef (supply.out ()),
        new Parallel (phil)
      }
    ).run ();

  }

}
