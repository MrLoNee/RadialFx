/**
 * Copyright 2014 (C) Mr LoNee - (Laurent NICOLAS) - www.mrlonee.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */
package com.mrlonee.radialfx.thermostat;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.imageio.ImageIO;

public class NestNoCssDemo extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
	final Group container = new Group();
	final Scene scene = new Scene(container, Color.TRANSPARENT);
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent event) {
		System.exit(0);
	    }
	});
	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

	    @Override
	    public void handle(final KeyEvent event) {
		System.out.println(event);
		if (event.getCode() == KeyCode.F5) {
		    takeSnapshot(scene);
		}
	    }
	});
	primaryStage.setScene(scene);
	primaryStage.setWidth(500);
	primaryStage.setHeight(550);

	final NestNoCss nest = new NestNoCss();
	nest.setPrefSize(400, 400);
	nest.prefWidthProperty().bind(scene.widthProperty());
	nest.prefHeightProperty().bind(scene.heightProperty());
	container.getChildren().add(nest);

	nest.setCurrentTemperature(28);
	nest.setReachTargetTemperatureDelay(25);

	final DoubleBinding reachTargetDoubleBinding = new DoubleBinding() {
	    {
		super.bind(nest.currentTemperatureProperty(),
			nest.targetTemperatureProperty());
	    }

	    @Override
	    protected double computeValue() {
		final double diff = Math.abs(nest.getCurrentTemperature()
			- nest.getTargetTemperature());
		if (diff < 0.5) {
		    return 0;
		}
		if (diff < 1) {
		    return 1;
		}
		if (diff < 2) {
		    return 2;
		} else if (diff < 5) {
		    return 5;
		} else {
		    final int offset = (int) diff / 5;
		    return offset * 5;

		}
	    }
	};

	nest.reachTargetTemperatureDelay().bind(reachTargetDoubleBinding);

	final ScheduledExecutorService scheduledExecutorService = Executors
		.newSingleThreadScheduledExecutor();

	scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

	    double lastTargetTemp = nest.getTargetTemperature();

	    @Override
	    public void run() {
		double newTarget = lastTargetTemp + 25.0 * Math.random();
		newTarget = newTarget % 50;
		final Timeline timeline = new Timeline(
			new KeyFrame(Duration.ZERO,
				new KeyValue(nest.targetTemperatureProperty(), lastTargetTemp),
				new KeyValue(nest.currentTemperatureProperty(), nest.getCurrentTemperature())),
			new KeyFrame(Duration.millis(1600),
				new KeyValue(nest.targetTemperatureProperty(), newTarget),
				new KeyValue(nest.currentTemperatureProperty(), nest.getCurrentTemperature())),
			new KeyFrame(Duration.millis(7000),
				new KeyValue(nest.currentTemperatureProperty(), newTarget),
				new KeyValue(nest.targetTemperatureProperty(), newTarget)));

		timeline.play();

		lastTargetTemp = newTarget;
	    }

	}, 1000, 8000, TimeUnit.MILLISECONDS);

	primaryStage.show();
    }

    int snapshotCounter = 0;

    private void takeSnapshot(final Scene scene) {
	// Take snapshot of the scene
	final WritableImage writableImage = scene.snapshot(null);

	// Write snapshot to file system as a .png image
	final File outFile = new File("snapshot/radialmenu-snapshot-"
		+ snapshotCounter + ".png");
	outFile.getParentFile().mkdirs();
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png",
		    outFile);
	} catch (final IOException ex) {
	    System.out.println(ex.getMessage());
	}

	snapshotCounter++;
    }

}
