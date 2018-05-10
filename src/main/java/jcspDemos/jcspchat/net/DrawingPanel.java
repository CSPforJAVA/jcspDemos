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



import javax.swing.*;
import java.awt.*;
import java.awt.image.*;


/**
 * @author Quickstone Technologies Limited
 */
public class DrawingPanel extends JPanel {
  public static final int whiteboardWidth = 1024;
  public static final int whiteboardHeight = 768;
  final static int FREEHAND = 1;
  final static int LINE = 2;
  final static int RECTANGLE = 3;
  final static int ROUND_RECTANGLE = 4;
  final static int OVAL = 5;
  final static int TEXT = 6;
  final static int WIPE = 999;
  final static Font TEXTFONT = new Font("SansSerif", Font.PLAIN, 16);

  int oldX,oldY,newX,newY;
  int tool = FREEHAND;
  boolean graphicsObjectInitialized = false;
  Rectangle oldRect = new Rectangle(0,0,whiteboardWidth,whiteboardHeight);
  boolean textActive = false;
  boolean filled = true;

  String textInput = "";


  BufferedImage buffer = new BufferedImage(whiteboardWidth,whiteboardHeight,BufferedImage.TYPE_3BYTE_BGR);
  Graphics2D bg = buffer.createGraphics();

  int strokeType = 3;
  Point startDrag = null;

  public DrawingPanel () {    
    bg.setColor(Color.white);
    bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    bg.fillRect(0,0,buffer.getWidth(),buffer.getHeight());
    bg.setFont(TEXTFONT);
  }

  public boolean isFocusable() {   // used to override: isFocusTraversable()
    return true;
  }

  public Rectangle clearOldShape (Rectangle rect, Rectangle newRect) {
    this.getGraphics().drawImage(buffer.getSubimage(rect.x,rect.y,rect.width,rect.height),rect.x,rect.y,this);
    return (Rectangle)newRect.createIntersection(new Rectangle(0,0,whiteboardWidth,whiteboardHeight));
  }

  public int getCurrentTool() {
    return tool;
  }

  public Point getStartDrag() {
    return startDrag;
  }
  public boolean isFilled() {
    return filled;
  }
  public String getTextInput() {
    return textInput;
  }
  public void finishTextInput() {
    textActive = false;
    textInput= "";
  }
  public Point getOldPoint() {
    return new Point(oldX,oldY);
  }
  public int getStroke() {
    return strokeType;
  }
  public boolean isTextActive() {
    return textActive;
  }
  public BufferedImage getBufferedImage() {
    return buffer;
  }


  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(buffer,0,0,this);

  }
}
