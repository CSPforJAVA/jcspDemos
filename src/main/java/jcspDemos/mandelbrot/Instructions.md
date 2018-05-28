**Mandelbrot Demonstrations**

There are three different versions of the Mandelbrot demonstration:
multicore, net and net2, each of which is executed in a different way.

**Multicore**
Run MandelbrotMain - it will produce a splash screen with some instructions,
do not change the defaults and click OK.  A further screen will be produced showing a
coloured representation of the Mandelbrot set.  You can select a portion
of the space with the a mouse double-click.  
The default system runs with 8 worker processes.  This number can be
changed by modifying the value of nWorkers on line 34 of MandelNetwork.
By changing the number of workers based upon the number of cores in the available 
machine you can observer the time it takes to produce an image.
Typically, once you exceed the number of cores plus hyper threads (if any),
the performance tends to get worse.  This may not be obvious because the time 
taken to produce an image depends on the number of black points in the image.

**Net**
Run MandelbrotMainNet - in this case the image space is opened immediately but
remains black, until you start some worker processes.  In the background the 
process has started and instance of a CNS server, running on the 
local node at port 7890.  This is used to provide the means whereby net 
channels are created.  This is indicated by a message in the console output, 
an example of which is shown in context below:

:MandelbrotMainNet.main()
jcsp.net.LinkManager
	Running
jcsp.net.tcpip.TCPIPLinkServer
	TCP/IP V4 LinkServer listening on 192.168.1.69:7890 Started


Run MandelWorkerNet
A splash screen will appear asking for the CNS address.
This obtained from the MandelbrotMainNet console as shown above and means
you will need to copy the IP address, which in this case is 192.168.1.69 .
Once you click OK an image will appear.  As you add further MandelWorkerNet 
processes, the rate at which images are created will improve.  Each MandelWorkerNet
adds a further 10 threads, see line 36 of MandelWorkerNet.  The time to produce
the image can be seen on the MandebrotlMainNet console.

**Net2**
Run MandelbrotMainNet2 - in this case the image space is opened immediately but
remains black, until you start some worker processes.  The default version runs 
on a real network but a loopback IP address can be created if, the argument -internal is
supplied as a program argument, through main(args).
The console output will indicate the IP address of MandelbrotMainNet2, for example,

Main Node IP address = 192.168.1.69 now running

Run MandelWorkerNet2
A splash screen will appear asking for the IP address of the main process.
This obtained from the MandelbrotMainNet console as shown above and means
you will need to copy the IP address, which in this case is 192.168.1.69 .
It also asks for the worker port number, which is required if the worker process runs 
on the same node as the main process.  Use any port number except 1000. 
If the worker is running on another machine any port number can be used.
Once you click OK an image will appear.  As you add further MandelWorkerNet 
processes, the rate at which images are created will improve.  Each MandelWorkerNet
adds a further 10 threads, see line 38 of MandelWorkerNet2.  The time to produce
the image can be seen on the MandebrotlMainNet2 console.
The net2 version will run much slower than the net version becasue the net version
automatically accounts for processes running on the same node, whereas net2 assumes 
the processes will run on different machines.
You can run the net2 version with a loopback IP address by specifying the arguemnt
value -internal as arg[0] of MandelWorkerMainNet2, in which case the suer is asked to
specify the loopback IP address of the worker which must be anything other than 127.0.0.1, 
which is used by the main process.



