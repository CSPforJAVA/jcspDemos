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

package jcspDemos.flasher;
//////////////////////////////////////////////////////////////////////



import jcsp.awt.*;

public class FlasherApplet extends ActiveApplet {

  public static final int minPeriod = 300;       // milliseconds
  public static final int maxPeriod = 1000;      // milliseconds
  public static final int defaultPeriod = 500;   // milliseconds

  public void init () {

    final int period =
      getAppletInt ("period", minPeriod, maxPeriod, defaultPeriod);

    setProcess (new FlasherNetwork (period, this));
    // setProcess (new FlasherNetworkSSD (period, this));

  }

}
