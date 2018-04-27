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



/**
 * @author Quickstone Technologies Limited
 */
final class Counter {
	
	private static final int NUM_SAMPLES = 50;
	
	private final long timeSample[] = new long[NUM_SAMPLES];
	private final long dataSample[] = new long[NUM_SAMPLES];
	private int samplePtr = 0, samples = 0;
	private long timeSum = 0, dataSum = 0;
	
	private long tag = 0;
	
	public final void click () {
		long t = System.currentTimeMillis ();
		if (tag == 0) {
			tag = t;
			return;
		}
		timeSum -= timeSample[samplePtr];
		timeSum += (timeSample[samplePtr] = (t - tag));
		if (++samplePtr == NUM_SAMPLES) samplePtr = 0;
		if (samples < NUM_SAMPLES) samples++;
		if ((samplePtr & 15) == 0) {
			System.out.println ("Count = " + (timeSum / samples) + "ms/frame (" + ((double)samples * 1000.0 / (double)timeSum) + " fps)");
		}
		tag = t;
	}
	
	public final void dataStart () {
		tag = System.currentTimeMillis ();
	}
		
	public final void dataEnd (int bytes) {
		long t = System.currentTimeMillis ();
		timeSum -= timeSample[samplePtr];
		timeSum += (timeSample[samplePtr] = (t - tag));
		dataSum -= dataSample[samplePtr];
		dataSum += (dataSample[samplePtr] = bytes);
		if (++samplePtr == NUM_SAMPLES) samplePtr = 0;
		if (samples < NUM_SAMPLES) samples++;
		if ((samplePtr & 15) == 0) {
			System.out.println ("Data = " + (dataSum / timeSum) + "pixels/ms");
		}
	}
	
}
