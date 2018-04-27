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

package jcspDemos.eratosthenes;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class NumbersFrom implements CSProcess {

  private final int start;
  private final int increment;
  private final ChannelOutputInt out;
  
  public NumbersFrom (final int start,
                      final int increment,
                      final ChannelOutputInt out) {
    this.start = start;
    this.increment = increment;
    this.out = out;
  }
  
  public void run () {
    int n = start;
    while (true) {
      out.write (n);
      n += increment;
    }
  }

}
