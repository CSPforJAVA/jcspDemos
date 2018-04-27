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

package jcspDemos.philosophers.callChannel;



import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
class PhilChannel extends Any2OneCallChannel implements PhilReport {

  public void thinking (int id) {
    join ();                                       // ready to make the CALL
    ((PhilReport) server).thinking (id);
    fork ();                                       // call finished
  }

  public void hungry (int id) {
    join ();                                       // ready to make the CALL
    ((PhilReport) server).hungry (id);
    fork ();                                       // call finished
  }

  public void sitting (int id) {
    join ();                                       // ready to make the CALL
    ((PhilReport) server).sitting (id);
    fork ();                                       // call finished
  }

  public void eating (int id) {
    join ();                                       // ready to make the CALL
    ((PhilReport) server).eating (id);
    fork ();                                       // call finished
  }

  public void leaving (int id) {
    join ();                                       // ready to make the CALL
    ((PhilReport) server).leaving (id);
    fork ();                                       // call finished
  }

}
