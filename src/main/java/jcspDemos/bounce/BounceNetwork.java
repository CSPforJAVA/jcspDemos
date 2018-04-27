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

package jcspDemos.bounce;



import java.awt.*;
import java.net.*;
import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class BounceNetwork implements CSProcess {

  private final Bounce[] bounce;

  public BounceNetwork (final BounceMain.Params[] params,
                        final URL documentBase,
                        final Container parent) {

    bounce = new Bounce[params.length];

    final Object[] images = new Object[params.length];
    final Panel[] panel = new Panel[params.length];

    for (int i = 0; i < params.length; i++) {
      images[i] = ImageLoader.load (params[i], documentBase);
      bounce[i] = new Bounce ((Image[]) images[i],
                              params[i].background,
                              parent, params.length);
      panel[i] = bounce[i].getPanel ();
    }

    parent.setLayout (new GridLayout (params.length, 1));
    parent.add (panel[0]);
    parent.add (panel[1]);
    parent.add (panel[2]);

  }

  public void run () {

    new Parallel (bounce).run ();

  }

}

