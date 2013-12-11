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

	DemoUtil demoUtil = new DemoUtil();
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

	final Group demoControls = new Group(item, demoUtil);
	stage.setScene(new Scene(demoControls));
	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    @Override
	    public void handle(WindowEvent arg0) {
		System.exit(0);
	    }
	});

	stage.setWidth(600);
	stage.setHeight(600);
	stage.show();
    }
}
