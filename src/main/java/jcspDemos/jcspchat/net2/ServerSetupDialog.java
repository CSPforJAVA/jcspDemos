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


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * @author Quickstone Technologies Limited
 * @author Jon Kerridge for net2 version
 */
public class ServerSetupDialog extends JDialog{

  private JTextField channelTextField = new JTextField("", 30);
  private JButton createButton = new JButton ("Create");
  private String channelName;

  public ServerSetupDialog() {
    this.setModal(true);
    Container cp = this.getContentPane();
    Box vbox1 = Box.createVerticalBox();
    cp.add(vbox1);
    vbox1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,1, Color.darkGray),BorderFactory.createMatteBorder(1,1,0,0, Color.white)),BorderFactory.createEmptyBorder(4,4,4,4)));
    Box hbox2 = Box.createHorizontalBox();
    vbox1.add(Box.createVerticalStrut(8));
    vbox1.add(hbox2);
    hbox2.add(new JLabel("Channel Number:"));
    hbox2.add(Box.createHorizontalStrut(2));
    channelTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),BorderFactory.createEmptyBorder(2,2,2,2)));
    hbox2.add(channelTextField);
    vbox1.add(Box.createVerticalStrut(8));
    vbox1.add(createButton);
    createButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        channelName = channelTextField.getText();
        if (channelName != "") {
          ServerSetupDialog.this.setVisible(false);
        }
      }
    });

    this.pack();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension td = this.getSize();
    this.setBounds((d.width - td.width)/2,(d.height - td.height)/2,td.width,td.height);
    this.setVisible(true);
  }
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }
  public String getChannelName() {
    return channelName;
  }

}
