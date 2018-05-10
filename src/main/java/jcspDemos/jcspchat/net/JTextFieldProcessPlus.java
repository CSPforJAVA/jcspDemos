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
import javax.swing.event.*;
import jcsp.lang.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */
public class JTextFieldProcessPlus implements CSProcess {
  private ChannelOutput out;
  private JTextField jtf;
  private String user;

  public JTextFieldProcessPlus(JTextField field, ChannelOutput chan, String username) {
    jtf = field;
    out = chan;
    user = username;
    jtf.setText("Type your message here");

    jtf.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()+"\n"));
          jtf.setText("");
        }
      }

    });

    jtf.addFocusListener(new FocusAdapter() {
      boolean initialMessage = true;
      public void focusGained(FocusEvent e) {
        if (initialMessage) {
          jtf.setText("");
          initialMessage=false;
        }
      }
    });

    jtf.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
      }
      public void insertUpdate (DocumentEvent e) {
        JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()));
      }
      public void removeUpdate (DocumentEvent e) {
        JTextFieldProcessPlus.this.out.write(new MessageObject(user,jtf.getText()));
      }
    });

  }
  public void run() {
  }
}
