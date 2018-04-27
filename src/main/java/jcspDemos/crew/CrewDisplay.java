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

package jcspDemos.crew;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class CrewDisplay implements CSProcess {

  private final ChannelInputInt in, info;

  public CrewDisplay (final ChannelInputInt in, final ChannelInputInt info) {
    this.in = in;
    this.info = info;
  }

  protected void initialise () {
  }

  protected void updateBlackboard (char scribbleChar) {
    System.out.print ("BLACKBOARD = ");
    System.out.print (scribbleChar);
    System.out.print (scribbleChar);
    System.out.println (scribbleChar);
  }

  public void run () {

    final int zeroInt = 48;
    int scribbleInt;
    char scribbleChar = '?';

    initialise ();
    updateBlackboard (scribbleChar);

    while (true) {
      int state = in.read ();
      int philId = info.read ();
      switch (state) {
        case PhilState.THINKING:
          System.out.println ("\tPhil " + philId + " : thinking ...");
        break;
        case PhilState.WANNA_READ:
          System.out.println ("\t\tPhil " + philId + " : wanna read ...");
        break;
        case PhilState.READING:
          System.out.println ("\t\t\tPhil " + philId + " : reading ...");
        break;
        case PhilState.DONE_READ:
          scribbleInt = info.read ();
          if (scribbleInt != -1) scribbleChar = (char) (scribbleInt + zeroInt);
          System.out.print ("\t\t\t\tPhil " + philId + " : read ");
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          System.out.println (scribbleChar);
        break;
        case PhilState.WANNA_WRITE:
          System.out.print ("\t\t\t\t\tPhil " + philId + " : wanna write ... ");
          System.out.print (philId);
          System.out.print (philId);
          System.out.println (philId);
        break;
        case PhilState.WRITING:
          System.out.print ("\t\t\t\t\t\tPhil " + philId + " : writing ... ");
          System.out.print (philId);
          System.out.print (philId);
          System.out.println (philId);
        break;
        case PhilState.DONE_WRITE:
          scribbleInt = info.read ();
          scribbleChar = (char) (scribbleInt + zeroInt);
          System.out.print ("\t\t\t\t\t\t\tPhil " + philId + " : wrote ... ");
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          System.out.println (scribbleChar);
          updateBlackboard (scribbleChar);
        break;
        case PhilState.TIME:
          System.out.println ("TICK " + philId);
        break;
      }
    }

  }

}
