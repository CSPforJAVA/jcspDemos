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
class Any2OneFooChannel extends Any2OneCallChannel implements FooChannel {

  public int calculate (int a, double b, long c) {
    join ();                                    // ready to make the CALL
    int t = ((Foo) server).calculate (a, b, c);
    fork ();                                    // call finished
    return t;
  }

  public void processQuery (int a, double b, long c) {
    join ();                                    // ready to make the CALL
    ((Foo) server).processQuery (a, b, c);
    fork ();                                    // call finished
  }

  public double closeValve (int a, double b, long c) {
    join ();                                    // ready to make the CALL
    double t = ((Foo) server).closeValve (a, b, c);
    fork ();                                    // call finished
    return t;
  }

}
