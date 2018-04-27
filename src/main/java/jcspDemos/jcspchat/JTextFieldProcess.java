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

package jcspDemos.jcspchat;



import javax.swing.*;
import javax.swing.event.*;
import jcsp.lang.*;
import java.awt.event.*;

/**
 * @author Quickstone Technologies Limited
 */
public class JTextFieldProcess implements CSProcess {
  private ChannelOutput out;
  private JTextField jtf;
  private ChannelInput namein;

  public JTextFieldProcess(JTextField field, ChannelOutput chan, boolean needsConfirm) {
    jtf = field;
    out = chan;
    if (needsConfirm) {
      jtf.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JTextFieldProcess.this.out.write(jtf.getText());
          jtf.setText("");
        }
      });
    }
    else {
      jtf.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
        }
        public void insertUpdate (DocumentEvent e) {
          JTextFieldProcess.this.out.write(jtf.getText());
        }
        public void removeUpdate (DocumentEvent e) {}
      });
    }
  }
  public void run() {
  }
}
