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


import jcsp.lang.*;
import jcsp.net2.NetChannel;
import jcsp.net2.NetChannelInput;
import jcsp.net2.NetChannelOutput;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;
import jcsp.userIO.Ask;
import jcsp.util.InfiniteBuffer;

import java.io.IOException;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Kerridge net2 version
 */
public final class Worker implements CSProcess {

	private final ChannelInput fromFarmer;
	private final ChannelOutput toHarvester;

	private Worker (ChannelInput fromFarmer, ChannelOutput toHarvester) {
		this.fromFarmer = fromFarmer;
		this.toHarvester = toHarvester;
	}

	public final void run () {
		System.out.println ("Worker: started");
		final ResultPacket[] results = new ResultPacket[Main.BUFFERING];
		for (int i = 0; i < Main.BUFFERING; i++) results[i] = new ResultPacket ();
		int currentResult = 0;
		final Counter frameCounter = new Counter (), dataCounter = new Counter ();
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
			final ImageDef image = work.image;
			final double wh = (double)work.height, ww = (double)work.width;
			for (int y = work.offset; y < work.height; y += work.step) {
				final double _y = (double)y;
				double left_x = (_y * (image.view_window_x4 - image.view_window_x1)) / wh + image.view_window_x1;
				double left_y = (_y * (image.view_window_y4 - image.view_window_y1)) / wh + image.view_window_y1;
				double left_z = (_y * (image.view_window_z4 - image.view_window_z1)) / wh + image.view_window_z1;
				final double step_x = ((_y * (image.view_window_x3 - image.view_window_x2)) / wh + image.view_window_x2 - left_x) / ww;
				final double step_y = ((_y * (image.view_window_y3 - image.view_window_y2)) / wh + image.view_window_y2 - left_y) / ww;
				final double step_z = ((_y * (image.view_window_z3 - image.view_window_z2)) / wh + image.view_window_z2 - left_z) / ww;
				for (int x = 0; x < work.width; x++) {
					// trace the ray
					int c = image.trace (
						image.camera_x,
						image.camera_y,
						image.camera_z,
						left_x - image.camera_x,
						left_y - image.camera_y,
						left_z - image.camera_z,
						ImageDef.AMBIENT,
						0);
					result.color[i++] = (byte)(c >> 16);
					result.color[j++] = (byte)(c >> 8);
					result.color[k++] = (byte)c;
					left_x += step_x;
					left_y += step_y;
					left_z += step_z;
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
			final Alternative alt = new Alternative(new Guard[] { flush, in });
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
		TCPIPNodeAddress mainNodeAddress = null;
		TCPIPNodeAddress workerNodeAddress = null;
		String serverAddress = null;
		System.out.println("Worker starting ...");
    serverAddress = Ask.string ("Server address? : ");
		mainNodeAddress = new TCPIPNodeAddress(serverAddress, 1000);
    System.out.println ("Server running on " + mainNodeAddress.getIpAddress());
    int port = Ask.Int("port to use on worker node >=2000 ? ", 2000, 9000);
		if ( serverAddress.equals("127.0.0.1")){
		  // loop back network ask for worker IP address
      String workerAddress = Ask.string ("Worker address- 127.0.0.N st 1 < N < 255? : ");
      workerNodeAddress = new TCPIPNodeAddress(workerAddress, port);
    } else {  // find IP address of this node
      workerNodeAddress = new TCPIPNodeAddress(port);
    }
		Node.getInstance().init(workerNodeAddress);
		System.out.println ("Worker running on " + workerNodeAddress.getIpAddress());
		// Establish the NET connections
		final NetChannelOutput toHarvester = NetChannel.one2net (mainNodeAddress, 50);
		final NetChannelOutput joinNetwork = NetChannel.one2net (mainNodeAddress, 51);
		final NetChannelOutput leaveNetwork = NetChannel.one2net (mainNodeAddress, 52);

		final NetChannelInput fromFarmer = NetChannel.net2one ();

		System.out.println ("Worker: joining network");
		joinNetwork.write (fromFarmer.getLocation ());

		final One2OneChannel farmer2worker = Channel.one2one (new InfiniteBuffer()),
							  worker2harvester = Channel.one2one (new InfiniteBuffer()),
							  flushSignal = Channel.one2one ();

		Worker w = new Worker (farmer2worker.in (), worker2harvester.out ());
		new Parallel(
			new CSProcess[] {
				w.new TerminatingIdentity(fromFarmer, farmer2worker.out ()),
				w,
				w.new FlushingIdentity(worker2harvester.in (), flushSignal.in (), toHarvester),
				new CSProcess() {
					public void run () {
    					try {
    						System.in.read ();
    					} catch (IOException e) {
    					}
    					System.out.println ("Worker: leaving network");
    					leaveNetwork.write (fromFarmer.getLocation ());
    					System.out.println ("Worker: flushing outstanding packets");
    					flushSignal.out ().write (null);
					}
				}
			}
		).run ();

	}

}
