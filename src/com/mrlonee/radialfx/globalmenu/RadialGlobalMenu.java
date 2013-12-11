/**
 * RadialMenuGame
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import com.mrlonee.radialfx.core.RadialMenuItem;
import com.mrlonee.radialfx.core.RadialMenuItemBuilder;

public class RadialGlobalMenu extends Group {

    private static final Color BACK_GRADIENT_COLOR = Color.web("000000a0");
    private final Color BACK_SELECT_GRADIENT_COLOR = Color.web("00fffb97");

    private DoubleProperty length = new SimpleDoubleProperty(45);
    private DoubleProperty innerRadius = new SimpleDoubleProperty(150);
    private DoubleProperty radius = new SimpleDoubleProperty(300);
    private DoubleProperty offset = new SimpleDoubleProperty(6);
    private DoubleProperty widthProp = new SimpleDoubleProperty();
    private DoubleProperty heightProp = new SimpleDoubleProperty();

    private final List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();
    private final double initialAngle = -90 + 45.0 / 2.0;
    private Group itemsContainer = new Group();
    private Group backContainer;
    private ParallelTransition pTrans;

    public RadialGlobalMenu() {
	backContainer = new Group();

	addMenuItem("resources/icons/gemicon/PNG/64x64/row 1/13.png", null);
	addMenuItem("resources/icons/gemicon/PNG/64x64/row 1/6.png", null);
	addMenuItem("resources/icons/gemicon/PNG/64x64/row 4/6.png", null);
	addMenuItem("resources/icons/gemicon/PNG/64x64/row 4/3.png", null);
	addMenuItem("resources/icons/gemicon/PNG/64x64/row 6/14.png", null);
	addMenuItem("resources/icons/gemicon/PNG/64x64/row 7/7.png", null);
	final ChangeListener<Number> sizeChangeListener = new ChangeListener<Number>() {

	    @Override
	    public void changed(final ObservableValue<? extends Number> arg0,
		    final Number arg1, final Number arg2) {
		RadialGlobalMenu.this.computeBack();
	    }
	};
	widthProp.addListener(sizeChangeListener);
	heightProp.addListener(sizeChangeListener);

	computeItemsStartAngle();

	getChildren().setAll(backContainer, itemsContainer);
    }

    public DoubleProperty widthProperty() {
	return widthProp;

    }

    public DoubleProperty heightProperty() {
	return heightProp;

    }

    private void computeBack() {
	final Rectangle rect = new Rectangle();
	rect.setWidth(widthProp.get());
	rect.setHeight(heightProp.get());
	rect.setTranslateX(widthProp.get() / -2.0);
	rect.setTranslateY(heightProp.get() / -2.0);

	final RadialGradient radialGradient = new RadialGradient(0, 0,
		widthProp.get() / 2.0, heightProp.get() / 2.0,
		widthProp.get() / 2.0, false, CycleMethod.NO_CYCLE, new Stop(0,
			Color.TRANSPARENT), new Stop(1, BACK_GRADIENT_COLOR));

	rect.setFill(radialGradient);
	backContainer.getChildren().setAll(rect);
    }

    private void addMenuItem(final String iconPath,
	    final EventHandler<MouseEvent> eventHandler) {
	final RadialGradient backGradient = new RadialGradient(0, 0, 0, 0,
		radius.get(), false, CycleMethod.NO_CYCLE, new Stop(0,
			BACK_GRADIENT_COLOR), new Stop(1, Color.TRANSPARENT));
	final RadialGradient backSelectGradient = new RadialGradient(0, 0, 0,
		0, radius.get(), false, CycleMethod.NO_CYCLE, new Stop(0,
			BACK_SELECT_GRADIENT_COLOR), new Stop(1,
			Color.TRANSPARENT));

	final RadialMenuItem item = RadialMenuItemBuilder.create()
		.length(length).graphic(new Group(getImageView(iconPath)))
		.backgroundFill(backGradient)
		.backgroundMouseOnFill(backSelectGradient)
		.innerRadius(innerRadius).radius(radius).offset(offset)
		.clockwise(true).backgroundVisible(true).strokeVisible(false)
		.build();
	item.setOnMouseClicked(eventHandler);
	items.add(item);
	itemsContainer.getChildren().addAll(item);
    }

    private void computeItemsStartAngle() {
	double angleOffset = initialAngle;
	int i = 1;
	for (final RadialMenuItem item : items) {
	    item.setStartAngle(angleOffset);
	    angleOffset = angleOffset + item.getLength();
	    if (i % 3 == 0) {
		angleOffset += item.getLength();
	    }
	    i++;
	}
    }

    private ImageView getImageView(final String path) {
	ImageView imageView = null;
	try {
	    imageView = ImageViewBuilder.create()
		    .image(new Image(new FileInputStream(path))).build();
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	}
	assert (imageView != null);
	return imageView;

    }

    public void transitionVisible(final boolean visible) {
	double currentAnimX = -1;
	double currentAnimOpacity = -1;
	if (pTrans != null && pTrans.getStatus() == Status.RUNNING) {
	    pTrans.stop();
	    currentAnimX = items.get(0).getTranslateX();
	    currentAnimOpacity = backContainer.getOpacity();
	}
	if (visible) {
	    pTrans = new ParallelTransition();
	    int i = 0;
	    final double startX = currentAnimX != -1 ? currentAnimX : widthProp
		    .get() / 2.0;
	    for (final RadialMenuItem item : items) {
		TranslateTransition transition;
		if (i < 3) {
		    item.setTranslateX(startX);
		    transition = TranslateTransitionBuilder.create().node(item)
			    .fromX(startX).toX(0)
			    .duration(Duration.millis(200)).build();
		} else {
		    item.setTranslateX(-currentAnimX);
		    transition = TranslateTransitionBuilder.create().node(item)
			    .fromX(-startX).toX(0)
			    .duration(Duration.millis(200)).build();
		}
		i++;
		pTrans.getChildren().add(transition);
	    }
	    final double startOpacity = currentAnimOpacity != -1 ? currentAnimOpacity
		    : 0.0;
	    pTrans.getChildren().add(
		    createOpacityTransition(backContainer, startOpacity, 1.0));
	    setVisible(true);
	    pTrans.play();
	} else {
	    final ParallelTransition pTrans = new ParallelTransition();
	    int i = 0;
	    final double startX = currentAnimX != -1 ? currentAnimX : 0;
	    for (final RadialMenuItem item : items) {
		TranslateTransition transition;
		if (i < 3) {
		    transition = TranslateTransitionBuilder.create().node(item)
			    .fromX(startX).toX(widthProp.get() / 2.0)
			    .duration(Duration.millis(200)).build();
		} else {
		    transition = TranslateTransitionBuilder.create().node(item)
			    .fromX(-startX).toX(-widthProp.get() / 2.0)
			    .duration(Duration.millis(200)).build();
		}
		i++;
		pTrans.getChildren().add(transition);
	    }
	    final double startOpacity = currentAnimOpacity != -1 ? currentAnimOpacity
		    : 1.0;
	    pTrans.getChildren().add(
		    createOpacityTransition(backContainer, startOpacity, 0));

	    pTrans.setOnFinished(new EventHandler<ActionEvent>() {

		@Override
		public void handle(final ActionEvent arg0) {
		    setVisible(false);
		}

	    });
	    pTrans.play();

	}
    }

    private Transition createOpacityTransition(final Node node,
	    final double from, final double to) {
	final FadeTransition fadeIn = FadeTransitionBuilder.create().node(node)
		.fromValue(from).toValue(to).duration(Duration.millis(200))
		.build();
	backContainer.setOpacity(from);
	return fadeIn;

    }

}