package com.mrlonee.radialfx.core;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

public class RadialMenuItemBuilder<B extends RadialMenuItemBuilder<B>> {

    private RadialMenuItem item;

    protected RadialMenuItemBuilder() {
	this.item = new RadialMenuItem();
    }

    public static RadialMenuItemBuilder create() {
	return new RadialMenuItemBuilder();
    }

    public B startAngle(final double startAngle) {
	this.item.setStartAngle(startAngle);
	return (B) this;
    }

    public B startAngle(final DoubleProperty startAngle) {
	this.item.startAngle.bind(startAngle);
	return (B) this;
    }

    public B length(final double length) {
	this.item.setLength(length);
	return (B) this;
    }

    public B length(final DoubleProperty length) {
	this.item.lengthProperty().bind(length);
	return (B) this;
    }

    public B backgroundFill(final Paint fill) {
	this.item.setBackgroundFill(fill);
	return (B) this;
    }

    public B backgroundMouseOnFill(final Paint fill) {
	this.item.setBackgroundMouseOnFill(fill);
	return (B) this;
    }

    public B backgroundVisible(final boolean visible) {
	this.item.setBackgroundVisible(visible);
	return (B) this;
    }

    public B clockwise(final boolean clockwise) {
	this.item.setClockwise(clockwise);
	return (B) this;
    }

    public B graphic(final Node graphic) {
	this.item.setGraphic(graphic);
	return (B) this;
    }

    public B radius(final double radius) {
	this.item.setRadius(radius);
	return (B) this;
    }

    public B radius(final DoubleProperty radius) {
	this.item.radiusProperty().bind(radius);
	return (B) this;
    }

    public B innerRadius(final double radius) {
	this.item.setInnerRadius(radius);
	return (B) this;
    }

    public B innerRadius(final DoubleProperty radius) {
	this.item.innerRadiusProperty().bind(radius);
	return (B) this;
    }

    public B offset(final double offset) {
	this.item.setOffset(offset);
	return (B) this;
    }

    public B offset(final DoubleProperty radius) {
	this.item.offsetProperty().bind(radius);
	return (B) this;
    }

    public B strokeFill(final Paint fill) {
	this.item.setStrokeFill(fill);
	return (B) this;
    }

    public B strokeMouseOnFill(final Paint fill) {
	this.item.setStrokeMouseOnFill(fill);
	return (B) this;
    }

    public B strokeVisible(final boolean visible) {
	this.item.setStrokeVisible(visible);
	return (B) this;
    }

    public RadialMenuItem build() {
	return this.item;
    }
}
