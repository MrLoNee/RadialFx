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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Radial Menu Item that is the base item for radial menu.
 *
 * @author Mr LoNee - (Laurent NICOLAS) - www.mrlonee.com
 */
public class RadialMenuItem extends Group implements ChangeListener<Object> {

    /*************************************************************************************
     * Property part
     *************************************************************************************/
    protected DoubleProperty startAngle = new SimpleDoubleProperty(0);

    protected DoubleProperty length = new SimpleDoubleProperty(45);

    protected DoubleProperty innerRadius = new SimpleDoubleProperty(50);

    protected DoubleProperty radius = new SimpleDoubleProperty(100);

    protected DoubleProperty offset = new SimpleDoubleProperty(5);

    protected ObjectProperty<Paint> backgroundMouseOnFill = new SimpleObjectProperty<Paint>(
	    Color.LIGHTGRAY);

    protected ObjectProperty<Paint> backgroundFill = new SimpleObjectProperty<Paint>(
	    Color.GRAY);

    protected BooleanProperty backgroundVisible = new SimpleBooleanProperty(
	    true);

    protected BooleanProperty strokeVisible = new SimpleBooleanProperty(true);

    protected ObjectProperty<Paint> strokeFill = new SimpleObjectProperty<Paint>(
	    Color.GRAY);

    protected ObjectProperty<Paint> strokeMouseOnFill = new SimpleObjectProperty<Paint>(
	    Color.LIGHTGRAY);

    protected BooleanProperty clockwise = new SimpleBooleanProperty(false);

    protected ObjectProperty<Node> graphic = new SimpleObjectProperty<Node>();

    /*****************************************************************************
     * Graphic Part
     *****************************************************************************/
    protected MoveTo moveTo;

    protected ArcTo arcToInner;

    protected ArcTo arcTo;

    protected LineTo lineTo;

    protected LineTo lineTo2;

    protected Path path;

    protected Group graphicContainer = new Group();

    protected String text;

    protected double innerStartX;

    protected double innerStartY;

    protected double innerEndX;

    protected double innerEndY;

    protected boolean innerSweep;

    protected double startX;

    protected double startY;

    protected double endX;

    protected double endY;

    protected boolean sweep;

    protected double graphicX;

    protected double graphicY;

    protected double translateX;

    protected double translateY;

    protected boolean mouseOn = false;

    public RadialMenuItem() {
	length.addListener(this);
	innerRadius.addListener(this);
	radius.addListener(this);
	offset.addListener(this);
	backgroundVisible.addListener(this);
	strokeVisible.addListener(this);
	clockwise.addListener(this);
	backgroundFill.addListener(this);
	strokeFill.addListener(this);
	backgroundMouseOnFill.addListener(this);
	strokeMouseOnFill.addListener(this);
	startAngle.addListener(this);
	graphic.addListener(this);

	path = new Path();
	moveTo = new MoveTo();
	arcToInner = new ArcTo();
	arcTo = new ArcTo();
	lineTo = new LineTo();
	lineTo2 = new LineTo();

	path.getElements().add(moveTo);
	path.getElements().add(arcToInner);
	path.getElements().add(lineTo);
	path.getElements().add(arcTo);
	path.getElements().add(lineTo2);

	getChildren().setAll(path, graphicContainer);

	setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent arg0) {
		mouseOn = true;
		RadialMenuItem.this.redraw();
	    }
	});

	setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent arg0) {
		mouseOn = false;
		RadialMenuItem.this.redraw();
	    }
	});

	redraw();
    }

    public DoubleProperty startAngleProperty() {
	return startAngle;
    }

    public void setStartAngle(final double angle) {
	startAngle.set(angle);
    }

    public double getStartAngle() {
	return startAngle.get();
    }

    public DoubleProperty lengthProperty() {
	return length;
    }

    public void setLength(final double length) {
	this.length.set(length);
    }

    public double getLength() {
	return length.get();
    }

    public DoubleProperty innerRadiusProperty() {
	return innerRadius;
    }

    public void setInnerRadius(final double radius) {
	innerRadius.set(radius);
    }

    public double getInnerRadius() {
	return innerRadius.get();
    }

    public DoubleProperty radiusProperty() {
	return radius;
    }

    public void setRadius(final double radius) {
	this.radius.set(radius);
    }

    public double getRadius() {
	return radius.get();
    }

    public DoubleProperty offsetProperty() {
	return offset;
    }

    public void setOffset(final double offset) {
	this.offset.set(offset);
    }

    public double getOffset() {
	return offset.get();
    }

    public ObjectProperty<Paint> backgroundFillProperty() {
	return backgroundFill;
    }

    public void setBackgroundFill(final Paint fill) {
	backgroundFill.set(fill);
    }

    public Paint getBackgroundFill() {
	return backgroundFill.get();
    }

    public ObjectProperty<Paint> strokeFillProperty() {
	return strokeFill;
    }

    public void setStrokeFill(final Paint fill) {
	strokeFill.set(fill);
    }

    public Paint getStrokeFill() {
	return strokeFill.get();
    }

    public ObjectProperty<Paint> backgroundMouseOnFillProperty() {
	return backgroundMouseOnFill;
    }

    public void setBackgroundMouseOnFill(final Paint fill) {
	backgroundMouseOnFill.set(fill);
    }

    public Paint getBackgroundMouseOnFill() {
	return backgroundMouseOnFill.get();
    }

    public ObjectProperty<Paint> strokeMouseOnFillProperty() {
	return strokeMouseOnFill;
    }

    public void setStrokeMouseOnFill(final Paint fill) {
	strokeMouseOnFill.set(fill);
    }

    public Paint getStrokeMouseOnFill() {
	return strokeMouseOnFill.get();
    }

    public BooleanProperty clockwiseProperty() {
	return clockwise;
    }

    public void setClockwise(final boolean clockwise) {
	this.clockwise.set(clockwise);
    }

    public boolean isClockwise() {
	return clockwise.get();
    }

    public BooleanProperty strokeVisibleProperty() {
	return strokeVisible;
    }

    public void setStrokeVisible(final boolean visible) {
	strokeVisible.set(visible);
    }

    public boolean isStrokeVisible() {
	return strokeVisible.get();
    }

    public BooleanProperty backgroundVisibleProperty() {
	return backgroundVisible;
    }

    public void setBackgroundVisible(final boolean visible) {
	backgroundVisible.set(visible);
    }

    public boolean isBackgroundVisible() {
	return strokeVisible.get();
    }

    public ObjectProperty<Node> graphicProperty() {
	return graphic;
    }

    public void setGraphic(final Node graphic) {
	this.graphic.set(graphic);
    }

    public Node getGraphic() {
	return graphic.get();
    }

    protected void redraw() {

	if (graphic.get() != null) {
	    graphicContainer.getChildren().setAll(graphic.get());
	} else {
	    graphicContainer.getChildren().clear();
	}

	path.setFill(backgroundVisible.get() ? (mouseOn
		&& backgroundMouseOnFill.get() != null ? backgroundMouseOnFill
		.get() : backgroundFill.get()) : null);
	path.setStroke(strokeVisible.get() ? (mouseOn
		&& strokeMouseOnFill.get() != null ? strokeMouseOnFill.get()
		: strokeFill.get()) : null);

	path.setFillRule(FillRule.EVEN_ODD);

	computeCoordinates();

	updateCoordinates();

    }

    protected void updateCoordinates() {
	final double innerRadiusValue = innerRadius.get();
	final double radiusValue = radius.get();

	moveTo.setX(innerStartX + translateX);
	moveTo.setY(innerStartY + translateY);

	arcToInner.setX(innerEndX + translateX);
	arcToInner.setY(innerEndY + translateY);
	arcToInner.setSweepFlag(innerSweep);
	arcToInner.setRadiusX(innerRadiusValue);
	arcToInner.setRadiusY(innerRadiusValue);

	lineTo.setX(startX + translateX);
	lineTo.setY(startY + translateY);

	arcTo.setX(endX + translateX);
	arcTo.setY(endY + translateY);
	arcTo.setSweepFlag(sweep);

	arcTo.setRadiusX(radiusValue);
	arcTo.setRadiusY(radiusValue);

	lineTo2.setX(innerStartX + translateX);
	lineTo2.setY(innerStartY + translateY);

	if (graphic.get() != null) {
	    graphic.get().setTranslateX(graphicX + translateX);
	    graphic.get().setTranslateY(graphicY + translateY);
	}

	// this.translateXProperty().set(this.translateX);
	// this.translateYProperty().set(this.translateY);
    }

    protected void computeCoordinates() {
	final double innerRadiusValue = innerRadius.get();
	final double startAngleValue = startAngle.get();
	final double length = this.length.get();
	final double graphicAngle = startAngleValue + (length / 2.0);
	final double radiusValue = radius.get();

	final double graphicRadius = innerRadiusValue
		+ (radiusValue - innerRadiusValue) / 2.0;

	final double offsetValue = offset.get();

	if (!clockwise.get()) {
	    innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    innerStartY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + length));
	    innerEndY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + length));

	    innerSweep = false;

	    startX = radiusValue
		    * Math.cos(Math.toRadians(startAngleValue + length));
	    startY = -radiusValue
		    * Math.sin(Math.toRadians(startAngleValue + length));
	    endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    endY = -radiusValue * Math.sin(Math.toRadians(startAngleValue));

	    sweep = true;

	    if (graphic.get() != null) {
		graphicX = graphicRadius
			* Math.cos(Math.toRadians(graphicAngle))
			- graphic.get().getBoundsInParent().getWidth() / 2.0;
		graphicY = -graphicRadius
			* Math.sin(Math.toRadians(graphicAngle))
			- graphic.get().getBoundsInParent().getHeight() / 2.0;

	    }
	    translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue + (length / 2.0)));
	    translateY = -offsetValue
		    * Math.sin(Math.toRadians(startAngleValue + (length / 2.0)));

	} else if (clockwise.get()) {
	    innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    innerStartY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + length));
	    innerEndY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + length));

	    innerSweep = true;

	    startX = radiusValue
		    * Math.cos(Math.toRadians(startAngleValue + length));
	    startY = radiusValue
		    * Math.sin(Math.toRadians(startAngleValue + length));
	    endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    endY = radiusValue * Math.sin(Math.toRadians(startAngleValue));

	    sweep = false;

	    if (graphic.get() != null) {
		graphicX = graphicRadius
			* Math.cos(Math.toRadians(graphicAngle))
			- graphic.get().getBoundsInParent().getWidth() / 2.0;
		graphicY = graphicRadius
			* Math.sin(Math.toRadians(graphicAngle))
			- graphic.get().getBoundsInParent().getHeight() / 2.0;

	    }

	    translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue + (length / 2.0)));
	    translateY = offsetValue
		    * Math.sin(Math.toRadians(startAngleValue + (length / 2.0)));
	}

    }

    @Override
    public void changed(
	    final ObservableValue<? extends Object> observableValue,
	    final Object previousValue, final Object newValue) {
	redraw();
    }

}
