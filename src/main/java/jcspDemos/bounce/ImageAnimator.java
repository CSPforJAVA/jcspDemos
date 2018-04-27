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

package jcspDemos.bounce;



import java.awt.*;
import jcsp.lang.*;
import jcsp.awt.*;

/**
 * @author P.H. Welch
 */
public class ImageAnimator implements CSProcess {

  private final ChannelInputInt in;
  private final ChannelOutput toGraphics;
  private final ChannelInput fromGraphics;
  private final Display display;
  private final Image[] images;
  private int dx;
  private int dy;
  
  /**
   * Construct a new ImageAnimator that will cycle.
   *
   * @param in the channel used to direct the animation
   * @param toGraphics the channel to the graphics component
   * @param fromGraphics the channel from the graphics component
   * @param display the display-channel to the graphics component
   * @param images the array of images to be displayed on the animation
   * @param dx the x displacement between each frame
   * @param dy the y displacement between each frame
   */
  public ImageAnimator (ChannelInputInt in, ChannelOutput toGraphics, ChannelInput fromGraphics,
                        Display display, Image[] images, int dx, int dy) {
    this.in = in;
    this.toGraphics = toGraphics;
    this.fromGraphics = fromGraphics;
    this.display = display;
    this.images = images;
    this.dx = dx;
    this.dy = dy;
  }

  private Dimension dimension;

  private int lastX = 0;
  private int lastY = 0;

  private final GraphicsCommand[] gCommand = {null, null, null};

  public void run () {

    final Thread me = Thread.currentThread ();
    System.out.println ("ImageAnimator " + " priority = " + me.getPriority ());
    me.setPriority (Thread.MIN_PRIORITY);
    System.out.println ("ImageAnimator " + " priority = " + me.getPriority ());

    toGraphics.write (GraphicsProtocol.GET_DIMENSION);
    dimension = (Dimension) fromGraphics.read ();
    toGraphics.write (GraphicsProtocol.GET_BACKGROUND);
    gCommand[0] = new GraphicsCommand.SetColor ((Color) fromGraphics.read ());
    gCommand[1] = new GraphicsCommand.FillRect (0, 0, dimension.width, dimension.height);
    final int nImages = images.length;
    int frame = 0;
    displayFirstImage (images[frame]);
    while (true) {
      final int delta = in.read ();
      frame = (frame + delta + nImages) % nImages;
      displayNextImage (images[frame]);
    }
  }
  
  /**
   * Called to update the graphics display
   *
   * @param image the image to draw
   */
  public void displayFirstImage (final Image image) {
    final int imageWidth = image.getWidth (null);
    final int imageHeight = image.getHeight (null);
    lastX = (dimension.width - imageWidth)/2;
    lastY = (dimension.height - imageHeight)/2;
    gCommand[2] = new GraphicsCommand.DrawImage (image, lastX, lastY);
    display.set (gCommand);
  }
  
  /**
   * Called to update the graphics display
   *
   * @param image the image to draw
   */
  public void displayNextImage (final Image image) {
    final int imageWidth = image.getWidth (null);
    final int imageHeight = image.getHeight (null);
    lastX += dx;
    if ((lastX < 0) | ((lastX + imageWidth) >= dimension.width)) {
      dx = -dx;
      lastX += dx;
    }
    lastY += dy;
    if ((lastY < 0) | ((lastY + imageHeight) >= dimension.height)) {
      dy = -dy;
      lastY += dy;
    }
    gCommand[2] = new GraphicsCommand.DrawImage (image, lastX, lastY);
    display.set (gCommand);
  }

}
