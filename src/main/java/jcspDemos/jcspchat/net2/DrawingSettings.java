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

package jcspDemos.jcspchat.net2;




import java.awt.*;

/**
 * @author Quickstone Technologies Limited
 */
public class DrawingSettings {
  private Color fillColor;
  private Color lineColor;
  private int strokeSize;
  private boolean filled;
  private int tool;
  private WhiteboardDataBundle currentDrawOp;
  private boolean isDrawing;
  private boolean textActive;

  public DrawingSettings(Color lc, Color fc, boolean f, int s, int t) {
    fillColor = fc;
    lineColor = lc;
    strokeSize = s;
    filled = f;
    tool = t;
  }
  public Color getLineColor() {
    return lineColor;
  }
  public Color getFillColor() {
    return fillColor;
  }
  public boolean isFilled() {
    return filled;
  }
  public int getStroke() {
    return strokeSize;
  }
  public int getTool() {
    return tool;
  }

  public void setLineColor(Color c) {
    lineColor = c;
  }
  public void setFillColor(Color c) {
    fillColor = c;
  }
  public void setFilled(boolean b) {
    filled = b;
  }
  public void setStrokeSize(int i) {
    strokeSize = i;
  }
  public void setTool(int i) {
    tool = i;
    if (i != DrawingPanel.TEXT) {
      this.setTextActive(false);
    }
  }
  public boolean isDrawing() {
    return isDrawing;
  }
  public boolean textActive() {
    return textActive;
  }
  public void setTextActive(boolean b) {
    textActive = b;
  }
}
