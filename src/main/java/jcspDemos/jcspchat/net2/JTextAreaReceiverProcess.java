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
import jcsp.lang.ChannelInput;

import javax.swing.*;

/**
 * @author Quickstone Technologies Limited
 */
public class JTextAreaReceiverProcess implements CSProcess {
  private JTextArea jta;
  private ChannelInput in;

  public JTextAreaReceiverProcess(JTextArea jta, ChannelInput in) {
    this.jta = jta;
    this.in = in;
  }
  public void run() {
    while (true) {
      final String messageText = (String)in.read();
      final JTextArea temparea  = jta;
      Runnable r = new Runnable() {
        public void run() {
          try {
            String s = temparea.getText();
            if (s != null) {
              s = s + messageText;
            }
            else {
              s = messageText;
            }
            temparea.setText(s);
          }
          catch (Exception e) {
            System.out.println(e);
          }
        }
      };
      SwingUtilities.invokeLater(r);
    }
  }
}
