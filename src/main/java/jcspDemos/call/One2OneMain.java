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
import jcsp.userIO.*;

/**
 * @author P.H. Welch
 */
public class One2OneMain {

  public static final String TITLE = "One-One Call Channel";
  public static final String DESCR =
  	"Shows an One-One call channel in use. The channel has three methods: " +
  	"'processQuery', 'calculate' and 'closeValve'.";

  public static void main (String[] args) {

  	Ask.app (TITLE, DESCR);
  	Ask.show ();
  	Ask.blank ();

    One2OneFooChannel c = new One2OneFooChannel ();

    new Parallel (
      new CSProcess[] {
        new A (c),
        new B2 (c)
      }
    ).run ();

    System.exit (0);

  }

}
