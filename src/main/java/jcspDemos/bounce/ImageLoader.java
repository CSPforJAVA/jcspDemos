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

/**
 * @author P.H. Welch
 */
public class ImageLoader {

  final private static MediaTracker tracker = new MediaTracker (new Canvas ());

  public static Image[] load (BounceMain.Params params, final URL documentBase) {

    final int nImages = (params.to - params.from) + 1;
    if (nImages < 0) return null;

    final Image[] images = new Image[nImages];

    if (documentBase == null) {     // we're an application -- so load from a file
      for (int i = 0; i < nImages; i++) {
        String file = params.path + (i + params.from) + params.suffix;
        images[i] = Toolkit.getDefaultToolkit ().getImage (file);
        System.out.println ("ImageLoader.load: " + file + " " + images[i]);
      }
    } else {                        // we're an applet -- so load from a URL
      try {
        for (int i = 0; i < nImages; i++) {
          URL url = new URL (documentBase, params.path + (i + params.from) + params.suffix);
          images[i] = Toolkit.getDefaultToolkit ().getImage (url);
          System.out.println ("ImageLoader.load: " + url + " " + images[i]);
        }
      } catch (MalformedURLException e) {
        System.out.println ("ImageLoader.load: MalformedURLException" + e);
      }
    }

    for (int i = 0; i < nImages; i++) {
      tracker.addImage (images[i], 0);
    }
    try {
      tracker.waitForAll ();
    }
    catch (InterruptedException e) {
      System.out.println (e);
      System.exit (-1);
    }

    return images;

  }

}
