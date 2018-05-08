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

package jcspDemos.raytrace.net2;


import jcsp.lang.CSProcess;
import jcsp.lang.ChannelInput;
import jcsp.lang.ChannelOutput;

/**
 * @author Quickstone Technologies Limited
 */
class FrameDeMux implements CSProcess {
	
	private final ChannelInput in;
	private final ChannelOutput[] out;
	
	public FrameDeMux (final ChannelInput in, final ChannelOutput[] out) {
		this.in = in;
		this.out = out;
	}
	
	public void run () {
		System.out.println ("Frame DeMux: started");
		while (true) {
			//System.out.println ("Frame DeMux: waiting for data");
			ResultPacket rp = (ResultPacket)in.read ();
			//System.out.println ("Frame DeMux: frame " + rp.frame + " data received");
			out[rp.frame % out.length].write (rp);
		}
	}
	
}
