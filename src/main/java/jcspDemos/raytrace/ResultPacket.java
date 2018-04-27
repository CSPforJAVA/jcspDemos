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

package jcspDemos.raytrace;



import java.io.*;
import java.util.*;

/**
 * @author Quickstone Technologies Limited
 */
class ResultPacket implements Externalizable {
	
	public int frame;
	public int offset, step;
	public byte[] color;
	private int expandedSize;
	
	public ResultPacket () {
	}
	
	private static final Object sync = new Object ();
	private static Vector v1 = new Vector (), v2 = new Vector ();
	
	private static byte[] alloc (int len, boolean scratch) {
		synchronized (sync) {
			final Vector v = scratch ? v1 : v2;
			if (v.size () > 0) {
				byte[] b = (byte[])v.remove (v.size () - 1);
				if (b.length != len) {
					//System.out.println ("ResultPacket: realloc memory (" + len + ")");
					return new byte[len];
				} else {
					//System.out.println ("ResultPacket: pooled memory (" + len + ")");
					return b;
				}
			} else {
				//System.out.println ("ResultPacket: alloc memory (" + len + ")");
				return new byte[len];
			}
		}
	}

	private static void unalloc (byte[] b, boolean scratch) {
		synchronized (sync) {
			//System.out.println ("ResultPacket: freed memory (" + b.length + ")");
			(scratch ? v1 : v2).addElement (b);
		}
	}
	
	private void writeInt (ObjectOutput out, int i) throws IOException {
		out.write ((byte)(i >> 24));
		out.write ((byte)(i >> 16));
		out.write ((byte)(i >> 8));
		out.write ((byte)i);
	}
	
	private int readInt (ObjectInput in) throws IOException {
		int a = (in.read () << 24) & 0xFF000000;
		a |= (in.read () << 16) & 0xFF0000;
		a |= (in.read () << 8) & 0xFF00;
		return a | (in.read () & 0xFF);
	}
	
	public void writeExternal (ObjectOutput out) {
		try {
			writeInt (out, frame);
			writeInt (out, offset);
			writeInt (out, step);
			writeInt (out, color.length);
			
			byte[] buffer = alloc (((color.length + 127) >> 7) * 129, true);
			int bufptr = 0;

    		int mark = 0;
    		for (int i = 0; i < color.length - 3; i++) {
    			byte ref = color[i];
    			if ((ref == color[i + 1]) && (ref == color[i + 2])) {
    				// A block starts at I
    				if (mark != i) {
    					// Write out any raw data
    					buffer[bufptr++] = (byte)((i - mark) - 1);
    					while (mark < i) {
    						buffer[bufptr++] = color[mark++];
    					}
    					mark = i;
    				}
    				// Determine extent of block
    				while ((i < color.length) && (color[i] == ref) && (i - mark < 127)) i++;
    				// Write block
    				buffer[bufptr++] = (byte)((i - mark) | 0x80);
    				buffer[bufptr++] = ref;
    				// Update the reference marker
    				mark = i;
    				// Decrement I because it will increment at the end of the loop
    				i--;
    			} else {
    				// Check the raw data block is not overlength
    				if (i - mark == 128) {
    					// Write out the raw data
    					buffer[bufptr++] = (byte)((i - mark) - 1);
    					while (mark < i) {
    						buffer[bufptr++] = color[mark++];
    					}
    					mark = i;
    				}
    			}
    		}
    		// Tail end of the data
    		if (mark != color.length) {
    			buffer[bufptr++] = (byte)((color.length - mark) - 1);
    			while (mark < color.length) {
    				buffer[bufptr++] = color[mark++];
    			}
    		}
    		
    		writeInt (out, bufptr);
    		out.write (buffer, 0, bufptr);
    		unalloc (buffer, true);
    		
    		//System.out.println ("Compression = " + ((bufptr * 100) / color.length) + "%");

		} catch (Exception e) {
			System.err.println (e);
			//System.exit (1);
		}
	}
	
	public void readExternal (ObjectInput in) {
		try {
			frame = readInt (in);
			offset = readInt (in);
			step = readInt (in);
			expandedSize = readInt (in);
			color = alloc (((expandedSize + 127) >> 7) * 129, true);
			final int compressedSize = readInt (in);
			for (int i = 0; i < compressedSize; i++) {
				color[i] = (byte)in.read ();
			}
		} catch (Exception e) {
			System.err.println (e);
			//System.exit (1);
		}
	}
	
	public void expand () {
		final byte[] source = color;
		color = alloc (expandedSize, false);
		for (int i = 0, ptr = 0; i < expandedSize; ) {
			int r = source[ptr++] & 0xFF;
			if ((r & 0x80) == 0) {
				// Raw block
				for (int j = 0; j <= r; j++) {
					color[i++] = source[ptr++];
				}
			} else {
				// Constant block
				r &= 0x7F;
				byte b = source[ptr++];
				for (int j = 0; j < r; j++) {
					color[i++] = b;
				}
			}
		}
		unalloc (source, true);
	}
	
	public void discard () {
		unalloc (color, false);
	}
	
}
