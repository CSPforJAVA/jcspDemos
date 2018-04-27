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

package jcspDemos.infect;



import jcsp.lang.*;
import jcsp.plugNplay.*;

/**
 * @author P.H. Welch
 */
class PseudoButton implements CSProcess {

  protected ChannelInput configure;
  protected ChannelOutput event;
  protected ChannelInput feedBack;

  public PseudoButton (ChannelInput configure,
                       ChannelOutput event,
                       ChannelInput feedBack) {
    this.configure = configure;
    this.event = event;
    this.feedBack = feedBack;
  }

  public void run () {
    new Parallel (
      new CSProcess[] {
        new BlackHole (configure),
        new CSProcess() {
          public void run () {
            while (true) {
              feedBack.read ();
              event.write ("");
            }
          }
        }
      }
    ).run ();
  }

}
