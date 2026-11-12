package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ProtoCurveController {
    @FXML private AnchorPane anchorPane;
    @FXML private Canvas canvas;

    private final ArrayList<Point2D> points = new ArrayList<>();

    private static final double POINT_RADIUS = 3.0;

    private static final double CURVE_STEP = 1.0;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((obs, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((obs, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handlePrimaryClick(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                handleSecondaryClick();
            }
        });
        redraw();
    }

    private void handlePrimaryClick(MouseEvent event) {
        Point2D clickPoint = new Point2D(event.getX(), event.getY());


        if (!points.isEmpty() && points.get(points.size() - 1).distance(clickPoint) < 1.0) {
            return;
        }
        points.add(clickPoint);
        redraw();
    }


    private void handleSecondaryClick() {
        points.clear();
        redraw();
    }

    private void redraw() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (points.size() >= 2) {
            drawLagrangeCurve(g);
        }

        g.setFill(Color.BLACK);
        for (Point2D point : points) {
            g.fillOval(point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
    }

    private void drawLagrangeCurve(GraphicsContext g) {
        double[] xs = new double[points.size()];
        double[] ys = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xs[i] = points.get(i).getX();
            ys[i] = points.get(i).getY();
        }

        LagrangeCurve curve = new LagrangeCurve(xs, ys);

        double tStart = curve.getTStart();
        double tEnd = curve.getTEnd();
        int steps = Math.max(1, (int) Math.ceil((tEnd - tStart) / CURVE_STEP));

        g.setStroke(Color.BLUE);
        g.setLineWidth(1.5);

        double[] previous = curve.point(tStart);
        for (int k = 1; k <= steps; k++) {
            double t = tStart + (tEnd - tStart) * k / steps;
            double[] current = curve.point(t);
            g.strokeLine(previous[0], previous[1], current[0], current[1]);
            previous = current;
        }
    }
}
