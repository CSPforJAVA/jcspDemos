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

package jcspDemos.altingBarriers;

import jcsp.lang.*;
import java.awt.*;

public class AltingBarrierGadget0 implements CSProcess {

  private final AltingChannelInput click;
  private final AltingBarrier group;
  private final ChannelOutput configure;

  public AltingBarrierGadget0 (
    AltingChannelInput click, AltingBarrier group, ChannelOutput configure
  ) {
    this.click = click;
    this.group = group;
    this.configure = configure;
  }

  public void run () {

    final Alternative clickGroup =
      new Alternative (new Guard[] {click, group});

    final int CLICK = 0, GROUP = 1;

    int n = 0;
    configure.write (String.valueOf (n));

    while (true) {

      configure.write (Color.green);                // pretty
    
      while (!click.pending ()) {
        n++;                                        // work on our own
	configure.write (String.valueOf (n));       // work on our own
      }
      click.read ();                                // must consume the click
      
      configure.write (Color.red);                  // pretty

      boolean group = true;
      while (group) {
        switch (clickGroup.priSelect ()) {
          case CLICK:
	    click.read ();                          // must consume the click
	    group = false;                          // end group working
	  break;
	  case GROUP:
	    n--;                                    // work with the group
	    configure.write (String.valueOf (n));   // work with the group
	  break;
	}
      }
      
    }

  }

}
