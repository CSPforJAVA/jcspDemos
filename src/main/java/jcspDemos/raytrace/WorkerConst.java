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



import jcsp.lang.*;
import jcsp.net.*;
import jcsp.net.cns.*;
import jcsp.net.tcpip.*;
import jcsp.util.*;
import jcsp.userIO.*;
import java.util.Random;
import java.io.*;

/**
 * @author Quickstone Technologies Limited
 */
public final class WorkerConst implements CSProcess {

	private final ChannelInput fromFarmer;
	private final ChannelOutput toHarvester;

	private WorkerConst (ChannelInput fromFarmer, ChannelOutput toHarvester) {
		this.fromFarmer = fromFarmer;
		this.toHarvester = toHarvester;
	}

	public final void run () {
		System.out.println ("Worker: started");
		final ResultPacket[] results = new ResultPacket[Main.BUFFERING];
		final Counter frameCounter = new Counter(), dataCounter = new Counter ();
		for (int i = 0; i < Main.BUFFERING; i++) results[i] = new ResultPacket ();
		int currentResult = 0;
		final int c = new Random ().nextInt ();
		while (true) {
			final ResultPacket result = results[currentResult];
			if (++currentResult >= Main.BUFFERING) currentResult = 0;
			// Request some work
			//System.out.println ("Worker: waiting for work");
			final WorkPacket work = (WorkPacket)fromFarmer.read ();
			// Determine the result packet
			result.frame = work.frame;
			result.offset = work.offset;
			result.step = work.step;
			int workLength = work.width * (work.height / work.step) * 3;
			if ((result.color == null) || (result.color.length != workLength)) {
				System.out.println ("Worker: allocating new result block");
				result.color = new byte[workLength];
			}
			dataCounter.dataStart ();
			int i = 0, j = workLength / 3, k = (j << 1);
			for (int y = work.offset; y < work.height; y += work.step) {
				for (int x = 0; x < work.width; x++) {
					result.color[i++] = (byte)(c >> 16);
					result.color[j++] = (byte)(c >> 8);
					result.color[k++] = (byte)c;
				}
			}
			frameCounter.click ();
			dataCounter.dataEnd (workLength);
			// Send to harvester
			//System.out.println ("Worker: writing frame " + result.frame + " to harvester");
			toHarvester.write (result);
		}
	}

	private final Object identity_sync = new Object ();
	private int identity_count = 0;

	private final class TerminatingIdentity implements CSProcess {
		private final ChannelInput in;
		private final ChannelOutput out;
		public TerminatingIdentity (ChannelInput in, ChannelOutput out) {
			this.in = in;
			this.out = out;
		}
		public void run () {
			while (true) {
				Object o = in.read ();
				if (o == null) System.exit (1);
				synchronized (identity_sync) {
					identity_count++;
				}
				out.write (o);
			}
		}
	}

	private final class FlushingIdentity implements CSProcess {
		private final AltingChannelInput in, flush;
		private final ChannelOutput out;
		public FlushingIdentity (AltingChannelInput in, AltingChannelInput flush, ChannelOutput out) {
			this.in = in;
			this.flush = flush;
			this.out = out;
		}
		public void run () {
			final Alternative alt = new Alternative (new Guard[] { flush, in });
			while (true) {
				if (alt.priSelect () == 0) {
					flush.read ();
					while (true) {
						synchronized (identity_sync) {
							if (identity_count == 0) System.exit (0);
							System.out.println ("Worker: " + identity_count + " frame(s) left");
						}
						Object o = in.read ();
        				synchronized (identity_sync) {
        					identity_count--;
        				}
        				out.write (o);
					}
				} else {
    				Object o = in.read ();
    				synchronized (identity_sync) {
    					identity_count--;
    				}
    				out.write (o);
				}
			}
		}
	}

	public static final void main (String[] args) throws Exception {

		// Get the command line
		if (args.length != 1) {
			Ask.app (
				"Ray Tracer",
				"Implements a basic ray-tracing algorithm incorporating reflection, refraction and texture mapped " +
				"surfaces, distributed using a farmer/worker/harvester approach. This is a worker node. By specifying " +
				"the address of a CNS server that the farmer/harvester registered with it can join an existing network." +
				"This demonstration shows performance/scaleability using JCSP. Workers can join and leave at any time. " +
				"To leave the network, press ENTER. Do not terminate the program with CTRL+C or the worker may not " +
				"deregister itself with the farmer and cause a deadlock.");
			Ask.addPrompt ("CNS address");
			Ask.show ();
			Node.getInstance ().init (new TCPIPNodeFactory (Ask.readStr ("CNS address")));
		} else {
			Node.getInstance ().init (new TCPIPNodeFactory (args[0]));
		}

		// Establish the NET connections
		final NetChannelEndFactory factory = new UnacknowledgedNetChannelEndFactory ();
		final NetChannelOutput toHarvester = factory.createOne2Net (CNS.resolve("org.jcsp.demos.raytrace.demux"));
		final NetChannelOutput joinNetwork = CNS.createOne2Net ("org.jcsp.demos.raytrace.join");
		final NetChannelOutput leaveNetwork = CNS.createOne2Net ("org.jcsp.demos.raytrace.leave");
		final NetChannelInput fromFarmer = factory.createNet2One ();

		System.out.println ("Worker: joining network");
		joinNetwork.write (fromFarmer.getChannelLocation ());

		final One2OneChannel farmer2worker = Channel.one2one (new InfiniteBuffer ()),
							  worker2harvester = Channel.one2one (new InfiniteBuffer ()),
							  flushSignal = Channel.one2one ();

		WorkerConst w = new WorkerConst (farmer2worker.in (), worker2harvester.out ());
		new Parallel (
			new CSProcess[] {
				w.new TerminatingIdentity (fromFarmer, farmer2worker.out ()),
				w,
				w.new FlushingIdentity (worker2harvester.in (), flushSignal.in (), toHarvester),
				new CSProcess () {
					public void run () {
    					try {
    						System.in.read ();
    					} catch (IOException e) {
    					}
    					System.out.println ("Worker: leaving network");
    					leaveNetwork.write (fromFarmer.getChannelLocation ());
    					System.out.println ("Worker: flushing outstanding packets");
    					flushSignal.out ().write (null);
					}
				}
			}
		).run ();

	}

}
