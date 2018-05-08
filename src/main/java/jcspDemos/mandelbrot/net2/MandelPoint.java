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

package jcspDemos.mandelbrot.net2;



import jcsp.userIO.ComplexDouble;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 */
class MandelPoint {

  protected int maxIterations;

  protected double radiusSquared;

  public MandelPoint (final int maxIterations, final int radius) {
    this.maxIterations = maxIterations;
    radiusSquared = (double) radius*radius;
  }

  public void setMaxIterations (final int maxIterations) {
    this.maxIterations = maxIterations;
  }

  public int compute (final ComplexDouble c) {
    int n = 0;
    ComplexDouble z = (ComplexDouble) c.clone ();
    double zModulusSquared = z.modulusSquared ();
    while ((n < maxIterations) && (zModulusSquared < radiusSquared)) {
      z.mult (z).add (c);
      zModulusSquared = z.modulusSquared ();
      n++;
    }
    return n;
  }

  public int compute (final double a, final double b) {
    int n = 0;
    double x = a;
    double y = b;
    double xSquared = x*x;
    double ySquared = y*y;
    while ((n < maxIterations) && ((xSquared + ySquared) < radiusSquared)) {
      double tmp = (xSquared - ySquared) + a;
      y = ((2.0d*x)*y) + b;
      x = tmp;
      xSquared = x*x;
      ySquared = y*y;
      n++;
    }
    return n;
  }
}
