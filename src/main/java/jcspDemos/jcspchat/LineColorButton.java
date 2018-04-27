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

package jcspDemos.jcspchat;



import javax.swing.*;
import java.awt.*;

/**
 * @author Quickstone Technologies Limited
 */
public class LineColorButton extends JButton {
  private Dimension d;
  private DrawingSettings ds;

  public LineColorButton(DrawingSettings ds) {
    super();
    this.ds =ds;
    //this.setContentAreaFilled(false);
    //this.setHorizontalTextPosition(AbstractButton.LEFT);
    //this.setVerticalTextPosition(AbstractButton.TOP);
    d = new Dimension (26,18);
//    System.out.println("dimension = " + d);
    this.setSize(d);
    this.setPreferredSize(d);
    this.setMinimumSize(d);
    this.setMaximumSize(d);
    this.setBorder(BorderFactory.createEmptyBorder(4,4,4,8));
    this.setBorderPainted(true);
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(ds.getLineColor());
    g.fillRect(3,3,20,12);
    g.setColor(Color.black);
    g.drawRect(3,3,20,12);
  }
}
