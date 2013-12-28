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
package com.mrlonee.radialfx.moviemenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.mrlonee.radialfx.core.RadialMenuItem;
import com.mrlonee.radialfx.core.RadialMenuItemBuilder;

public class RadialMovieMenu extends Group {


    private double itemInnerRadius = 60;
    private double itemRadius = 95;
    private double centerClosedRadius = 28;
    private double centerOpenedRadius = 40;
    private String[] menus;

    private Circle center;
    private final List<RadialMenuItem> items;
    private final Group itemsGroup = new Group();
    private final Group textsGroup = new Group();
    private Circle fakeBackground;
    private Text centerText;
    private Circle radiusStroke;
    private Circle innerRadiusStroke;

    private final Color centerColor = Color.web("ffffff");
    private final Color itemColor = Color.web("ffffff80");
    private final Paint textColor = Color.web("000000a0");
    private Paint strokeColor = Color.web("c0c0c0");

    final Font textFont = Font.font(java.awt.Font.SANS_SERIF,FontWeight.NORMAL, 11);
    final Font textFontBold = Font.font(java.awt.Font.SANS_SERIF,FontWeight.BOLD, 11);
    final Font menuFont = Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD,12);

    private double animDuration = 350;
    private Animation openTransition;
    private final Map<RadialMenuItem, List<Text>> itemToTexts;

    public RadialMovieMenu(final String[] itemNames,
	    final double innerRadius,
	    final double radius, final double centerClosedRadius,
	    final double centerOpenedRadius) {

	menus = itemNames;
	itemInnerRadius = innerRadius;
	itemRadius = radius;
	this.centerClosedRadius = centerClosedRadius;
	this.centerOpenedRadius = centerOpenedRadius;

	itemToTexts = new HashMap<RadialMenuItem, List<Text>>();
	items = new ArrayList<RadialMenuItem>();

	double menuLetterNumber = 0;
	for (final String itemTitle : menus) {
	    menuLetterNumber += itemTitle.length();
	}
	double startAngle = 0;

	radiusStroke = CircleBuilder.create().radius(0).stroke(strokeColor)
		.fill(null).build();
	innerRadiusStroke = CircleBuilder.create().radius(0).fill(null)
		.stroke(strokeColor).build();

	itemsGroup.getChildren().addAll(radiusStroke, innerRadiusStroke);

	for (final String itemTitle : menus) {
	    final double length = 360.0 * (itemTitle.length() / menuLetterNumber);
	    final RadialMenuItem item = RadialMenuItemBuilder.create()
		    .backgroundFill(itemColor).innerRadius(0).radius(1)
		    .strokeVisible(false).offset(0).startAngle(startAngle)
		    .length(length).build();
	    items.add(item);
	    itemsGroup.getChildren().add(item);
	    final List<Text> texts = getTextNodes(itemTitle, startAngle);
	    textsGroup.getChildren().addAll(texts);
	    itemToTexts.put(item, texts);
	    final EventHandler<? super MouseEvent> itemEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(final MouseEvent event) {
		    if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
			for (final Text charText : texts) {
			    charText.setFill(Color.BLACK);
			    charText.setFont(textFontBold);
			}
		    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			for (final Text charText : texts) {
			    charText.setFill(textColor);
			    charText.setFont(textFont);
			}
		    }
		}
	    };
	    item.setOnMouseEntered(itemEventHandler);
	    item.setOnMouseExited(itemEventHandler);
	    item.setOnMouseClicked(itemEventHandler);

	    startAngle += length;
	}

	center = CircleBuilder.create().fill(centerColor)
		.radius(centerClosedRadius).stroke(strokeColor).centerX(0)
		.centerX(0).build();
	centerText = new Text("MENU");
	centerText.setFont(menuFont);
	centerText.setFontSmoothingType(FontSmoothingType.LCD);

	final StackPane stack = new StackPane();
	stack.getChildren().addAll(center, centerText);
	stack.translateXProperty().bind(stack.widthProperty().divide(-2.0));
	stack.translateYProperty().bind(stack.heightProperty().divide(-2.0));

	final EventHandler<? super MouseEvent> expansionEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
		    openTransition = createOpenTransition();
		    openTransition.play();
		} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
		    if (openTransition != null) {
			Duration startDuration = Duration.millis(animDuration);
			if (openTransition.getStatus() == Status.RUNNING) {
			    openTransition.stop();
			    startDuration = openTransition.getCurrentTime();
			}
			openTransition.setAutoReverse(true);
			openTransition.setCycleCount(2);
			openTransition.playFrom(startDuration);
		    }
		}
	    }
	};
	fakeBackground = CircleBuilder.create().fill(Color.TRANSPARENT)
		.radius(centerClosedRadius + 4).centerX(0).centerX(0).build();

	setOnMouseEntered(expansionEventHandler);
	setOnMouseExited(expansionEventHandler);

	getChildren().addAll(fakeBackground, itemsGroup, textsGroup, stack);
    }

    private Animation createOpenTransition() {
	final ParallelTransition openTransition = new ParallelTransition();
	final Animation centerTransition = new Timeline(new KeyFrame(
		Duration.ZERO, new KeyValue(center.radiusProperty(),
			centerClosedRadius), new KeyValue(
			fakeBackground.radiusProperty(),
			fakeBackground.getRadius())), new KeyFrame(
		Duration.millis(animDuration), new KeyValue(
			center.radiusProperty(), centerOpenedRadius),
		new KeyValue(fakeBackground.radiusProperty(), itemRadius + 4)));
	openTransition.getChildren().add(centerTransition);

	// Text font transition
	final DoubleProperty animValue = new SimpleDoubleProperty();
	final ChangeListener<? super Number> listener = new ChangeListener<Number>() {

	    @Override
	    public void changed(
		    final ObservableValue<? extends Number> obsValue,
		    final Number previousValue, final Number newValue) {
		final Font f = getTextFont(newValue.doubleValue());

		centerText.setFont(f);

	    }

	    Font[] fonts = new Font[] {
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD, 12),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD, 13),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD, 14),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD, 15),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.BOLD, 16) };

	    private Font getTextFont(final double newValue) {
		final int fontArrayIndex;
		if (newValue < 0.2) {
		    fontArrayIndex = 0;
		} else if (newValue < 0.4) {
		    fontArrayIndex = 1;
		} else if (newValue < 0.6) {
		    fontArrayIndex = 2;
		} else if (newValue < 0.8) {
		    fontArrayIndex = 3;
		} else {
		    fontArrayIndex = 4;
		}
		return fonts[fontArrayIndex];
	    }
	};
	animValue.addListener(listener);

	final Animation menuTextTransition = new Timeline(new KeyFrame(
		Duration.ZERO, new KeyValue(animValue, 0)), new KeyFrame(
		Duration.millis(animDuration), new KeyValue(animValue, 1.0)));

	openTransition.getChildren().add(menuTextTransition);

	final List<KeyValue> keyValueZero = new ArrayList<KeyValue>();
	final List<KeyValue> keyValueFinal = new ArrayList<KeyValue>();

	for (final RadialMenuItem item : items) {
	    keyValueZero.add(new KeyValue(item.innerRadiusProperty(),
		    centerClosedRadius));
	    keyValueZero.add(new KeyValue(item.radiusProperty(),
		    centerClosedRadius));

	    keyValueFinal.add(new KeyValue(item.innerRadiusProperty(),
		    itemInnerRadius));
	    keyValueFinal.add(new KeyValue(item.radiusProperty(), itemRadius));

	    final Animation textTransition = getTextOpenTransition(item);
	    openTransition.getChildren().add(textTransition);
	}

	keyValueZero.add(new KeyValue(radiusStroke.radiusProperty(),
		centerClosedRadius));
	keyValueZero.add(new KeyValue(innerRadiusStroke.radiusProperty(),
		centerClosedRadius));

	keyValueFinal.add(new KeyValue(radiusStroke.radiusProperty(),
		itemInnerRadius));
	keyValueFinal.add(new KeyValue(innerRadiusStroke.radiusProperty(),
		itemRadius));

	final Animation radiusTransition = new Timeline(new KeyFrame(
		Duration.ZERO, keyValueZero.toArray(new KeyValue[0])),
		new KeyFrame(Duration.millis(animDuration), keyValueFinal
			.toArray(new KeyValue[0])));

	openTransition.getChildren().add(radiusTransition);

	return openTransition;
    }

    public Animation getTextOpenTransition(final RadialMenuItem item) {
	final List<Text> texts = itemToTexts.get(item);
	final double textRadius = (itemInnerRadius + itemRadius) / 2.0;
	final double startAngle = item.getStartAngle();
	final double length = item.getLength() * 0.9;
	final double angleOffset = item.getLength() * 0.1;
	final double angleStep = (length) / (texts.size() + 1);

	for (final Text charText : texts) {
	    charText.setEffect(null);
	    charText.setVisible(true);
	}

	final DoubleProperty animValue = new SimpleDoubleProperty();
	final ChangeListener<? super Number> listener = new ChangeListener<Number>() {

	    @Override
	    public void changed(
		    final ObservableValue<? extends Number> obsValue,
		    final Number previousValue, final Number newValue) {
		final double textRotationOffset = 180;
		final double radius = centerClosedRadius
			+ (textRadius - centerClosedRadius)
			* newValue.doubleValue();

		double letterAngle = startAngle + angleStep + angleOffset
			+ ((1 - newValue.doubleValue()) * textRotationOffset);

		final Font f = getTextFont(newValue.doubleValue());

		for (final Text charText : texts) {
		    charText.setRotate(0);
		    charText.setFont(f);
		    final Bounds bounds = charText.getBoundsInParent();
		    final double lettertWidth = bounds.getWidth();
		    final double lettertHeight = bounds.getHeight();

		    final double currentX = xCenterOnCircle(letterAngle,
			    radius, lettertWidth);
		    final double currentY = yCenterLetterOnCircle(letterAngle,
			    radius, lettertHeight);
		    final double rotate = rotate(letterAngle);

		    charText.setTranslateX(currentX);
		    charText.setTranslateY(currentY);
		    charText.setRotate(rotate);

		    letterAngle += angleStep;
		}

	    }

	    Font[] fonts = new Font[] {
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 6),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 7),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 8),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 10),
		    Font.font(java.awt.Font.SANS_SERIF, FontWeight.NORMAL, 11) };

	    private Font getTextFont(final double newValue) {
		final int fontArrayIndex;
		if (newValue < 0.2) {
		    fontArrayIndex = 0;
		} else if (newValue < 0.4) {
		    fontArrayIndex = 1;
		} else if (newValue < 0.6) {
		    fontArrayIndex = 2;
		} else if (newValue < 0.8) {
		    fontArrayIndex = 3;
		} else {
		    fontArrayIndex = 4;
		}
		return fonts[fontArrayIndex];
	    }
	};
	animValue.addListener(listener);

	final Animation itemTransition = new Timeline(new KeyFrame(
		Duration.ZERO, new KeyValue(animValue, 0)), new KeyFrame(
		Duration.millis(animDuration), new KeyValue(animValue, 1.0)));
	itemTransition.setOnFinished(new EventHandler<ActionEvent>() {

	    boolean visible = false;

	    @Override
	    public void handle(final ActionEvent event) {
		for (final Text charText : texts) {
		    charText.setEffect(new Glow());
		    if (visible) {
			charText.setVisible(false);
		    }
		}
		visible = !visible;
	    }

	});
	return itemTransition;

    }

    private List<Text> getTextNodes(final String title, final double startAngle) {
	final List<Text> texts = new ArrayList<Text>();
	final char[] titleCharArray = title.toCharArray();

	for (int i = titleCharArray.length - 1; i >= 0; i--) {
	    final Text charText = new Text(
		    Character.toString(titleCharArray[i]));
	    charText.setFontSmoothingType(FontSmoothingType.LCD);
	    charText.setSmooth(true);
	    charText.setMouseTransparent(true);
	    charText.setFill(textColor);
	    charText.setBlendMode(BlendMode.COLOR_BURN);
	    charText.setFont(textFont);
	    texts.add(charText);
	}

	return texts;
    }

    private double xCenterOnCircle(final double angle, final double radius,
	    final double width) {
	return radius * Math.cos(Math.toRadians(angle)) - width / 2.0;
    }

    private double yCenterLetterOnCircle(final double angle,
	    final double radius, final double height) {
	return -radius * Math.sin(Math.toRadians(angle)) + height / 4.0;
    }

    private double rotate(final double angle) {
	final double rotate = 90 - angle;
	return rotate;
    }

}
