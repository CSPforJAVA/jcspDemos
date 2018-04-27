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

package jcspDemos.net2.barriers;
/**
 * 
 */
import jcsp.lang.Barrier;
import jcsp.lang.CSProcess;

/**
 * @author Kevin
 */
public class NetBarrierTestProcess
    implements CSProcess
{
    private final Barrier toSync;

    int n;

    public NetBarrierTestProcess(Barrier bar, int procNum)
    {
        this.toSync = bar;
        this.n = procNum;
    }

    public void run()
    {
        while (true)
        {
            System.out.println("Process " + this.n + " syncing");
            this.toSync.sync();
            System.out.println("Process " + this.n + " released");
        }
    }

}
