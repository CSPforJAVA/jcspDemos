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

package jcspDemos.infect;



import jcsp.lang.*;
import jcsp.util.*;
import jcsp.util.ints.*;
import jcsp.awt.*;
import java.awt.*;

/**
 * @author P.H. Welch
 */
class InfectNetwork implements CSProcess {

  private final ActiveCanvas activeCanvas;
  private final ActiveScrollbar scrollBar;
  private final ActiveButton[] button;
  private final PseudoButton pseudoButton;
  private final ActiveLabel infoLabel;
  private final ActiveLabel rateLabel;
  private final InfectionControl control;
  private final Infection infection;

  public InfectNetwork (final int rate, final Container parent) {

    parent.setLayout (new BorderLayout ());
    parent.setBackground (Color.blue);

    System.out.println ("Infect creating channels ...");
    
    final One2OneChannel[] event = Channel.one2oneArray (InfectionControl.NUMBER + 1, new OverWriteOldestBuffer (1));
    final One2OneChannel[] configure = Channel.one2oneArray (InfectionControl.NUMBER + 1);

    final One2OneChannelInt scrollEvent = Channel.one2oneInt (new OverWriteOldestBufferInt (1));
    final One2OneChannel scrollConfigure = Channel.one2one ();

    final One2OneChannel report = Channel.one2one ();

    final One2OneChannel toGraphics = Channel.one2one ();
    final One2OneChannel fromGraphics = Channel.one2one ();

    final One2OneChannel feedBack = Channel.one2one ();

    final One2OneChannel infoConfigure = Channel.one2one ();
    final One2OneChannel rateConfigure = Channel.one2one ();

    System.out.println ("InfectNetwork created channels");
    System.out.println ("InfectNetwork creating ActiveButtons ...");

    button = new ActiveButton[InfectionControl.NUMBER];
    for (int i = 0; i < InfectionControl.NUMBER; i++) {
      button[i]
        = new ActiveButton (configure[i].in (), event[i].out (), "XXXXXXXXXXXXX");
      button[i].setBackground (Color.white);
      System.out.println ("  button " + i);
    }

    infoLabel = new ActiveLabel (infoConfigure.in (), "XXXXXXXXXXXXX");
    infoLabel.setAlignment (Label.CENTER);
    infoLabel.setBackground (Color.white);

    rateLabel = new ActiveLabel (rateConfigure.in (), "XXXXXXXXXXXXX");
    rateLabel.setAlignment (Label.CENTER);
    rateLabel.setBackground (Color.white);

    System.out.println ("InfectNetwork created ActiveButtons ... now adding them to the parent ...");
    
    final Panel north = new Panel ();
    final Panel south = new Panel ();

    north.add (button[InfectionControl.RESET]);
    north.add (infoLabel);
    north.add (button[InfectionControl.FREEZE]);
    south.add (button[InfectionControl.RANDOM]);
    south.add (rateLabel);
    south.add (button[InfectionControl.CENTRE]);

    parent.add ("North", north);
    parent.add ("South", south);

    System.out.println ("InfectNetwork creating ActiveScrollbar ...");

    scrollBar = new ActiveScrollbar (scrollConfigure.in (), scrollEvent.out (),
                                     Scrollbar.VERTICAL, 100 - rate, 25, 0, 125);
    scrollBar.setBackground (Color.white);

    System.out.println ("InfectNetwork created ActiveScrollbar ... now adding it to the parent ...");

    parent.add ("West", scrollBar);

    System.out.println ("InfectNetwork now creating ActiveCanvas ...");
    activeCanvas = new ActiveCanvas ();
    activeCanvas.setGraphicsChannels (toGraphics.in (), fromGraphics.out ());
    activeCanvas.setSize (parent.getSize ());

    // If the parent is an applet, the above setSize has no effect and the activeCanvas
    // is fitted to the "Center" area (see below) of the applet's panel.

    // If the parent is a frame, the above *does* define the size of the activeCanvas
    // and the size of the parent is expanded to wrap around when it is packed.

    System.out.println ("InfectNetwork adding ActiveCanvas to the parent ...");
    parent.add ("Center", activeCanvas);

    System.out.println ("InfectNetwork creating infectionControl ...");
    control = new InfectionControl (Channel.getInputArray (event), Channel.getOutputArray (configure), report.out ());

    System.out.println ("InfectNetwork now creating infection ...");
    infection = new Infection (rate, report.in (), scrollEvent.in (), scrollConfigure.out (),
                               infoConfigure.out (), rateConfigure.out (), feedBack.out (),
                               toGraphics.out (), fromGraphics.in ());

    System.out.println ("InfectNetwork creating pseudo button ...");

    pseudoButton = new PseudoButton (configure[InfectionControl.NUMBER].in (),
                                     event[InfectionControl.NUMBER].out (),
                                     feedBack.in ());

  }

  public void run () {

    System.out.println ("Infect starting up the network ...");

    new Parallel (
      new CSProcess[] {
        activeCanvas,
        scrollBar,
        pseudoButton,
        infoLabel,
        rateLabel,
        control,
        infection,
        new Parallel (button)
      }
    ).run ();

  }

}
