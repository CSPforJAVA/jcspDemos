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

package jcspDemos.matrix;



import java.util.Random;
import jcsp.lang.*;

/**
 * @author P.H. Welch
 */
public class Matrix {

  public static void randomise (final double[][] X, final double limit, final Random random) {
    final double halfLimit = limit/2.0d;
    for (int i = 0; i < X.length; i++) {
      final double[] Xi = X[i];
      for (int j = 0; j < Xi.length; j++) {
        Xi[j] = (limit*random.nextDouble ()) - halfLimit;
      }
    }
  }

  public static void print (final double[][] X, final int rows, final int cols) {
    for (int i = 0; i < rows; i++) {
      final double[] Xi = X[i];
      for (int j = 0; j < cols; j++) {
        if ((j%5) == 0) System.out.println ();
        System.out.print ("\t" + Xi[j]);
      }
    }
    System.out.println ();
  }

  public static boolean same (final double[][] X, final double[][] Y) {
    for (int i = 0; i < X.length; i++) {
      final double[] Xi = X[i];
      final double[] Yi = Y[i];
      for (int j = 0; j < Xi.length; j++) {
        if (Xi[j] != Yi[j]) {
          System.out.println ("X[" + i + "][" + j + "] = " + Xi[j]);
          System.out.println ("Y[" + i + "][" + j + "] = " + Yi[j]);
          return false;
        }
      }
    }
    return true;
  }

  public static void multiply (final double[][] X, final double[][] Y, final double[][] Z) {
    if (X[0].length != Y.length) {
      throw new MultiplyBoundsException ("X[0].length != Y.length");
    }
    if (X.length != Z.length) {
      throw new MultiplyBoundsException ("X.length != Z.length");
    }
    if (Y[0].length != Z[0].length) {
      throw new MultiplyBoundsException ("Y[0].length != Z[0].length");
    }
    for (int i = 0; i < X.length; i++) {
      final double[] Xi = X[i];
      final double[] Zi = Z[i];
      for (int j = 0; j < Y[0].length; j++) {
        double sum = 0.0d;
        for (int k = 0; k < Y.length; k++) {
          sum += Xi[k]*Y[k][j];
        }
        Zi[j] = sum;
      }
    }
  }

  public static void seqMultiply (final double[][] X, final double[][] Y, final double[][] Z) {
    if (X[0].length != Y.length) {
      throw new MultiplyBoundsException ("X[0].length != Y.length");
    }
    if (X.length != Z.length) {
      throw new MultiplyBoundsException ("X.length != Z.length");
    }
    if (Y[0].length != Z[0].length) {
      throw new MultiplyBoundsException ("Y[0].length != Z[0].length");
    }
    final CSProcess[] rowProcess = new CSProcess[X.length];
    for (int i = 0; i < X.length; i++) {
      final int ii = i;
      rowProcess[ii] = new CSProcess () {
        public void run () {
          final double[] Xi = X[ii];
          final double[] Zi = Z[ii];
          final double[][] YY = Y;
          for (int j = 0; j < YY[0].length; j++) {
            double sum = 0.0d;
            for (int k = 0; k < YY.length; k++) {
              sum += Xi[k]*YY[k][j];
            }
            Zi[j] = sum;
          }
        }
      };
    }
    new Sequence (rowProcess).run ();
  }

  public static void parMultiply (final double[][] X, final double[][] Y, final double[][] Z) {
    if (X[0].length != Y.length) {
      throw new MultiplyBoundsException ("X[0].length != Y.length");
    }
    if (X.length != Z.length) {
      throw new MultiplyBoundsException ("X.length != Z.length");
    }
    if (Y[0].length != Z[0].length) {
      throw new MultiplyBoundsException ("Y[0].length != Z[0].length");
    }
    final CSProcess[] rowProcess = new CSProcess[X.length];
    for (int i = 0; i < X.length; i++) {
      final int ii = i;
      rowProcess[ii] = new CSProcess () {
        public void run () {
          final double[] Xi = X[ii];
          final double[] Zi = Z[ii];
          final double[][] YY = Y;
          for (int j = 0; j < YY[0].length; j++) {
            double sum = 0.0d;
            for (int k = 0; k < YY.length; k++) {
              sum += Xi[k]*YY[k][j];
            }
            Zi[j] = sum;
          }
        }
      };
    }
    final Parallel par = new Parallel (rowProcess);
    par.run ();
    par.releaseAllThreads ();
  }

  public static Parallel makeParMultiply (final double[][] X,
                                          final double[][] Y,
                                          final double[][] Z) {
    if (X[0].length != Y.length) {
      throw new MultiplyBoundsException ("X[0].length != Y.length");
    }
    if (X.length != Z.length) {
      throw new MultiplyBoundsException ("X.length != Z.length");
    }
    if (Y[0].length != Z[0].length) {
      throw new MultiplyBoundsException ("Y[0].length != Z[0].length");
    }
    final CSProcess[] rowProcess = new CSProcess[X.length];
    for (int i = 0; i < X.length; i++) {
      final int ii = i;
      rowProcess[ii] = new CSProcess () {
        public void run () {
          final double[] Xi = X[ii];
          final double[] Zi = Z[ii];
          final double[][] YY = Y;
          for (int j = 0; j < YY[0].length; j++) {
            double sum = 0.0d;
            for (int k = 0; k < YY.length; k++) {
              sum += Xi[k]*YY[k][j];
            }
            Zi[j] = sum;
          }
        }
      };
    }
    return new Parallel (rowProcess);
  }

  public static class MultiplyBoundsException extends RuntimeException {

    public MultiplyBoundsException (String s) {
      super (s);
    }

  }

}
