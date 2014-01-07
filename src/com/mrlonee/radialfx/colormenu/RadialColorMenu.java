package com.mrlonee.radialfx.colormenu;

import java.awt.geom.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineToBuilder;
import javafx.scene.shape.MoveToBuilder;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.util.Duration;

import com.mrlonee.radialfx.core.RadialMenuItem;
import com.mrlonee.radialfx.core.RadialMenuItemBuilder;

public class RadialColorMenu extends Group {

    private ItemExtEventHandler itemExtMouseHandler;
    private ObjectProperty<Paint> selectedColor;
    private final double minOffset = 5;

    public RadialColorMenu() {
	selectedColor = new SimpleObjectProperty<Paint>(Color.BLACK);
	itemExtMouseHandler = new ItemExtEventHandler();

	final Color[] colors = new Color[] { Color.BLACK, Color.web("00275b"),
		Color.web("008021"), Color.web("8e0000"), Color.web("ff8800") };

	int i = 0;
	for (final Color color : colors) {

	    addColorItem(color, i * 360d / colors.length, 360d / colors.length);

	    i++;
	}

	final Circle center = new Circle();
	center.fillProperty().bind(selectedColor);
	center.setRadius(40);
	center.setCenterX(0);
	center.setCenterY(0);

	getChildren().add(center);
    }

    private void addColorItem(final Color color, final double startAngle,
	    final double length) {

	final RadialMenuItem colorItem = RadialMenuItemBuilder.create()
		.startAngle(startAngle).length(length).backgroundFill(color)
		.backgroundMouseOnFill(color).strokeVisible(false).offset(minOffset)
		.innerRadius(60).radius(140).build();
	getChildren().add(colorItem);

	final Path extGraphic = PathBuilder
		.create()
		.elements(MoveToBuilder.create().x(-2.5).y(0).build(),
			LineToBuilder.create().x(-2.5).y(5).build(),
			LineToBuilder.create().x(2.5).y(0).build(),
			LineToBuilder.create().x(-2.5).y(-5).build(),
			LineToBuilder.create().x(-2.5).y(0).build())
		.fill(Color.WHITE)
		.stroke(null)
		.rotate(-colorItem.startAngleProperty().get()
			- colorItem.lengthProperty().get() / 2.0)

		.build();
	final RadialMenuItem colorItemExt = RadialMenuItemBuilder.create()
		.startAngle(startAngle).length(length).backgroundFill(color)
		.backgroundMouseOnFill(color).strokeVisible(false)
		.innerRadius(142).radius(180).graphic(extGraphic).offset(minOffset)
		.build();

	getChildren().add(colorItemExt);
	colorItemExt.setOnMouseClicked(itemExtMouseHandler);

	final Paint selectColor = Color.GRAY;
	final double colorOffset = 6;
	final RadialMenuItem colorItemSel = RadialMenuItemBuilder.create()
		.startAngle(startAngle + colorOffset).offset(minOffset)
		.length(length - colorOffset * 2).backgroundFill(selectColor)
		.strokeVisible(false).innerRadius(132).radius(134).build();
	colorItemSel.setOpacity(0.0);
	getChildren().add(colorItemSel);

	final EventHandler<MouseEvent> mouseHandler = new ItemOnEventHandler(
		colorItemSel, colorItem, colorItemExt);
	colorItem.setOnMouseEntered(mouseHandler);
	colorItem.setOnMouseExited(mouseHandler);
	colorItem.setOnMouseMoved(mouseHandler);
    }

    private final class ItemExtEventHandler implements EventHandler<MouseEvent> {

	RadialMenuItem selected = null;

	@Override
	public void handle(final MouseEvent event) {
	    final RadialMenuItem item = (RadialMenuItem) event.getSource();

	    if (item == selected) {
		// do close
	    } else if (selected != null) {
		// do replace
	    } else {
		// do open
	    }
	    selected = item;
	}
    }

    private final class ItemOnEventHandler implements EventHandler<MouseEvent> {

	private Timeline outTransition;
	private final RadialMenuItem colorItemSel;
	private final RadialMenuItem colorItem;
	private final RadialMenuItem colorItemExt;
	double offset = 0;
	boolean enteredByInner = false;

	private ItemOnEventHandler(final RadialMenuItem colorItemSel,
		final RadialMenuItem colorItem,
		final RadialMenuItem colorItemExt) {
	    this.colorItemSel = colorItemSel;
	    this.colorItem = colorItem;
	    this.colorItemExt = colorItemExt;
	}

	@Override
	public void handle(final MouseEvent event) {
	    if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
		final double distanceToCenter = Point2D.distance(event.getX(),
			event.getY(), 0, 0);

		if (Math.abs(colorItem.getInnerRadius()+colorItem.getOffset() - distanceToCenter) < 20) {
		    // Entering by the center of the menu
		    enteredByInner = true;
		    colorItemSel.setOpacity(1.0);
		} else {
		    enteredByInner = false;
		}
	    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
		if (enteredByInner) {
		    outTransition = new Timeline(
			    new KeyFrame(Duration.ZERO, new KeyValue(colorItem
				    .offsetProperty(), offset), new KeyValue(
				    colorItemExt.offsetProperty(), offset),
				    new KeyValue(colorItemSel.offsetProperty(),
					    offset), new KeyValue(colorItemSel
					    .opacityProperty(), 1.0)),
			    new KeyFrame(Duration.millis(180), new KeyValue(
				    colorItem.offsetProperty(), minOffset),
				    new KeyValue(colorItemExt.offsetProperty(),
					    minOffset), new KeyValue(colorItemSel
					    .opacityProperty(), 0),
				    new KeyValue(colorItemSel.offsetProperty(),
					    minOffset)));
		    outTransition.playFromStart();

		    final double distanceToCenter = Point2D.distance(
			    event.getX(), event.getY(), 0, 0);
		    if (Math.abs(colorItem.getRadius()+colorItem.getOffset() - distanceToCenter) < 20) {
			// Exiting by the external item of the menu
			selectedColor.set(colorItem.getBackgroundFill());
		    }
		}
		enteredByInner = false;
	    } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
		if (enteredByInner) {
		    final double distanceToCenter = Point2D.distance(
			    event.getX(), event.getY(), 0, 0);

		    offset = 0.4 * (distanceToCenter - colorItem
			    .getInnerRadius());
		    colorItem.setOffset(offset);
		    colorItemExt.setOffset(offset);
		    colorItemSel.setOffset(offset);
		}
	    }
	    ;
	}

    }

    public ObjectProperty<Paint> selectedColorProperty() {
	return selectedColor;
    }

    public Paint getSelectedColor() {
	return selectedColor.get();
    }

}
