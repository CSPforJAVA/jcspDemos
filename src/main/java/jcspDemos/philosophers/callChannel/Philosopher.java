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

package jcspDemos.philosophers.callChannel;



import java.util.Random;
import jcsp.lang.*;
import jcsp.plugNplay.ints.*;

/**
 * @author P.H. Welch
 */
class Philosopher implements CSProcess {

  // protected attributes

  protected final static int seconds = 1000;

  protected final static int maxThink = 10*seconds;
  protected final static int maxEat = 15*seconds;

  protected final int id;
  protected final ChannelOutputInt left, right, down, up;
  protected final PhilReport report;

  protected final Random random;

  // constructors

  public Philosopher (int id, ChannelOutputInt left, ChannelOutputInt right,
                      ChannelOutputInt down, ChannelOutputInt up,
                      PhilReport report) {
    this.id = id;
    this.left = left;
    this.right = right;
    this.down = down;
    this.up = up;
    this.report = report;
    this.random = new Random (id + 1);
  }

  // public methods

  public void run () {

    final CSTimer tim = new CSTimer ();

    final ProcessWriteInt signalLeft = new ProcessWriteInt (left);
    signalLeft.value = id;

    final ProcessWriteInt signalRight = new ProcessWriteInt (right);
    signalRight.value = id;

    final CSProcess signalForks = new Parallel (new CSProcess[] {signalLeft, signalRight});
    /*final CSProcess signalForks = new Sequence (
    	new CSProcess[] {
    		signalLeft,
    		new CSProcess () { public void run () { tim.sleep (seconds); } },
    		signalRight
    	}
    );*/

    while (true) {
      report.thinking (id);
      tim.sleep (range (maxThink));
    // thinking
      report.hungry (id);
      down.write (id);                 // get past the security guard
      report.sitting (id);
      signalForks.run ();              // pick up my forks (in parallel)
      report.eating (id);
      tim.sleep (range (maxEat));      // eating
      report.leaving (id);
      signalForks.run ();              // put down my forks (in parallel)
      up.write (id);                   // get up from the table and go past the security guard
    }
  }

  // protected methods

  protected int range (int n) {
    // returns random int in the range 0 .. (n - 1)  [This is not needed in JDK 1.2.x]
    int i = random.nextInt ();
    if (i < 0) {
      if (i == Integer.MIN_VALUE) {
        i = 42;
      } else {
        i = -i;
      }
    }
    return i % n;
  }

}
