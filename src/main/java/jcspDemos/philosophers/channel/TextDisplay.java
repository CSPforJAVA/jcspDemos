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



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class TextDisplay implements CSProcess {

  // attributes

  private final ChannelInput in;
  private final int nPhilosophers;

  // constructors

  public TextDisplay (int nPhilosophers, ChannelInput in) {
    this.nPhilosophers = nPhilosophers;
    this.in = in;
  }

  private String space (int n) {
    String s = "  ";
    for (int i = 0; i < n; i++) {
      s = s + "  ";
    }
    return s;
  }

  // public methods

  public void run () {
    ISReport report;
    int id, state, philId;
    System.out.println ("\nCollege starting with " + nPhilosophers +
                        " philosophers\n");
    while (true) {
      report = (ISReport) in.read ();
      id = report.getId ();
      state = report.getState ();
      if (report instanceof PhilReport) {
        switch (state) {
          case PhilReport.THINKING:
            System.out.println (space(id) + "Philosopher " + id +
                                " is thinking ...");
          break;
          case PhilReport.HUNGRY:
            System.out.println (space(id) + "Philosopher " + id +
                                " is hungry ...");
          break;
          case PhilReport.SITTING:
            System.out.println (space(id) + "Philosopher " + id +
                                " is sitting ...");
          break;
          case PhilReport.EATING:
            System.out.println (space(id) + "Philosopher " + id +
                                " is eating ...");
          break;
          case PhilReport.LEAVING:
            System.out.println (space(id) + "Philosopher " + id +
                                " has finished eating  ...");
          break;
        }
      } else if (report instanceof ForkReport) {
        philId = ((ForkReport) report).getPhilId ();
        switch (state) {
          case ForkReport.DOWN:
            System.out.println (space(philId) + "Philosopher " + philId +
                                " has put down fork " + id + " ...");
          break;
          case ForkReport.UP:
            System.out.println (space(philId) + "Philosopher " + philId +
                                " has picked up fork " + id + " ...");
          break;
        }
      } else if (report instanceof SecurityReport) {
        System.out.println ("Security: " + id + " sat down ...");
      } else if (report instanceof ClockReport) {
        System.out.println ("\n[TICK " + id + "]\n");
      } else {
        System.out.println ("\nBad report !!!");
      }
    }
  }

}

