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
package com.mrlonee.radialfx.futurist;

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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;

public class FuturistDemo extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
	final Group container = new Group();
	final Scene scene = new Scene(container, Color.web("000033"));
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
	primaryStage.setWidth(400);
	primaryStage.setHeight(400);

	final Futurist nest = new Futurist();
	nest.setPrefSize(400, 400);
	nest.prefWidthProperty().bind(scene.widthProperty());
	nest.prefHeightProperty().bind(scene.heightProperty());
	container.getChildren().add(nest);

	primaryStage.show();
    }

    int snapshotCounter = 0;

    private void takeSnapshot(final Scene scene) {
	// Take snapshot of the scene
	final WritableImage writableImage = scene.snapshot(null);

	// Write snapshot to file system as a .png image
	final File outFile = new File("snapshot/"+getClass().getSimpleName().toLowerCase()+"-"
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
