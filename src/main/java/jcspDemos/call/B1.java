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
class B1 implements CSProcess, Foo {

  private final ChannelAccept in;

  public B1 (final ChannelAccept in) {
    this.in = in;
  }

  public int calculate (int a, double b, long c) {
    int result = a + (int) b + (int) c;
    System.out.println ("B.calculate: " + a + ", " + b + ", " + c + " ==> " + result);
    return result;
  }

  public void  processQuery (int a, double b, long c) {
    System.out.println ("B1.processQuery: " + a + ", " + b + ", " + c);
  }

  public double closeValve (int a, double b, long c) {
    int result = a + (int) b + (int) c;
    System.out.println ("B1.closeValve: " + a + ", " + b + ", " + c + " ==> " + result);
    return result;
  }

  public void run () {
    in.accept (this);
    in.accept (this);
    in.accept (this);
  }

}
