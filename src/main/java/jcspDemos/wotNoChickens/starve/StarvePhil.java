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



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class StarvePhil implements CSProcess {

  //A Philosopher thinks for a while -- around 3 seconds -- and then goes to the
  //Canteen for food, consuming what he gets straight away.   This cycle continues
  //indefinitely.
  //
  //Except, that is, for Philosopher 0 ...  who refuses to think and just keeps
  //going to the Canteen.
  //
  //For this Canteen, when there's no chicken, the Philosphers wait for a bit and try again,
  //possibly losing their place in the queue.

  private final String id;
  private final StarveCanteen.Service service;
  private final int thinkTime;
  private final int eatTime;
  private final int waitTime;
  private final boolean greedy;

  public StarvePhil (String id, StarveCanteen.Service service,
                   int thinkTime, int eatTime, int waitTime, boolean greedy) {
    this.id = id;
    this.service = service;
    this.thinkTime = thinkTime;
    this.eatTime = eatTime;
    this.waitTime = waitTime;
    this.greedy = greedy;
  }

  public void run () {
    final CSTimer tim = new CSTimer ();
    int nEaten = 0;
    while (true) {
      // everyone, unless greedy, has a little think
      if (! greedy) {
        System.out.println ("   Phil " + id + " : thinking ... ");
        tim.sleep (thinkTime);   // thinking
      }
      // want chicken
      System.out.println ("   Phil " + id + " : gotta eat ... ");
      while (service.takeChicken (id) == 0) {
        System.out.println ("   Phil " + id + " : wot, no chickens ? it's an outrage !");
        tim.sleep (waitTime); // try again later
      }
      nEaten++;
      System.out.println ("   Phil " + id + " : mmm ... that's good [" + nEaten + " so far]");
      tim.sleep (eatTime);       // eating
    }
  }

}
