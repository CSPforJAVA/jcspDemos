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

package jcspDemos.mandelbrot.net;



import jcsp.lang.*;
import jcsp.awt.*;
import jcsp.net.*;
import jcsp.net.cns.*;
import jcsp.net.tcpip.*;

import java.net.*;


/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 */
public class MandelbrotMain {

  public static final String TITLE = "Mandelbrot Set (distributed)";
  public static final String DESCR =
  	"Demonstates a distributed farmer/worker/harvester parallelisation. The farmer and harvestor processes " +
  	"will run on this JVM. Workers can run on this machine or elsewhere to generate the actual image.";

  private static final int DEFAULT_WIDTH = 640, DEFAULT_HEIGHT = 480;

  public static void main (String[] args) throws Exception {

	// Get arguments
	String cnsServer = null;
	int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	for (int i = 0; i < args.length; i++) {
		if (args[i].equals ("-width")) {
			width = Integer.parseInt (args[++i]);
		} else if (args[i].equals ("-height")) {
			height = Integer.parseInt (args[++i]);
		} else {
			cnsServer = args[i];
		}
	}
	if (cnsServer == null) {
        NodeKey key =
          Node.getInstance().init(
            new TCPIPAddressID(
              InetAddress.getLocalHost().getHostAddress(),
              TCPIPCNSServer.DEFAULT_CNS_PORT,
              true));
		CNS.install(key);
        NodeAddressID cnsAddress = Node.getInstance().getNodeID().getAddresses()[0];
        CNSService.install(key, cnsAddress);
	} else {
		Node.getInstance ().init (new TCPIPNodeFactory (cnsServer));
	}

	final ActiveClosingFrame activeClosingFrame = new ActiveClosingFrame ("Distributed Mandelbrot");
    final ActiveFrame activeFrame = activeClosingFrame.getActiveFrame ();
    activeFrame.setSize (width, height);

    final MandelNetwork mandelbrot = new MandelNetwork (activeFrame);

    activeFrame.pack ();
    activeFrame.setVisible (true);
    activeFrame.toFront ();

    new Parallel (
      new CSProcess[] {
        activeClosingFrame,
        mandelbrot
      }
    ).run ();

  }

}
