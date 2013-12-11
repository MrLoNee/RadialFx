/**
 * RadialMenuEnhancedDemo
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

package com.mrlonee.radialfx.globalmenu;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class RadialGlobalMenuDemo extends Application {

    public static void main(final String[] args) {
	launch(args);
    }

    private Group container;
    private RadialGlobalMenu radialMenu;
    private boolean visible = false;

    @Override
    public void start(final Stage primaryStage) throws Exception {
	container = new Group();
	final Scene scene = new Scene(container, Color.TRANSPARENT);
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent event) {
		System.exit(0);
	    }
	});
	primaryStage.initStyle(StageStyle.TRANSPARENT);
	primaryStage.setResizable(false);
	primaryStage.setScene(scene);
	primaryStage.centerOnScreen();
	final Dimension screenSize = Toolkit.getDefaultToolkit()
		.getScreenSize();
	primaryStage.setWidth(screenSize.getWidth());
	primaryStage.setHeight(screenSize.getHeight());
	primaryStage.toFront();

	radialMenu = new RadialGlobalMenu();

	radialMenu.translateXProperty().bind(scene.widthProperty().divide(2.0));
	radialMenu.translateYProperty()
		.bind(scene.heightProperty().divide(2.0));
	radialMenu.widthProperty().bind(scene.widthProperty());
	radialMenu.heightProperty().bind(scene.heightProperty());
	radialMenu.setVisible(visible);

	final ToggleButton showHideButton = new ToggleButton("Toggle display");
	showHideButton.setOnAction(new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(final ActionEvent event) {
		if (visible) {
		    setRadialMenuVisible(false);
		} else {
		    setRadialMenuVisible(true);
		}
		visible = !visible;
	    }
	});
	showHideButton.setTranslateX(20);
	showHideButton.setTranslateY(20);

	final ToggleButton exitButton = new ToggleButton("Exit");
	exitButton.setOnAction(new EventHandler<ActionEvent>() {

	    @Override
	    public void handle(final ActionEvent event) {
		System.exit(0);
	    }
	});
	exitButton.setTranslateX(50);
	exitButton.setTranslateY(20);
	final HBox box = new HBox();
	box.getChildren().addAll(showHideButton, exitButton);
	container.getChildren().addAll(radialMenu, box);

	primaryStage.show();

    }

    private void setRadialMenuVisible(final boolean visible) {
	radialMenu.transitionVisible(visible);
    }
}
