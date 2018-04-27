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
import jcsp.lang.*;


/**
 * @author Quickstone Technologies Limited
 */
public class WhiteboardReceiveProcess implements CSProcess {
  private DrawingPanel dp;
  private ChannelInput in;
  private Graphics2D bg;
  public static final BasicStroke[] strokes = new BasicStroke[] {new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND),new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND),new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND),new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND),new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)};
  private String user;
  private ChannelOutputInt out;
  private WhiteboardDataBundle bun;

  public WhiteboardReceiveProcess(DrawingPanel panel, ChannelInput input, ChannelOutputInt out, String user) {
    dp = panel;
    this.user = user;
    this.out = out;
    in = input;
    bg = dp.getBufferedImage().createGraphics();
    bg.setColor(Color.white);

    bg.fillRect(0,0,dp.getBufferedImage().getWidth(),dp.getBufferedImage().getHeight());
    bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    bg.setFont(DrawingPanel.TEXTFONT);
  }
  public void run() {
    while (true) {
      bun = (WhiteboardDataBundle)in.read();
      Runnable runner = new Runnable() {
        public void run() {
        }
      };
      //System.out.println("bun " + x + " = " + bun);
      switch (bun.getTool()) {

        case DrawingPanel.WIPE:
          runner = new Runnable() {
            public void run() {
              bg.setColor(Color.white);
              bg.fillRect(0,0,DrawingPanel.whiteboardWidth,DrawingPanel.whiteboardHeight);
              dp.repaint();
            }
          };
          break;
        case DrawingPanel.FREEHAND:
          //System.out.println("drawing freehand");
          if (!(bun.getUser().equals(user))) {
            runner = new Runnable() {
              private Color lc = bun.getLineColor();
              private int s = bun.getStroke()-1;
              private Point p1 = bun.getP1();
              private Point p2 = bun.getP2();
              public void run () {
                bg.setColor(lc);
                bg.setStroke(strokes[s]);
                bg.drawLine(p1.x,p1.y,p2.x,p2.y);
                dp.paintImmediately(Math.min(p1.x,p2.x)-10,Math.min(p1.y,p2.y)-10,Math.abs(p1.x - p2.x)+20,Math.abs(p1.y - p2.y)+20);
              }
            };
          }
          break;

        case DrawingPanel.LINE:
          runner = new Runnable() {
            private Color lc = bun.getLineColor();
            private int s = bun.getStroke()-1;
            private Point p1 = bun.getP1();
            private Point p2 = bun.getP2();
            public void run () {
              bg.setColor(lc);
              bg.setStroke(strokes[s]);
              bg.drawLine(p1.x,p1.y,p2.x,p2.y);
              dp.paintImmediately(Math.min(p1.x,p2.x)-10,Math.min(p1.y,p2.y)-10,Math.abs(p1.x - p2.x)+20,Math.abs(p1.y - p2.y)+20);
            }
          };
          break;
        case DrawingPanel.OVAL:
          runner = new Runnable() {
            private boolean isFilled = bun.isFilled();
            private Color fc = bun.getFillColor();
            private Rectangle rect = bun.getRect();
            private Color lc = bun.getLineColor();
            private int s = bun.getStroke()-1;
            public void run () {
              if (isFilled) {
                bg.setColor(fc);
                bg.fillOval(rect.x,rect.y,rect.width,rect.height);
              }
              bg.setColor(lc);
              bg.setStroke(strokes[s]);
              bg.drawOval(rect.x,rect.y,rect.width,rect.height);
              dp.paintImmediately(rect.x - 10, rect.y - 10, rect.width+20, rect.height+20);
            }
          };


          break;
        case DrawingPanel.RECTANGLE:
          runner = new Runnable() {
            private boolean isFilled = bun.isFilled();
            private Color fc = bun.getFillColor();
            private Rectangle rect = bun.getRect();
            private Color lc = bun.getLineColor();
            private int s = bun.getStroke()-1;
            public void run () {
              if (isFilled) {
                bg.setColor(fc);
                bg.fillRect(rect.x,rect.y,rect.width,rect.height);
              }
              bg.setColor(lc);
              bg.setStroke(strokes[s]);
              bg.drawRect(rect.x,rect.y,rect.width,rect.height);
              dp.paintImmediately(rect.x - 10, rect.y - 10, rect.width+20, rect.height+20);
            }
          };


          break;
        case DrawingPanel.ROUND_RECTANGLE:

          runner = new Runnable() {
            private boolean isFilled = bun.isFilled();
            private Color fc = bun.getFillColor();
            private Rectangle rect = bun.getRect();
            private Color lc = bun.getLineColor();
            private int s = bun.getStroke()-1;
            public void run () {
              int edgeRadius = (Math.min(rect.width,rect.height))/10;
              if (isFilled) {
                bg.setColor(fc);
                bg.fillRoundRect(rect.x,rect.y,rect.width,rect.height,edgeRadius,edgeRadius);
              }
              bg.setColor(lc);
              bg.setStroke(strokes[s]);
              bg.drawRoundRect(rect.x,rect.y,rect.width,rect.height,edgeRadius,edgeRadius);
              dp.paintImmediately(rect.x - 10, rect.y - 10, rect.width+20, rect.height+20);
            }
          };
          break;

        case DrawingPanel.TEXT:

          runner = new Runnable() {
            private Color lc = bun.getLineColor();
            private String text = bun.getText();
            private Rectangle rect = bun.getRect();

            public void run() {
              bg.setColor(lc);
              bg.drawString(text,rect.x,rect.y);
              dp.repaint();
            }
          };
      }
      SwingUtilities.invokeLater(runner);
      out.write(0);
    }
  }
}
