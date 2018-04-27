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

package jcspDemos.mandelbrot;



import jcsp.awt.*;
import java.awt.event.*;

/**
 * @author P.H. Welch
 */
class Target {

  private final int width;
  private final int height;

  public Target (final int width, final int height) {
    this.width = width;
    this.height = height;
  }

  int zoomX, zoomY;

  final double initialZoom = 0.1;
  double zoom = initialZoom;

  final double zoomAdjust = 1.2;

  final double maxZoom = 0.75;
  final double minZoom = 0.03;

  boolean shiftZoom = false;

  final double maxShiftZoom = 1.0;

  int dX, dY, dX2, dY2;

  public void reset (MouseEvent mouseEvent) {
    zoom = initialZoom;
    dX = (int) (((double) width)*(zoom/2.0));
    dY = (int) (((double) height)*(zoom/2.0));
    dX2 = (int) (((double) width)*zoom);
    dY2 = (int) (((double) height)*zoom);
    zoomX = mouseEvent.getX ();
    zoomY = mouseEvent.getY ();
    shiftZoom = ((mouseEvent.getModifiers () & InputEvent.SHIFT_MASK) != 0);
  }

  public void move (MouseEvent mouseEvent) {
    zoomX = mouseEvent.getX ();
    zoomY = mouseEvent.getY ();
  }

  public void zoomUp () {
    zoom *= zoomAdjust;
    if (shiftZoom) {
      if (zoom > maxShiftZoom) zoom = maxShiftZoom;
    } else {
      if (zoom > maxZoom) zoom = maxZoom;
    }
    dX = (int) (((double) width)*(zoom/2.0));
    dY = (int) (((double) height)*(zoom/2.0));
    dX2 = (int) (((double) width)*zoom);
    dY2 = (int) (((double) height)*zoom);
  }

  public void zoomDown () {
    zoom /= zoomAdjust;
    if (zoom < minZoom) zoom = minZoom;
    dX = (int) (((double) width)*(zoom/2.0));
    dY = (int) (((double) height)*(zoom/2.0));
    dX2 = (int) (((double) width)*zoom);
    dY2 = (int) (((double) height)*zoom);
  }

  public GraphicsCommand makeGraphicsCommand () {
    if (!shiftZoom) {
      if (zoomX < dX) {
        zoomX = dX;
      } else if (zoomX >= (width - dX)) {
        zoomX = (width - dX) - 1;
      }
      if (zoomY < dY) {
        zoomY = dY;
      } else if (zoomY >= (height - dY)) {
        zoomY = (height - dY) - 1;
      }
    }
    return new GraphicsCommand.DrawRect (zoomX - dX, zoomY - dY, dX2, dY2);
  }

}
