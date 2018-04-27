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
import jcsp.awt.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */
class AWTDisplay implements CSProcess {

  private final ChannelInputInt in, info;
  
  private Canvas canvas;
  private Graphics graphics;
  private int fontHeight = 1, fontWidth = 1;
  
  private class FixedSizeCanvas extends Canvas {
  	public Dimension getPreferredSize () {
  		return new Dimension (90 * fontWidth, 18 * fontHeight);
  	}
  }

  public AWTDisplay (final ChannelInputInt in, final ChannelInputInt info) {
    this.in = in;
    this.info = info;
  }

  private void print (int x, int y, String text) {
  	graphics.clearRect (x * fontWidth, (y - 1) * fontHeight, text.length () * fontWidth, fontHeight);
  	graphics.drawString (text, x * fontWidth, y * fontHeight);
  }

  private void eraseEOS () {
  	Dimension dim = canvas.getSize ();
  	graphics.clearRect (0, 0, (int)dim.width, (int)dim.height);
  }

  protected void initialise () {
    eraseEOS ();
    print (2, 2, "Thinking     Wanna Read     Reading     " +
                        "Wanna Write     Writing     Local View");
    print (2, 3, "========     ==========     =======     " +
                        "===========     =======     ==========");
    print (43, 16, "Time =");
    print (60, 16, "Blackboard =");
  }

  protected void updateBlackboard (char scribbleChar) {
    print (73, 16, "" + scribbleChar + scribbleChar + scribbleChar);
  }

  public void run () {
  	
  	Frame f = new ActiveFrame ("Scribbling Philosophers");
  	canvas = new FixedSizeCanvas ();
  	f.add (canvas);
  	f.setVisible (true);
  	graphics = canvas.getGraphics ();
  	graphics.setFont (new Font ("monospaced", Font.PLAIN, 16));
  	FontMetrics fm = graphics.getFontMetrics ();
  	fontHeight = fm.getHeight ();
  	fontWidth = fm.charWidth ('x');
  	f.pack ();
  	f.addWindowListener (new WindowAdapter () { public void windowClosing (WindowEvent e) { System.exit(0); } });

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
          print (col[PhilState.THINKING], philId + rowShift, ":-)");
        break;
        case PhilState.WANNA_READ:
          print (col[PhilState.THINKING], philId + rowShift, "   ");
          print (col[PhilState.WANNA_READ], philId + rowShift, ":-(");
        break;
        case PhilState.READING:
          print (col[PhilState.WANNA_READ], philId + rowShift, "   ");
          print (col[PhilState.READING], philId + rowShift, ":-)");
        break;
        case PhilState.DONE_READ:
          scribbleInt = info.read ();
          if (scribbleInt != -1) scribbleChar = (char) (scribbleInt + zeroInt);
          print (col[PhilState.DONE_READ], philId + rowShift, "" + scribbleChar + scribbleChar + scribbleChar);
          print (col[PhilState.READING], philId + rowShift, "   ");
        break;
        case PhilState.WANNA_WRITE:
          print (col[PhilState.THINKING], philId + rowShift, "   ");
          print (col[PhilState.WANNA_WRITE], philId + rowShift, "" + philId + philId + philId);
        break;
        case PhilState.WRITING:
          print (col[PhilState.WANNA_WRITE], philId + rowShift, "   ");
          print (col[PhilState.WRITING], philId + rowShift, "" + philId + philId + philId);
        break;
        case PhilState.DONE_WRITE:
          scribbleInt = info.read ();
          scribbleChar = (char) (scribbleInt + zeroInt);
          print (col[PhilState.DONE_WRITE], philId + rowShift, "" + scribbleChar + scribbleChar + scribbleChar);
          updateBlackboard (scribbleChar);
          print (col[PhilState.WRITING], philId + rowShift, "   ");
        break;
        case PhilState.TIME:
          print (50, 16, "" + philId);
        break;
      }
    }

  }

}
