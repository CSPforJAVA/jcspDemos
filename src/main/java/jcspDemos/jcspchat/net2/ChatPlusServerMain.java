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

package jcspDemos.jcspchat.net2;


import jcsp.lang.CSProcess;
import jcsp.lang.Channel;
import jcsp.lang.One2OneChannel;
import jcsp.lang.Parallel;
import jcsp.net2.NetAltingChannelInput;
import jcsp.net2.NetChannel;
import jcsp.net2.Node;
import jcsp.net2.tcpip.TCPIPNodeAddress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Kerridge for net2 version
 */

public class ChatPlusServerMain {

  public static void main(String[] args) throws java.io.IOException {

    TCPIPNodeAddress serverNodeAddress = new TCPIPNodeAddress(1000); // default port
    Node.getInstance().init(serverNodeAddress);

    System.out.println("Server node: " + serverNodeAddress.getIpAddress()+ "/1000");

    JFrame serverFrame = new JFrame();
    String s =
      "Chat channel "
//        + chatChanName
        + "running on "
        + Node.getInstance().getNodeID();
    JButton exitButton = new JButton("Exit");
    Box vbox = Box.createVerticalBox();
    serverFrame.getContentPane().add(vbox);

    JLabel label = new JLabel(s);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setAlignmentX(0.5F);
    exitButton.setAlignmentX(0.5F);

    vbox.add(label);
    vbox.add(Box.createVerticalStrut(10));
    vbox.add(exitButton);
    exitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    serverFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    serverFrame.pack();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension td = serverFrame.getSize();
    serverFrame.setBounds(
      (d.width - td.width) / 2,
      (d.height - td.height) / 2,
      td.width,
      td.height);
    serverFrame.setVisible(true);
    
    NetAltingChannelInput connectIn = NetChannel.numberedNet2One(100);
    One2OneChannel connectAuth2dd = Channel.one2one();
    NetAltingChannelInput messageChan = NetChannel.numberedNet2One(101);

    new Parallel(
      new CSProcess[] {
        new CustomDynamicDelta(messageChan, connectAuth2dd.in()),
        new ConnectionAuthenticator(
          connectIn,
          connectAuth2dd.out(),
          messageChan)})
      .run();
  }
}
