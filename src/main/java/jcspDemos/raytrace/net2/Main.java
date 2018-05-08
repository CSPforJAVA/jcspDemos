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


import jcsp.awt.ActiveCanvas;
import jcsp.awt.ActiveFrame;
import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.TCPIPNodeAddress;
import jcsp.util.InfiniteBuffer;
import jcsp.util.OverWritingBuffer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Kerridge net2 version
 */
public class Main {
	
	public static final int BUFFERING = 5;//PROD:5
	
	private static final int DEFAULT_WIDTH = 640,
								DEFAULT_HEIGHT = 480;

	public static void main (String[] args) throws Exception {
		
		// Get the command line
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		boolean fullScreen = false;
		String mainIPAddress = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals ("-width")) {
				width = Integer.parseInt (args[++i]);
			} else if (args[i].equals ("-height")) {
				height = Integer.parseInt (args[++i]);
			} else if (args[i].equals ("-fullscreen")) {
				fullScreen = true;
			} else if (args[i].equals ("-internal")){
        mainIPAddress = "127.0.0.1";
			}
		}
    // create the main node
    TCPIPNodeAddress mainNodeAddress = null;
    if (mainIPAddress == null){  // creating node on real network
      mainNodeAddress = new TCPIPNodeAddress(3000);
    } else {  // loop back node
      mainNodeAddress = new TCPIPNodeAddress(mainIPAddress, 3000);
    }
    Node.getInstance().init(mainNodeAddress);
    System.out.println ("Server running on " + mainNodeAddress.getIpAddress());
		// Establish the NET channels
		final NetAltingChannelInput workers2demux = NetChannel.net2one();  // vcn = 50
		final NetAltingChannelInput workerJoin = NetChannel.net2one();  // vcn = 51
		final NetAltingChannelInput workerLeave = NetChannel.net2one();  // vcn = 52
		System.out.println ("Main: waiting for initial worker");
		NetChannelLocation ncl = (NetChannelLocation)workerJoin.read ();
		final NetChannelOutput[] toWorkers = new NetChannelOutput[] { NetChannel.one2net (ncl) };
		
		// Widget control channels
		final One2OneChannel frameControl = Channel.one2one ();
							  
		// Widget event channels
		final One2OneChannel frameEvent = Channel.one2one (new OverWritingBuffer(10)),
						      canvasEvent = Channel.one2one (new OverWritingBuffer(10)),
						      mouseEvent = Channel.one2one (new OverWritingBuffer(1)),
						      keyEvent = Channel.one2one (new OverWritingBuffer(10));
		
		// Graphics channels
        final Any2OneChannel toGraphics = Channel.any2one();
        final One2OneChannel fromGraphics = Channel.one2one();
        
        // FWH network
        final One2OneChannel farmer2harvester = Channel.one2one (),
        					  frameDemux[] = Channel.one2oneArray (BUFFERING, new InfiniteBuffer()),
        					  ui2farmer = Channel.one2one ();

        // Set up the canvas
        final ActiveCanvas activeCanvas = new ActiveCanvas();
        activeCanvas.addComponentEventChannel (canvasEvent.out ());
        activeCanvas.setGraphicsChannels(toGraphics.in(), fromGraphics.out());
        activeCanvas.setSize(width, height);
        activeCanvas.addMouseMotionEventChannel (mouseEvent.out ());
        activeCanvas.addKeyEventChannel (keyEvent.out ());

        // Set up the frame
        final ActiveFrame activeFrame = new ActiveFrame(frameControl.in (), frameEvent.out (), "Ray Tracing Demonstration");
        activeFrame.add (activeCanvas);
        
        // Try and go full screen ?
        if (fullScreen) {
            final GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment ();
            final GraphicsDevice graphDev = graphEnv.getDefaultScreenDevice ();
            try {
                if (graphDev.isFullScreenSupported ()) {
                	activeFrame.setUndecorated (true);
    		        activeFrame.pack ();
                    activeFrame.setVisible (true);
                    activeFrame.toFront ();
                	graphDev.setFullScreenWindow (activeFrame);
                	graphDev.setDisplayMode (new DisplayMode (width, height, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
                }
            } catch (Throwable e) {
            	System.out.println ("Full screen display failed - available modes:");
            	DisplayMode[] dms = graphDev.getDisplayModes ();
            	outer: for (int i = 0; i < dms.length; i++) {
            		if (dms[i].getBitDepth () >= 24) {
            			for (int j = 0; j < i; j++) {
            				if ((dms[j].getWidth () == dms[i].getWidth ())
            				 && (dms[j].getHeight () == dms[i].getHeight ())
            				 && (dms[j].getBitDepth () == dms[i].getBitDepth ())) continue outer;
            			}
    	       			System.out.println ("\t" + dms[i].getWidth () + "x" + dms[i].getHeight () + "x" + dms[i].getBitDepth ());
            		}
            	}
            	activeFrame.dispose ();
            	activeFrame.setUndecorated (false);
            }
        }
        if (!activeFrame.isDisplayable ()) {
        	activeFrame.pack ();
        	activeFrame.setVisible (true);
        	activeFrame.toFront ();
        }
        
        // Widget event ALT
        final Alternative alt = new Alternative(new Guard[] { frameEvent.in (), canvasEvent.in (), mouseEvent.in (), keyEvent.in () });
		
		final Parallel par = new Parallel(new CSProcess[] {
			activeCanvas,
			activeFrame,
			new CSProcess() {
				public void run () {
					while (true) {
						ComponentEvent e;
						switch (alt.select ()) {
							case 0 : // FRAME
        						e = (ComponentEvent)frameEvent.in ().read ();
        						switch (e.getID ()) {
        							case WindowEvent.WINDOW_CLOSING :
        								System.out.println ("AWT: closing window");
        								toGraphics.out ().write (null);
        								frameControl.out ().write (null);
        								ui2farmer.out ().write (null);
        								return;
        						}
        						break;
							case 1 : // CANVAS
        						e = (ComponentEvent)canvasEvent.in ().read ();
        						switch (e.getID ()) {
        							case ComponentEvent.COMPONENT_RESIZED :
        								Dimension newSize = activeCanvas.getSize ();
        								if ((newSize.getWidth () > 4) && (newSize.getHeight () > 4)) {
        									System.out.println ("AWT: canvas resized to " + (int)newSize.getWidth () + ", " + (int)newSize.getHeight ());
        									ui2farmer.out ().write (new Farmer.ResizeMessage ((int)newSize.getWidth (), (int)newSize.getHeight ()));
        								}
        								break;
        						}
        						break;
							case 2 : // MOUSE
								e = (ComponentEvent)mouseEvent.in ().read ();
								switch (e.getID ()) {
									case MouseEvent.MOUSE_MOVED :
										ui2farmer.out ().write (e);
										break;
								}
								break;
							case 3 : // KEY
								e = (ComponentEvent)keyEvent.in ().read ();
								switch (e.getID ()) {
									case KeyEvent.KEY_PRESSED :
										ui2farmer.out ().write (e);
										break;
								}
								break;
						}
					}
				}
			},
			new Farmer (toWorkers, farmer2harvester.out (), ui2farmer.in (), workerJoin, workerLeave),
			new FrameDeMux (workers2demux, Channel.getOutputArray (frameDemux)),
			new Harvester (Channel.getInputArray (frameDemux), farmer2harvester.in (), toGraphics.out (), fromGraphics.in ())
		});
		
		par.run ();
		
	}
	
}
