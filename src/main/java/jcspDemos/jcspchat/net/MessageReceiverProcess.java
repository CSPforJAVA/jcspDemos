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
import jcsp.lang.*;

/**
 * @author Quickstone Technologies Limited
 */
public class MessageReceiverProcess implements CSProcess {
  private String user;
  private ChannelInput in;
  private JList userlist;
  private JTextArea chatlog;
  private DefaultListModel listmodel;

  public MessageReceiverProcess(String user, ChannelInput in, JList userlist, JTextArea chatlog) {
  this.user = user;
  this.in = in;
  this.userlist = userlist;
  this.chatlog = chatlog;
  listmodel = new DefaultListModel();
  userlist.setModel(listmodel);
  }

  public boolean updateRealtimeArea(String name, String message, boolean remove) {
    if (remove) {
      for (int x=0; x < listmodel.getSize(); x++ ) {
        UserInListObject uilo = (UserInListObject)listmodel.getElementAt(x);
        String s = uilo.getUserName();
        if (name.equals(s)) {
          listmodel.removeElementAt(x);
          return true;
        }
      }
    }
    for (int x= 0; x < listmodel.getSize(); x++) {
      UserInListObject uilo = (UserInListObject)listmodel.getElementAt(x);
      String s = uilo.getUserName();
      if (name.equals(s)) {
        uilo.setMessageText(message);
        userlist.update(userlist.getGraphics());
        return true;
      }
    }
    UserInListObject newUserObject = new UserInListObject(name,message);
    listmodel.addElement(newUserObject);
    return false;
  }

  public void run() {
    while (true) {
      MessageObject mess = (MessageObject)in.read();
      final String messageText = mess.message;
      final String messageUser = mess.user;
      final int sysCommand = mess.sysCommand;
      if (sysCommand != MessageObject.USERMESSAGE) {
        SwingUtilities.invokeLater(new Runnable() {
          final int command = sysCommand;
          public void run() {
            String s = chatlog.getText();
            if (s != null) {
              s = s + messageText.toUpperCase();
            }
            else {
              s = messageText.toUpperCase();
            }
            chatlog.setText(s);
            if (command == MessageObject.CONNECT) {
              if (!(user.equals(messageUser))) {
                MessageReceiverProcess.this.updateRealtimeArea(messageUser,"",false);
              }
            }
            else if (command == MessageObject.DISCONNECT) {

              if (!(user.equals(messageUser))) {
                MessageReceiverProcess.this.updateRealtimeArea(messageUser,"",true);
              }
            }

          }
        });
      }
      else {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (messageText.endsWith("\n")) {
              String s = chatlog.getText();
              if (s != null) {
                s = s + messageUser + ": " + messageText;
              }
              else {
                s = messageUser + ": " + messageText;
              }
              chatlog.setText(s);
              if (!(user.equals(messageUser))) {
                MessageReceiverProcess.this.updateRealtimeArea(messageUser,"",false);
              }
            }
            else {
              if (!(user.equals(messageUser))) {
                MessageReceiverProcess.this.updateRealtimeArea(messageUser,messageText,false);
              }
            }
          }
        });
      }
    }
  }
}
