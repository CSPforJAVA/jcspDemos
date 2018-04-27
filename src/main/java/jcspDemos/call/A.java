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

package jcspDemos.call;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class A implements CSProcess {

  private final Foo out;

  public A (final Foo out) {
    this.out = out;
  }

  public void run () {
    System.out.println ("A: out.calculate (3, 4.0, 5)");
    int t = out.calculate (3, 4.0, 5);
    System.out.println ("A: ==> " + t);
    System.out.println ("A: out.closeValve (6, 7.0, 8)");
    double s = out.closeValve (6, 7.0, 8);
    System.out.println ("A: ==> " + s);
    System.out.println ("A: out.processQuery (0, 1.0, 2)");
    out.processQuery (0, 1.0, 2);
    System.out.println ("A: ==> returned OK");
  }

}
