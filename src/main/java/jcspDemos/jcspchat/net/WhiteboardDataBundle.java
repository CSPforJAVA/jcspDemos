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

package jcspDemos.jcspchat.net;


import java.awt.*;
import java.io.*;

/**
 * @author Quickstone Technologies Limited
 */
public class WhiteboardDataBundle implements Serializable {
  private int tool;
  private String user = "";
  private Rectangle rect = null;
  private boolean filled = false;
  private String text = "";
  private Color lineColor = null;
  private Color fillColor = null;
  private Object[] freehandArray = null;
  private int stroke = 1;
  private Point p1;
  private Point p2;


  //shape constructor
  public WhiteboardDataBundle(String user,int tool, Rectangle rect, boolean filled, Color lineColor, Color fillColor, int stroke) {
    this.tool = tool;
    this.rect = rect;
    this.filled = filled;
    this.lineColor = lineColor;
    this.fillColor = fillColor;
    this.stroke = stroke;
    this.user = user;
  }
  //line constructor
  public WhiteboardDataBundle(String user,int tool, Color lineColor,int stroke, Point p1, Point p2) {
    this.tool = tool;
    this.user = user;
    this.lineColor = lineColor;
    this.stroke = stroke;
    this.p1 = p1;
    this.p2 = p2;
  }
  //text constructor
  public WhiteboardDataBundle(String user,int tool, Rectangle rect, Color lineColor, String text) {
    this.tool = tool;
    this.user = user;
    this.rect = rect;
    this.lineColor = lineColor;
    this.text = text;
  }
  //wipe constructor
  public WhiteboardDataBundle(int tool) {
    this.tool = tool;
  }
  public int getTool() {
    return tool;
  }
  public int getStroke() {
    return stroke;
  }
  public String getUser() {
    return user;
  }
  public Rectangle getRect() {
    return rect;
  }
  public Point getP1() {
    return p1;
  }
  public Point getP2() {
    return p2;
  }
  public Color getLineColor() {
    return lineColor;
  }
  public Color getFillColor() {
    return fillColor;
  }
  public String getText() {
    return text;
  }
  public boolean isFilled() {
    return filled;
  }


}
