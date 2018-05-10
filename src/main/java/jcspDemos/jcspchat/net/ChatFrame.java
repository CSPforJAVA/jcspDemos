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

package jcspDemos.jcspchat.net;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */

public class ChatFrame extends JFrame {
  private JTextField nameField = new JTextField();
  private JTextArea chatArea = new JTextArea();
  private JTextField messageField = new JTextField();

  public ChatFrame() {
    //layout and init components
    Container contentPane = this.getContentPane();
    Box vbox1 = Box.createVerticalBox();
    Box hbox1 = Box.createHorizontalBox();
    //vbox1.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
    vbox1.add(hbox1);
    hbox1.add(new JLabel("Username:"));
    hbox1.add(Box.createHorizontalStrut(2));
    hbox1.add(nameField);
    hbox1.add(Box.createGlue());
    hbox1.add(Box.createHorizontalStrut(2));
    vbox1.add(Box.createVerticalStrut(8));
    vbox1.add(chatArea);
    chatArea.setPreferredSize(new Dimension(400,400));
    chatArea.setEditable(false);
    chatArea.setBorder(BorderFactory.createLoweredBevelBorder());
    vbox1.add(Box.createVerticalStrut(8));
    vbox1.add(messageField);
    messageField.setPreferredSize(new Dimension(400,40));
    messageField.setBorder(BorderFactory.createLoweredBevelBorder());
    contentPane.add(vbox1);

  }
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
  public JTextField getNameTextField() {
    return nameField;
  }
  public JTextField getMessageTextField() {
    return messageField;
  }
  public JTextArea getChatTextArea() {
    return chatArea;
  }
}
