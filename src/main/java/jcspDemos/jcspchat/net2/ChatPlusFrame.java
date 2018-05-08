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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * @author Quickstone Technologies Limited
 */

public class ChatPlusFrame extends JFrame {
  private DrawingPanel whiteboard = new DrawingPanel();
  private JTextField messageArea = new JTextField(80);
  private JTextArea chatLog = new JTextArea();
  private JList userArea = new JList();
  private JSplitPane horizPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
  private JSplitPane vertPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  private JScrollPane chatLogScrollPane = new JScrollPane(chatLog);
  private JScrollPane whiteboardScrollPane = new JScrollPane(whiteboard);
  private JScrollPane userAreaScrollPane = new JScrollPane(userArea);
  private JToolBar whiteboardBar = new JToolBar(JToolBar.HORIZONTAL);
  private LineColorButton lcb;
  private FillColorButton fcb;
  private DrawingSettings ds;
  private JButton wipeButton;
  private JComboBox toolMenu = new JComboBox(new Object[] {"Freehand","Line","Rectangle","Round Rect","Oval","Text"});
  private JComboBox strokeSizeMenu = new JComboBox(new Object[] {"1","2","3","4","5"});


  public ChatPlusFrame(DrawingSettings ds) {
    this.ds = ds;
    Container cp = this.getContentPane();
    JPanel outerpanel = new JPanel(new BorderLayout());
    cp.add(outerpanel);
    outerpanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.white),BorderFactory.createMatteBorder(0,0,1,1,Color.darkGray)),BorderFactory.createEmptyBorder(1,1,10,1)));
    Box vbox = Box.createVerticalBox();
    vbox.add(Box.createVerticalStrut(4));
    Box hbox = Box.createHorizontalBox();
    hbox.add(Box.createHorizontalStrut(4));
    hbox.add(messageArea);
    hbox.add(Box.createHorizontalStrut(4));
    vbox.add(hbox);
    outerpanel.add(vbox,BorderLayout.SOUTH);
    outerpanel.add(vertPane, BorderLayout.CENTER);
    vertPane.setTopComponent(horizPane);
    vertPane.setBottomComponent(userAreaScrollPane);
    JPanel whiteboardLayoutPanel = new JPanel();
    whiteboardLayoutPanel.setLayout(new BorderLayout());
    whiteboardLayoutPanel.add(whiteboardBar,BorderLayout.NORTH);
    whiteboardBar.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));


    whiteboardLayoutPanel.add(whiteboardScrollPane, BorderLayout.CENTER);
    horizPane.setLeftComponent(whiteboardLayoutPanel);
    horizPane.setRightComponent(chatLogScrollPane);
    chatLog.setEditable(false);
  }
//  protected void processWindowEvent(WindowEvent e) {
//    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//      System.exit(0);
//    }
//  }

  public DrawingPanel getWhiteboard() {
    return whiteboard;
  }
  public JTextField getMessageArea() {
    return messageArea;
  }
  public JList getUserArea() {
    return userArea;
  }
  public JTextArea getChatLog() {
    return chatLog;
  }
  public JButton getWipeButton() {
    return wipeButton;
  }
  public JScrollPane getWhiteboardScrollPane() {
    return whiteboardScrollPane;
  }
  public JComboBox getToolMenu() {
    return toolMenu;
  }
  public void initComponents() {

    userArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    vertPane.setOneTouchExpandable(true);
    horizPane.setOneTouchExpandable(true);
    Dimension d =new Dimension(DrawingPanel.whiteboardWidth,DrawingPanel.whiteboardHeight);
    whiteboard.setPreferredSize(d);
    whiteboard.setMinimumSize(d);
    vertPane.setDividerLocation(0.75);
    vertPane.setResizeWeight(0.75);
    horizPane.setDividerLocation(0.75);
    messageArea.setMargin(new Insets(4,4,4,4));
    lcb = new LineColorButton(ds);
    fcb = new FillColorButton(ds);

    strokeSizeMenu.setSelectedIndex(2);

    toolMenu.setSelectedIndex(0);    
    whiteboardBar.setFloatable(false);
    whiteboardBar.add(new JLabel("Tool:"));
    whiteboardBar.add(Box.createHorizontalStrut(2));

    whiteboardBar.add(toolMenu);
    toolMenu.setPreferredSize(new Dimension(100,22));
    toolMenu.setMaximumSize(new Dimension(100,22));
    toolMenu.setMinimumSize(new Dimension(100,22));
    whiteboardBar.add(Box.createHorizontalStrut(4));

    whiteboardBar.add(new JLabel(" Stroke Size:"));
    whiteboardBar.add(Box.createHorizontalStrut(2));
    whiteboardBar.add(strokeSizeMenu);
    strokeSizeMenu.setPreferredSize(new Dimension(40,22));
    strokeSizeMenu.setMaximumSize(new Dimension(40,22));
    strokeSizeMenu.setMinimumSize(new Dimension(40,22));
    whiteboardBar.add(Box.createHorizontalStrut(4));
    strokeSizeMenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ds.setStrokeSize(strokeSizeMenu.getSelectedIndex()+1);
      }
    });
    whiteboardBar.addSeparator();
    whiteboardBar.add(Box.createHorizontalStrut(4));
    whiteboardBar.add(new JLabel("Line:"));
    whiteboardBar.add(Box.createHorizontalStrut(2));
    whiteboardBar.add(lcb);
    whiteboardBar.add(Box.createHorizontalStrut(4));
    whiteboardBar.add(new JLabel("Fill:"));
    whiteboardBar.add(Box.createHorizontalStrut(2));
    whiteboardBar.add(fcb);
    lcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(ChatPlusFrame.this,"Choose Line Colour",ds.getLineColor());
        ds.setLineColor(newColor);
        lcb.repaint();
      }
    });
    fcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(ChatPlusFrame.this,"Choose Fill Colour",ds.getLineColor());
        ds.setFillColor(newColor);
        fcb.repaint();
      }
    });


    whiteboardBar.add(Box.createHorizontalStrut(4));
    JCheckBox fillCheckBox = new JCheckBox("Filled Shapes:",true);
    fillCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
    fillCheckBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
          ds.setFilled(false);
        }
        else {
          ds.setFilled(true);
        }
      }
    });
    whiteboardBar.add(fillCheckBox);
    whiteboardBar.addSeparator();
    whiteboardBar.add(Box.createHorizontalStrut(4));
    wipeButton = new JButton("Wipe");
    whiteboardBar.add(wipeButton);


  }
}
