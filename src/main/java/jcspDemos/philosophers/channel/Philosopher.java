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

package jcspDemos.philosophers.channel;



import java.util.Random;
import jcsp.lang.*;
import jcsp.plugNplay.*;

/**
 * @author P.H. Welch
 */
class Philosopher implements CSProcess {

  // protected attributes

  protected final static int seconds = 1000;

  protected final static int maxThink = 10*seconds;
  protected final static int maxEat = 15*seconds;

  protected final static String[] space =
    {"  ", "    ", "      ", "        ", "          "};

  protected final int id;
  protected final ChannelOutput left, right, down, up;
  protected final ChannelOutput report;

  protected final Random random;

  // constructors

  public Philosopher (int id, ChannelOutput left, ChannelOutput right,
                      ChannelOutput down, ChannelOutput up,
                      ChannelOutput report) {
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

    final Integer Id = new Integer (id);
    final CSTimer tim = new CSTimer ();

    final PhilReport thinking = new PhilReport (id, PhilReport.THINKING);
    final PhilReport hungry = new PhilReport (id, PhilReport.HUNGRY);
    final PhilReport sitting = new PhilReport (id, PhilReport.SITTING);
    final PhilReport eating= new PhilReport (id, PhilReport.EATING);
    final PhilReport leaving = new PhilReport (id, PhilReport.LEAVING);

    final ProcessWrite signalLeft = new ProcessWrite (left);
    signalLeft.value = Id;

    final ProcessWrite signalRight = new ProcessWrite (right);
    signalRight.value = Id;

    final CSProcess signalForks = new Parallel (new CSProcess[] {signalLeft, signalRight});
    /*final CSProcess signalForks = new Sequence (
    	new CSProcess[] {
    		signalLeft,
    		new CSProcess () { public void run () { tim.sleep (seconds); } },
    		signalRight
    	}
    );*/

    while (true) {
      report.write (thinking);
      tim.sleep (range (maxThink));
    // thinking
      report.write (hungry);
      down.write (Id);                 // get past the security guard
      report.write (sitting);
      signalForks.run ();              // pick up my forks (in parallel)
      report.write (eating);
      tim.sleep (range (maxEat));      // eating
      report.write (leaving);
      signalForks.run ();              // put down my forks (in parallel)
      up.write (Id);                   // get up from the table and go past the security guard
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
