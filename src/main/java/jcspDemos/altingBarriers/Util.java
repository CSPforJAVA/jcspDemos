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

import jcsp.lang.Any2OneChannel;
import jcsp.lang.ChannelInput;
import jcsp.lang.ChannelOutput;
import jcsp.lang.One2OneChannel;

public class Util {

	static ChannelInput[][] get2DInputArray(One2OneChannel[][] channels) {
		ChannelInput[][] ret = new ChannelInput[channels.length][];
		
		for (int i = 0;i < channels.length;i++) {
			ret[i] = new ChannelInput[channels[i].length];
			for (int j = 0;j < channels[i].length;j++) {
				ret[i][j] = channels[i][j].in(); 
			}
		}
		
		return ret;
	}
	
	static ChannelOutput[][] get2DOutputArray(One2OneChannel[][] channels) {
		ChannelOutput[][] ret = new ChannelOutput[channels.length][];
		
		for (int i = 0;i < channels.length;i++) {
			ret[i] = new ChannelOutput[channels[i].length];
			for (int j = 0;j < channels[i].length;j++) {
				ret[i][j] = channels[i][j].out(); 
			}
		}
		
		return ret;
	}
	
	static ChannelOutput[][] get2DOutputArray(Any2OneChannel[][] channels) {
		ChannelOutput[][] ret = new ChannelOutput[channels.length][];
		
		for (int i = 0;i < channels.length;i++) {
			ret[i] = new ChannelOutput[channels[i].length];
			for (int j = 0;j < channels[i].length;j++) {
				ret[i][j] = channels[i][j].out(); 
			}
		}
		
		return ret;
	}
}
