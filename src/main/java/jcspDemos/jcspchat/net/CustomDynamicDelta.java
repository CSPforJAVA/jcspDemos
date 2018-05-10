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

package jcspDemos.jcspchat.net;



import java.util.*;
import jcsp.lang.*;
import jcsp.plugNplay.*;

/**
 * @author Quickstone Technologies Limited
 */
public final class CustomDynamicDelta implements CSProcess {

  private AltingChannelInput in;
  private AltingChannelInput config;

  private Hashtable hash;
  private Parallel par;

  public CustomDynamicDelta (AltingChannelInput in, AltingChannelInput config) {
    this.in  = in;
    par = new Parallel ();
    hash = new Hashtable ();
    this.config = config;
  }

  public void run () {
    AltingChannelInput[] chans = {config, in};
    Alternative alt = new Alternative (chans);
    while (true) {
      switch (alt.priSelect ()) {
        case 0:
          System.out.println("dd: reading config chan");
          System.out.println("dd: hashtable looks like this:");
          System.out.println(hash);
          Object object = config.read ();

          if (object instanceof ConnectionBundle) {
            ConnectionBundle cb = (ConnectionBundle)object;
            if (hash.containsKey (cb.getUser())) {
              System.out.println("dd: removing chan");
              removeOutputChannel (cb);
            }
            else {
              addOutputChannel (cb);
            }
          }
        break;
        case 1:
          Object message = in.read ();
          Enumeration hashChans = hash.elements ();
          while (hashChans.hasMoreElements ()) {
            ((ProcessWrite) hashChans.nextElement ()).value = message;
          }
          par.run ();
        break;
      }
    }
  }

  private void addOutputChannel (ConnectionBundle cb) {
    ProcessWrite p = new ProcessWrite (cb.getReturnChan());
    par.addProcess (p);
    hash.put (cb.getUser(), p);
  }

  private void removeOutputChannel (ConnectionBundle cb) {
    System.out.println("removing outputchan " + cb.getUser() );
    ProcessWrite p = (ProcessWrite) hash.get (cb.getUser());
    par.removeProcess (p);
    hash.remove (cb.getUser());
    System.out.println("removed");
  }
}
