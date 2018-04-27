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
 * @author Quickstone Technologies Limited
 * @author P.H. Welch
 */
class VT100Display implements CSProcess {

  private final ChannelInputInt in, info;

  public VT100Display (final ChannelInputInt in, final ChannelInputInt info) {
    this.in = in;
    this.info = info;
  }

  private final static void cursorXY (int x, int y) {
    System.out.print ("\u001b[" + y + ";" + x + "H");
  }

  private final static void eraseEOS () {
    System.out.print ("\u001b[0J");
  }

  protected void initialise () {
    cursorXY (1, 1);
    eraseEOS ();
    cursorXY (2, 2);
    System.out.println ("Thinking     Wanna Read     Reading     " +
                        "Wanna Write     Writing     Local View");
    cursorXY (2, 3);
    System.out.println ("========     ==========     =======     " +
                        "===========     =======     ==========");
    cursorXY (43, 16);
    System.out.print ("Time =");
    cursorXY (60, 16);
    System.out.print ("Blackboard =");
  }

  protected void updateBlackboard (char scribbleChar) {
    cursorXY (73, 16);
    System.out.print (scribbleChar);
    System.out.print (scribbleChar);
    System.out.print (scribbleChar);
  }

  public void run () {

    final int[] col = {4, 19, 32, 73, 46, 60, 73};
    final int rowShift = 5;

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
          cursorXY (col[PhilState.THINKING], philId + rowShift);
          System.out.print (":-)");
        break;
        case PhilState.WANNA_READ:
          cursorXY (col[PhilState.THINKING], philId + rowShift);
          System.out.print ("   ");
          cursorXY (col[PhilState.WANNA_READ], philId + rowShift);
          System.out.print (":-(");
        break;
        case PhilState.READING:
          cursorXY (col[PhilState.WANNA_READ], philId + rowShift);
          System.out.print ("   ");
          cursorXY (col[PhilState.READING], philId + rowShift);
          System.out.print (":-)");
        break;
        case PhilState.DONE_READ:
          scribbleInt = info.read ();
          if (scribbleInt != -1) scribbleChar = (char) (scribbleInt + zeroInt);
          cursorXY (col[PhilState.DONE_READ], philId + rowShift);
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          cursorXY (col[PhilState.READING], philId + rowShift);
          System.out.print ("   ");
        break;
        case PhilState.WANNA_WRITE:
          cursorXY (col[PhilState.THINKING], philId + rowShift);
          System.out.print ("   ");
          cursorXY (col[PhilState.WANNA_WRITE], philId + rowShift);
          // System.out.print (":-(");
          System.out.print (philId);
          System.out.print (philId);
          System.out.print (philId);
        break;
        case PhilState.WRITING:
          cursorXY (col[PhilState.WANNA_WRITE], philId + rowShift);
          System.out.print ("   ");
          cursorXY (col[PhilState.WRITING], philId + rowShift);
          // System.out.print (":-)");
          System.out.print (philId);
          System.out.print (philId);
          System.out.print (philId);
        break;
        case PhilState.DONE_WRITE:
          scribbleInt = info.read ();
          scribbleChar = (char) (scribbleInt + zeroInt);
          cursorXY (col[PhilState.DONE_WRITE], philId + rowShift);
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          System.out.print (scribbleChar);
          updateBlackboard (scribbleChar);
          cursorXY (col[PhilState.WRITING], philId + rowShift);
          System.out.print ("   ");
        break;
        case PhilState.TIME:
          cursorXY (50, 16);
          System.out.print (philId);
        break;
      }
    }

  }

}
