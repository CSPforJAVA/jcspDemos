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



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class TextDisplay implements CSProcess {

  // attributes

  private final int nPhilosophers;

  private final AltingChannelAccept philChannel;
  private final AltingChannelAccept forkChannel;
  private final AltingChannelInputInt securityChannel;
  private final AltingChannelInputInt clockChannel;

  private String space (int n) {
  	String s = "  ";
  	for (int i = 0; i < n; i++) {
  		s = s + "  ";
  	}
  	return s;
  }

  // constructors

  public TextDisplay (int nPhilosophers,
                      PhilChannel philChannel,
                      ForkChannel forkChannel,
                      AltingChannelInputInt securityChannel,
                      AltingChannelInputInt clockChannel) {
    this.nPhilosophers = nPhilosophers;
    this.philChannel = philChannel;
    this.forkChannel = forkChannel;
    this.securityChannel = securityChannel;
    this.clockChannel = clockChannel;
  }

  private interface inner extends CSProcess, PhilReport, ForkReport {};

  // public methods

  public void run () {

    new inner () {

      public void thinking (int id) {
        System.out.println (space(id) + "Philosopher " + id +
                            " is thinking ...");
      }

      public void hungry (int id) {
        System.out.println (space(id) + "Philosopher " + id +
                            " is hungry ...");
      }

      public void sitting (int id) {
        System.out.println (space(id) + "Philosopher " + id +
                            " is sitting ...");
      }

      public void eating (int id) {
        System.out.println (space(id) + "Philosopher " + id +
                            " is eating ...");
      }

      public void leaving (int id) {
        System.out.println (space(id) + "Philosopher " + id +
                            " is leaving ...");
      }

      public void forkUp (int id, int philId) {
        System.out.println (space(philId) + "Philosopher " + philId +
                            " has picked up fork " + id + " ...");
      }

      public void forkDown (int id, int philId) {
        System.out.println (space(philId) + "Philosopher " + philId +
                            " has put down fork " + id + " ...");
      }

      public void run () {

        final Alternative alt = new Alternative (
          new Guard[] {philChannel, forkChannel, securityChannel, clockChannel}
        );
        final int PHIL = 0;
        final int FORK = 1;
        final int SECURITY = 2;
        final int CLOCK = 3;

        System.out.println ("\nCollege starting with " + nPhilosophers +
                            " philosophers\n");

        while (true) {
          switch (alt.fairSelect ()) {
            case PHIL:
              philChannel.accept (this);
            break;
            case FORK:
              forkChannel.accept (this);
            break;
            case SECURITY:
              final int nSitting = securityChannel.read ();
              System.out.println ("Security: " + nSitting + " sat down ...");
            break;
            case CLOCK:
              final int tick = clockChannel.read ();
              System.out.println ("\n[TICK " + tick + "]\n");
            break;
          }
        }

      }

    }.run ();

  }

}

