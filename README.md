This bundle contains many demonstrations of the use of the JCSP (CSP for Java) 
library.  

Please read the LICENCE as further use of the library implies acceptance of either of the licences (Apache 2.0 or
GNU LPGL 2.1).

It depends on the JCSP core API, the source for which is obtainable at:
https://github.com/CSPforJAVA

The project was built using Intellij and a clone of the repository will obtain all the required 
files and the codes can be built using the build.gradle definition..

This project also uses the junit Testing Framework.

To build the demonstrations download the JCSP Demonstration source from
https://github.com/CSPforJAVA/jcspDemos 

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

In order to use the jcsp library the following are required in the gradle.build file.
<pre>
repositories {
  mavenCentral()
  maven { // to download the CSPforJAVA.jcsp library
    name = "GitHubPackages"
    url = "https://maven.pkg.github.com/CSPforJAVA/jcsp"
    credentials {
      username = project.findProperty("gpr.user")
      password = project.findProperty("gpr.key")
    }
  }
}

dependencies {
  compile 'cspforjava:jcsp:1.1.?'
}
</pre>

**Please note**

In order to download Github Packages a user requires to have a Github Personal Access Token.  
See https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token

A gradle.properties file is required at the same directory level as the build.gradle file
that contains

<pre>
gpr.user=userName
gpr.key=userPersonalAccessToken
</pre>
