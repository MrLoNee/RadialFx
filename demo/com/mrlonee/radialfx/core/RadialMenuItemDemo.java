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
package com.mrlonee.radialfx.core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.mrlonee.radialfx.demoutil.DemoUtil;

public class RadialMenuItemDemo extends Application {

    public static void main(final String[] args) {
	Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
	final RadialMenuItem item = RadialMenuItemBuilder.create().build();
	item.setTranslateX(400);
	item.setTranslateY(300);

	final DemoUtil demoUtil = new DemoUtil();
	demoUtil.addAngleControl("StartAngle", item.startAngleProperty());
	demoUtil.addAngleControl("Length", item.lengthProperty());
	demoUtil.addRadiusControl("Inner Radius", item.innerRadiusProperty());
	demoUtil.addRadiusControl("Radius", item.radiusProperty());
	demoUtil.addRadiusControl("Offset", item.offsetProperty());
	demoUtil.addColorControl("Background", item.backgroundFillProperty());
	demoUtil.addColorControl("BackgroundMouseOn",
		item.backgroundMouseOnFillProperty());
	demoUtil.addColorControl("Stroke", item.strokeFillProperty());
	demoUtil.addColorControl("StrokeMouseOn",
		item.strokeMouseOnFillProperty());
	demoUtil.addBooleanControl("Clockwise", item.clockwiseProperty());
	demoUtil.addBooleanControl("BackgroundVisible",
		item.backgroundVisibleProperty());
	demoUtil.addBooleanControl("StrokeVisible",
		item.strokeVisibleProperty());
	demoUtil.addGraphicControl("Graphic",
		item.graphicProperty());

	final Group demoControls = new Group(item, demoUtil);
	stage.setScene(new Scene(demoControls));
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(final WindowEvent arg0) {
		System.exit(0);
	    }
	});

	stage.setWidth(600);
	stage.setHeight(600);
	stage.show();
    }
}
