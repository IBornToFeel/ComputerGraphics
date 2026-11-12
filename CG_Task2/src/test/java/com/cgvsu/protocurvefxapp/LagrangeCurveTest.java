package com.cgvsu.protocurvefxapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LagrangeCurveTest {

    private static final double EPS = 1e-9;

    @Test
    void curvePassesThroughAllNodes() {
        final double[] x = {10, 50, 120, 200, 260};
        final double[] y = {100, 20, 150, 40, 90};
        final LagrangeCurve curve = new LagrangeCurve(x, y);

        double t = 0;
        for (int i = 0; i < x.length; i++) {
            if (i > 0) {
                t += Math.hypot(x[i] - x[i - 1], y[i] - y[i - 1]);
            }
            final double[] p = curve.point(t);
            assertEquals(x[i], p[0], EPS);
            assertEquals(y[i], p[1], EPS);
        }
    }

    @Test
    void twoPointsGiveStraightLine() {
        final LagrangeCurve curve = new LagrangeCurve(new double[]{0, 10}, new double[]{0, 0});
        final double[] mid = curve.point(5);
        assertEquals(5, mid[0], EPS);
        assertEquals(0, mid[1], EPS);
    }

    @Test
    void collinearPointsStayOnLine() {
        final LagrangeCurve curve = new LagrangeCurve(new double[]{0, 10, 30}, new double[]{0, 10, 30});
        final double[] p = curve.point(curve.getTEnd() / 3);
        assertEquals(p[0], p[1], EPS);
    }

    @Test
    void curveCanBeNotAFunction() {

        final LagrangeCurve curve = new LagrangeCurve(new double[]{0, 10, 10, 0}, new double[]{0, 0, 10, 10});
        assertDoesNotThrow(() -> curve.point(curve.getTEnd() / 2));
    }

    @Test
    void duplicateNeighbourPointsAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new LagrangeCurve(new double[]{5, 5}, new double[]{7, 7}));
    }

    @Test
    void differentLengthsAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new LagrangeCurve(new double[]{1, 2}, new double[]{1}));
    }
}
