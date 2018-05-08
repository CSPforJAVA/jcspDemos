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


import jcsp.lang.*;
import jcsp.net2.*;
import jcsp.net2.tcpip.TCPIPNodeAddress;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Kerridge for net2 version
 */

public class ChatPlusClientMain {

  private boolean cnsConnected = false;

  public ChatPlusClientMain() {
  }
  public static void main(String[] args) {

    ConnectDialog cd = new ConnectDialog();
    String cnsName = cd.getCNSName(); // actually the server IP address
    String channelName = cd.getChannelName(); // actually port number of the client
    String username = cd.getUsername();
    int port = Integer.valueOf(channelName); // dangerous should check that the string is integer

    TCPIPNodeAddress serverNodeAddress = new TCPIPNodeAddress(cnsName, 1000);

    TCPIPNodeAddress clientNodeAddress = new TCPIPNodeAddress(port);
    Node.getInstance().init(clientNodeAddress);

    System.out.println("Client node: " + clientNodeAddress.getIpAddress() + " port: " + port);


    //setup channels
    ChannelOutput messageOutChan;

    NetChannelOutput connectChan = NetChannel.one2net(serverNodeAddress, 100);
    System.out.println("created connectChan from client to server");
    NetChannelInput messageInChan = NetChannel.numberedNet2One(200);
    NetChannelLocation messageInLocation = (NetChannelLocation)messageInChan.getLocation();
    System.out.println("created messageInChannel for server to client communications");
    System.out.println("at location " + messageInLocation.toString());


    ChannelOutput serverOutChan =
      NetChannel.one2net((NetChannelLocation)messageInChan.getLocation());
    One2OneChannel sorter2WhiteboardChan = Channel.one2one();
    One2OneChannel sorter2ChatChan = Channel.one2one();
    Any2OneChannel output2BufferChan = Channel.any2one();
    DrawingSettings ds =
      new DrawingSettings(Color.blue, Color.orange, true, 3, 1);
    ChatPlusFrame cpf = new ChatPlusFrame(ds);
    // set title of frame to username
    cpf.setTitle(username);
    One2OneChannel buffer2IdChan = Channel.one2one();
    One2OneChannelInt id2BufferChan = Channel.one2oneInt();
    One2OneChannelInt localRedrawNotifyChan = Channel.one2oneInt();

    connectChan.write(new ConnectionBundle(username, messageInLocation, true));
    Object o = messageInChan.read();

    messageOutChan = null;
    if (o instanceof NetChannelLocation) {
      messageOutChan = NetChannel.any2net((NetChannelLocation) o);
    }
    else {
      Boolean b = (Boolean) o;
      if (b.booleanValue()) {
        System.out.println("client: received confirm");
      }
      else {
        JOptionPane.showMessageDialog(
          new JFrame(),
          "The username "
            + username
            + " is already in use.\n"
            + "Please try a different name");
        System.exit(0);
      }
    }

    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    cpf.setSize(d.width, d.height / 2);
    cpf.setVisible(true);

    cpf.initComponents();
    cpf.validate();
    ArrayList al = new ArrayList(2);
    al.add(
      new MessageObject(
        username,
        username + " connected.\n",
        MessageObject.CONNECT));

    messageOutChan.write(al);

    CSProcess wsp =
      new WhiteboardSendProcess(
        ds,
        cpf.getWhiteboard(),
        cpf.getWipeButton(),
        cpf.getWhiteboardScrollPane(),
        cpf.getToolMenu(),
        cpf,
        output2BufferChan.out(),
        localRedrawNotifyChan.in(),
        username,
        connectChan,
        serverOutChan);

    new Parallel(
      new CSProcess[] {
        new SorterProcess(
          messageInChan,
          sorter2WhiteboardChan.out(),
          sorter2ChatChan.out()),
        new JTextFieldProcessPlus(
          cpf.getMessageArea(),
          output2BufferChan.out(),
          username),
        new MessageReceiverProcess(
          username,
          sorter2ChatChan.in(),
          cpf.getUserArea(),
          cpf.getChatLog()),
        new CoalescingBuffer(
          output2BufferChan.in(),
          buffer2IdChan.out(),
          id2BufferChan.in()),
        new ReadyReportingIdentity(
          buffer2IdChan.in(),
          messageOutChan,
          id2BufferChan.out()),
        wsp,
        new WhiteboardReceiveProcess(
          cpf.getWhiteboard(),
          sorter2WhiteboardChan.in(),
          localRedrawNotifyChan.out(),
          username)})
      .run();

  }
}
