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

package jcspDemos.altingBarriers;

import java.awt.Color;


/**
 * This contains the variables shared by each (synchronised) playing group.
 * Safe access is assured by <i>Exclusive-Read-Exclusive-Write</i> (EREW)
 * operation by each gadget.
 * The parcel is constructed by the <i>rightmost</i> (leader) gadget and
 * passed up and down the <i>rail track</i> connecting the group.
 * Only the gadget currently holding the parcel operates on it.
 * When it passes it on, it lets go the reference.
 */
public class Parcel {

  /** 
   * This is incremented by each group member as the parcel passes through.
   */
  public int count;

  /**
   * This is set (randomly) by the group leader at the start
   * of a new game.
   */
  public final Color colour;

  /** 
   * This is set <code>true</code> by any group member whose
   * button has been clicked.
   * It is routed immediately back towards the leader.
   * The leader gadget informs the rest of the group when it
   * receives a poisoned parcel (or when it receivesa non-poisoned
   * parcel but its own button has been clicked).
   * This (game over) information is passed in the same cycle by
   * the leader synchronising on the <i>alting barrier</i> that
   * caused the game to start and on which the others are waiting
   * (along with channels on which the parcel may arrive).
   */
  public boolean poisoned = false;

  /**
   * A parcel is constructed by the (group leader) gadget at
   * the start of each game.
   */
  public Parcel (Color colour, int count) {
    this.colour = colour;
    this.count = count;
  }

}
