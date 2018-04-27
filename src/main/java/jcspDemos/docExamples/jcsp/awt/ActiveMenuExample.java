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

package jcspDemos.docExamples.jcsp.awt;

import java.awt.*;
import java.awt.event.*;
import jcsp.lang.*;
import jcsp.util.*;
import jcsp.awt.*;

public class ActiveMenuExample {

  public static void main (String argv[]) {

    final ActiveClosingFrame activeClosingFrame =
      new ActiveClosingFrame ("ActiveCheckboxMenuItem Example");

    final ActiveFrame frame = activeClosingFrame.getActiveFrame ();

    final MenuBar menuBar = new MenuBar ();
    frame.setMenuBar (menuBar);

    final Menu fileMenu = new Menu ("File");
    menuBar.add (fileMenu);

    final String[] fileOptions = {"Hello World", "Rocket Science", "CSP",
                                  "Monitors", "Ignore Me", "Goodbye World"};

    final Any2OneChannel event[] = Channel.any2oneArray (2, new OverWriteOldestBuffer (10));

    final ActiveMenuItem[] fileMenuItem =
      new ActiveMenuItem[fileOptions.length];
    for (int i = 0; i < fileOptions.length; i++) {
      fileMenuItem[i] = new ActiveMenuItem (null, event[0].out (), fileOptions[i]);
      fileMenu.add (fileMenuItem[i]);
    }

    fileMenu.addSeparator ();

    final Any2OneChannel langConfigure = Channel.any2one ();
    final ActiveMenu langMenu = new ActiveMenu (langConfigure.in (), null, "Language");
    fileMenu.add (langMenu);  // set up the active langMenu as a sub-menu

    final String[] langOptions = {"occam-pi", "Java", "Smalltalk", "Algol-60",
                                  "Pascal", "Haskell", "SML", "Lisp"};

    final ActiveCheckboxMenuItem[] langCheckboxMenuItem =
      new ActiveCheckboxMenuItem[langOptions.length];
    for (int i = 0; i < langOptions.length; i++) {
      langCheckboxMenuItem[i] =
        new ActiveCheckboxMenuItem (null, event[1].out (), langOptions[i]);
      langMenu.add (langCheckboxMenuItem[i]);
    }

    frame.setSize (700, 350);
    frame.setBackground (Color.green);
    frame.setVisible (true);

    new Parallel (
      new CSProcess[] {     // don't forget to include all active processes
        langMenu,
        activeClosingFrame,
        new Parallel (fileMenuItem),
        new Parallel (langCheckboxMenuItem),
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              final String s = (String) event[0].in ().read ();
              System.out.println ("File ==> `" + s + "' selected ...");
              if (s == fileOptions[0]) {
                langConfigure.out ().write (Boolean.TRUE);
                System.out.println ("`Language' enabled ...");
              }
              running = (s != fileOptions[fileOptions.length - 1]);
            }
            frame.setVisible (false);
            System.exit (0);
          }
        },
        new CSProcess () {
          public void run () {
            while (true) {
              final ItemEvent e = (ItemEvent) event[1].in ().read ();
              final String item = (String) e.getItem ();
              System.out.print ("Language ==> `" + item);
              if (e.getStateChange () == ItemEvent.SELECTED) {
                System.out.println ("' selected ...");
                if (item == langOptions[0]) {
                  langConfigure.out ().write (Boolean.FALSE);
                  System.out.println ("`Language' disabled ...");
                }
              } else {
                System.out.println ("' deselected ...");
              }
            }
          }
        }
      }
    ).run ();

  }

}
