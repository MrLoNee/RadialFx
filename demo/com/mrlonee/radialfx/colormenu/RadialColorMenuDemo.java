/**
 * Copyright 2013 (C) Mr LoNee - (Laurent NICOLAS) - www.mrlonee.com
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

package com.mrlonee.radialfx.colormenu;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class RadialColorMenuDemo extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    private Group container;
    private RadialColorMenu radialMenu;

    @Override
    public void start(final Stage primaryStage) throws Exception {
	container = new Group();
	final Scene scene = new Scene(container);
	scene.setFill(Color.WHITE);
	primaryStage.setResizable(false);
	primaryStage.setScene(scene);
	primaryStage.setWidth(450);
	primaryStage.setHeight(480);
	primaryStage.centerOnScreen();
	primaryStage.setTitle("Radial Color Menu Demo");
	primaryStage.show();

	final double itemInnerRadius = 60;
	final double itemRadius = 95;
	final double centerClosedRadius = 28;
	final double centerOpenedRadius = 40;

	radialMenu = new RadialColorMenu();

	radialMenu.setTranslateX(200);
	radialMenu.setTranslateY(200);
	container.getChildren().addAll(radialMenu);

	scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(final MouseEvent event) {
		if (event.isSecondaryButtonDown()) {
		    radialMenu.setTranslateX(event.getX());
		    radialMenu.setTranslateY(event.getY());
		}
	    }
	});

	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

	    @Override
	    public void handle(final KeyEvent event) {
		System.out.println(event);
		if (event.getCode() == KeyCode.F5) {
		    RadialColorMenuDemo.this.takeSnapshot(scene);
		}
	    }
	});

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
