package com.cgvsu.protocurvefxapp;


public class LagrangeCurve {

    private final double[] t;
    private final double[] x;
    private final double[] y;

    public LagrangeCurve(final double[] x, final double[] y) {
        if (x == null || y == null || x.length != y.length) {
            throw new IllegalArgumentException("Массивы x и y должны быть одной длины");
        }
        if (x.length == 0) {
            throw new IllegalArgumentException("Нужна хотя бы одна точка");
        }
        this.x = x.clone();
        this.y = y.clone();
        this.t = calculateParams(x, y);
    }


    private static double[] calculateParams(final double[] x, final double[] y) {
        final double[] params = new double[x.length];
        for (int i = 1; i < x.length; i++) {
            final double distance = Math.hypot(x[i] - x[i - 1], y[i] - y[i - 1]);
            if (distance == 0) {
                throw new IllegalArgumentException("Соседние точки совпадают: узлы t должны быть различны");
            }
            params[i] = params[i - 1] + distance;
        }
        return params;
    }

    public double getTStart() {
        return t[0];
    }

    public double getTEnd() {
        return t[t.length - 1];
    }


    public double[] point(final double param) {
        return new double[]{
            interpolate(x, param), interpolate(y, param)};
    }


    private double interpolate(final double[] values, final double param) {
        double sum = 0;
        for (int i = 0; i < t.length; i++) {
            double basis = 1;
            for (int j = 0; j < t.length; j++) {
                if (j != i) {
                    basis *= (param - t[j]) / (t[i] - t[j]);
                }
            }
            sum += values[i] * basis;
        }
        return sum;
    }
}
