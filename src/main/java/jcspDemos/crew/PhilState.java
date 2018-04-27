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

package jcspDemos.crew;



/**
 * @author P.H. Welch
 */
interface PhilState {

  public static final int THINKING = 0;

  public static final int WANNA_READ = 1;

  public static final int READING = 2;

  public static final int DONE_READ = 3;

  public static final int WANNA_WRITE = 4;

  public static final int WRITING = 5;

  public static final int DONE_WRITE = 6;

  public static final int TIME = 7;

}
