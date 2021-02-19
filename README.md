This bundle contains many demonstrations of the use of the JCSP (CSP for Java) library.

Please read the LICENCE as further use of the library implies acceptance of either of the licences (Apache 2.0 or
GNU LPGL 2.1).

It depends on the JCSP core API, the source for which is obtainable at:
https://github.com/CSPforJAVA

This project also uses the junit Testing Framework.

To build the demonstrations download the source from
https://github.com/CSPforJAVA/jcspDemos 

The build.gradle file will build using the JCSP library version 1.1.2 that uses
is downloaded from JitPack: 
<pre>
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.CSPforJAVA:jcsp:1.1.2'
}
</pre>


The documentation provided with the JCSP core API also contains many examples of the use of the components that make up
the library.  These examples are available in the package jcspDemos.docExamples.

The other examples have a splash screen that explains the purpose of the demonstration.  These screens are provided
in the package jcsp.userIO, which also provides some very simple command line interactions for the inputting of 
values into applications. 

Three examples are provided of the use of both networking packages (net and net2).  These are 
contained in the packages raytrace, mandelbrot and jcspchat.  Mandelbrot is also provided as an example 
that runs on a single node.

In order to use the net versions it is necessary to run a channel name service.  
This is available in package: jcsp.net.tcpip.TCPIPCNSServer

