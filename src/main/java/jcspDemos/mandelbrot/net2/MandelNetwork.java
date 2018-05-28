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

package jcspDemos.mandelbrot.net2;


import jcsp.awt.ActiveButton;
import jcsp.awt.ActiveCanvas;
import jcsp.awt.ActiveChoice;
import jcsp.awt.ActiveLabel;
import jcsp.lang.CSProcess;
import jcsp.lang.Channel;
import jcsp.lang.One2OneChannel;
import jcsp.lang.Parallel;
import jcsp.net2.NetAltingChannelInput;
import jcsp.net2.NetChannel;
import jcsp.util.OverWriteOldestBuffer;

import java.awt.*;

/**
 * @author Quickstone Technologies Limited
 * @author P.H. Welch (non-networked original code)
 * @author Jon Kerridge - net2 version
 */
class MandelNetwork implements CSProcess {

    private final ActiveCanvas activeCanvas;
    private final MandelControl mandelControl;
    private final MandelFarmer mandelFarmer;
    private final MandelHarvester mandelHarvester;
    private final ActiveChoice scrollChoice;
    private final ActiveChoice iterationsChoice;
    private final ActiveChoice targetChoice;
    private final ActiveChoice colourChoice;
    private final ActiveButton forwardButton;
    private final ActiveButton backwardButton;
    private final ActiveButton cancelButton;
    private final ActiveLabel[] infoLabel;

    public MandelNetwork(final Container parent) {

        final int minMaxIterations = 256;
        final int maxMaxIterations = 16 * 4096;

        // channels

        final One2OneChannel mouseChannel =
                Channel.one2one(new OverWriteOldestBuffer(10));
        final One2OneChannel mouseMotionChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel keyChannel =
                Channel.one2one(new OverWriteOldestBuffer(10));

        final One2OneChannel farmer2harvester = Channel.one2one();
        final One2OneChannel harvester2farmer = Channel.one2one();
        // create net channels used by remote worker processes at vcn 100 (farmer) and 101 (harvester)
        // the node has been previously created in MandelbrotMainNet
        final NetAltingChannelInput workers2farmer = NetChannel.numberedNet2One(100);
        final NetAltingChannelInput workers2harvester = NetChannel.numberedNet2One(101);

        final One2OneChannel request = Channel.one2one();
        final One2OneChannel reply = Channel.one2one();

        final One2OneChannel toGraphics = Channel.one2one();
        final One2OneChannel fromGraphics = Channel.one2one();

        // processes

        parent.setLayout(new BorderLayout());
        parent.setBackground(Color.black);

        activeCanvas = new ActiveCanvas();
        activeCanvas.addMouseEventChannel(mouseChannel.out());
        activeCanvas.addMouseMotionEventChannel(mouseMotionChannel.out());
        activeCanvas.addKeyEventChannel(keyChannel.out());
        activeCanvas.setGraphicsChannels(toGraphics.in(), fromGraphics.out());
        activeCanvas.setSize(parent.getSize());

        parent.add("Center", activeCanvas);

        // menus

        final Panel south = new Panel();
        south.setBackground(Color.green);

        final One2OneChannel backwardChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel backwardConfigure = Channel.one2one();
        backwardButton =
                new ActiveButton(
                        backwardConfigure.in(),
                        backwardChannel.out(),
                        "Backward");
        backwardButton.setBackground(Color.white);
        backwardButton.setEnabled(false);
        south.add(backwardButton);

        final One2OneChannel forwardChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel forwardConfigure = Channel.one2one();
        forwardButton =
                new ActiveButton(
                        forwardConfigure.in(),
                        forwardChannel.out(),
                        "Forward");
        forwardButton.setBackground(Color.white);
        forwardButton.setEnabled(false);
        south.add(forwardButton);

        // south.add (new Label ("      ", Label.CENTER));
        // padding

        final One2OneChannel scrollChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel scrollConfigure = Channel.one2one();
        scrollChoice =
                new ActiveChoice(scrollConfigure.in(), scrollChannel.out());
        final String[] scrollMenu = {"Silent", "Up", "Down", "None"};
        for (int i = 0; i < scrollMenu.length; i++) {
            scrollChoice.add(scrollMenu[i]);
        }
        south.add(new Label("Scrolling", Label.CENTER));
        south.add(scrollChoice);

        final One2OneChannel iterationsChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel iterationsConfigure = Channel.one2one();
        iterationsChoice =
                new ActiveChoice(iterationsConfigure.in(), iterationsChannel.out());
        final String[] iterationsMenu =
                {"256", "512", "1K", "2K", "4K", "8K", "16K", "32K", "64K"};
        for (int i = 0; i < iterationsMenu.length; i++) {
            iterationsChoice.add(iterationsMenu[i]);
        }
        south.add(new Label("Iterations", Label.CENTER));
        south.add(iterationsChoice);

        final One2OneChannel targetChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel targetConfigure = Channel.one2one();
        targetChoice =
                new ActiveChoice(targetConfigure.in(), targetChannel.out());
        /*
            final String[] targetMenu = {"White", "Black", "Xor"};
        */
        final String[] targetMenu = {"White", "Black"};
        for (int i = 0; i < targetMenu.length; i++) {
            targetChoice.add(targetMenu[i]);
        }
        south.add(new Label("Target", Label.CENTER));
        south.add(targetChoice);

        final One2OneChannel colourChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel colourConfigure = Channel.one2one();
        colourChoice =
                new ActiveChoice(colourConfigure.in(), colourChannel.out());
        final String[] colourMenu = {"Step", "Fade"};
        for (int i = 0; i < colourMenu.length; i++) {
            colourChoice.add(colourMenu[i]);
        }
        south.add(new Label("Colours", Label.CENTER));
        south.add(colourChoice);

        south.add(new Label("      ", Label.CENTER)); // padding

        final One2OneChannel cancelChannel =
                Channel.one2one(new OverWriteOldestBuffer(1));
        final One2OneChannel cancelConfigure = Channel.one2one();
        cancelButton =
                new ActiveButton(
                        cancelConfigure.in(),
                        cancelChannel.out(),
                        "Cancel");
        cancelButton.setBackground(Color.white);
        cancelButton.setEnabled(false);
        south.add(cancelButton);

        parent.add("South", south);

        // labels

        final Panel north = new Panel();
        north.setBackground(Color.green);

        final String[] infoTitle = {"Top", "Left", "Scale"};
        final String[] infoWidth =
                {
                        "XXXXXXXXXXXXXXXXXXXXXX",
                        "XXXXXXXXXXXXXXXXXXXXXX",
                        "XXXXXXXXXXXXXXXXXXXXXX"};
        final One2OneChannel[] infoConfigure =
                Channel.one2oneArray(infoTitle.length);
        infoLabel = new ActiveLabel[infoTitle.length];
        for (int i = 0; i < infoTitle.length; i++) {
            infoLabel[i] = new ActiveLabel(infoConfigure[i].in(), infoWidth[i]);
            infoLabel[i].setAlignment(Label.CENTER);
            infoLabel[i].setBackground(Color.white);
            north.add(new Label(infoTitle[i], Label.CENTER));
            north.add(infoLabel[i]);
        }

        parent.add("North", north);

        mandelControl =
                new MandelControl(
                        minMaxIterations,
                        maxMaxIterations,
                        mouseChannel.in(),
                        mouseMotionChannel.in(),
                        keyChannel.in(),
                        scrollConfigure.out(),
                        scrollChannel.in(),
                        scrollMenu,
                        iterationsConfigure.out(),
                        iterationsChannel.in(),
                        iterationsMenu,
                        targetConfigure.out(),
                        targetChannel.in(),
                        targetMenu,
                        colourConfigure.out(),
                        colourChannel.in(),
                        colourMenu,
                        forwardConfigure.out(),
                        forwardChannel.in(),
                        backwardConfigure.out(),
                        backwardChannel.in(),
                        Channel.getOutputArray(infoConfigure),
                        request.out(),
                        reply.in(),
                        toGraphics.out(),
                        fromGraphics.in());

        mandelFarmer =
                new MandelFarmer(
                        request.in(),
                        harvester2farmer.in(),
                        farmer2harvester.out(),
                        cancelChannel.in(),
                        cancelConfigure.out(),
                        workers2farmer);

        mandelHarvester =
                new MandelHarvester(
                        reply.out(),
                        farmer2harvester.in(),
                        harvester2farmer.out(),
                        workers2harvester,
                        toGraphics.out(),
                        fromGraphics.in());

    }

    public void run() {

        new Parallel(
                new CSProcess[]{
                        activeCanvas,
                        mandelControl,
                        mandelFarmer,
                        mandelHarvester,
                        scrollChoice,
                        iterationsChoice,
                        targetChoice,
                        colourChoice,
                        forwardButton,
                        backwardButton,
                        cancelButton,
                        new Parallel(infoLabel)})
                .run();
    }

}
