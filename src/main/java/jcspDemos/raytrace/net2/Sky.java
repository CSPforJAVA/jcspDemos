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


import jcsp.lang.Barrier;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.net.URL;

/**
 * @author Quickstone Technologies Limited
 */
abstract class Sky {
	
	public static int width = -1, height = -1;
	public static int radius;
	public static int centre;
	
	public static int[] data;
	
	private static class Notify implements ImageObserver {
		
		public int width = -1, height = -1;

		private Barrier bar;
		
		public Notify (Barrier bar) {
			this.bar = bar;
		}
		
		public boolean imageUpdate (Image img, int infoFlags, int x, int y, int width, int height) {
			if ((infoFlags & (ERROR | ABORT)) != 0) {
				System.out.println ("Sky: image size error");
				bar.resign ();
				return false;
			}
			if ((infoFlags & WIDTH) != 0) {
				this.width = width;
			}
			if ((infoFlags & HEIGHT) != 0) {
				this.height = height;
			}
			if ((this.width == -1) || (this.height == -1)) {
				return true;
			} else {
				bar.resign ();
				return false;
			}
		}
		
	}
	
	static {
		try {
    		//System.out.println ("Sky: loading image");
    		final Toolkit tk = Toolkit.getDefaultToolkit ();
    		final URL url = Sky.class.getClassLoader ().getResource ("com/quickstone/jcsp/demos/raytrace/clouds.jpg");
    		if (url == null) throw new NullPointerException ();
    		//System.out.println (url.toString ());
    		final Image img = tk.getImage (url);
    		final Barrier bar = new Barrier(2);
    		final Notify nfy = new Notify (bar);
    		nfy.width = width = img.getWidth (nfy);
    		nfy.height = height = img.getHeight (nfy);
    		if ((width == -1) || (height == -1)) {
    			//System.out.println ("Sky: waiting for async load to complete");
    			bar.sync ();
    			width = nfy.width;
    			height = nfy.height;
    		}
    		if ((width == -1) || (height == -1)) {
    			throw new Exception ();
    		} else {
    			//System.out.println ("Sky: texture " + width + ", " + height);
	    		data = new int[width * height];
	    		final PixelGrabber pg = new PixelGrabber (img, 0, 0, width, height, data, 0, width);
	    		pg.grabPixels ();
    		}
		} catch (Exception e) {
			width = 256;
			height = 256;
			data = new int[width * height];
			for (int i = 0 ; i < width * height; i++) {
				data[i] = i;
			}
		} finally {
    		if (width < height) {
	    		radius = width >> 1;
    		} else {
	    		radius = height >> 1;
    		}
    		centre = (width >> 1) + (height >> 1) * width;
    		//System.out.println ("Sky: ready");
		}
	}

}
