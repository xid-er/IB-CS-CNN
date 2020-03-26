/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eesignatures;

/**
 *
 * @author User
 */
public class NNMath {
    public static double[][] matrixAdd(double[][] a, double[][] b) {
        if (a.length == 0 || b.length == 0 || a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Cannot add unequal matrices");
        }

        double result[][] = new double[a.length][a[0].length];

        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }

        return result;
    }
}
