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
package com.mrlonee.radialfx.thermostat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.mrlonee.radialfx.core.RadialMenuItem;
import com.mrlonee.radialfx.core.RadialMenuItemBuilder;

/**
 * Nest Thermostat reproduction in JavaFX.
 *
 * @author MrLoNee
 */
public class NestNoCss extends Region {

    private Circle frame;
    private Circle frame1;
    private Circle frame2;
    private Circle frame3;
    private SVGPath line;
    private Ellipse lightEffect;
    private SVGPath line1;
    private List<RadialMenuItem> arcs = new ArrayList<RadialMenuItem>();
    private RadialMenuItem targetTemperatureTick;
    private RadialMenuItem currentTemperatureTick;
    private Text currentTemperatureText;
    private Text reachTargetTemperatureDelayText;

    private InvalidationListener listener = new InternalListener();
    private final static double initialSize = 533.919;
    private static final Paint FRAME_FILL = new LinearGradient(0.271, 0.065,
	    0.7735, 0.91, true, CycleMethod.NO_CYCLE, new Stop(0.0,
		    Color.web("#e8e8e8")), new Stop(0.5, Color.web("#c6c6c6")),
	    new Stop(1.0, Color.web("#a6a6a6")));
    private static final Paint FRAME1_FILL = new LinearGradient(0.271, 0.065,
	    0.7735, 0.91, true, CycleMethod.NO_CYCLE, new Stop(0.0,
		    Color.web("#fdfdfd")), new Stop(0.5, Color.web("#747474")),
	    new Stop(1.0, Color.web("#a8a8a8")));
    private static final Paint FRAME1_STROKE = new LinearGradient(0.271, 0.065,
	    0.7735, 0.91, true, CycleMethod.NO_CYCLE, new Stop(0.0,
		    Color.web("#d5d5d5")), new Stop(0.5, Color.web("#747474")),
	    new Stop(1.0, Color.web("#8f8f8f")));
    private static final Paint FRAME2_FILL =new LinearGradient(0.271, 0.065,
	    0.7735, 0.91, true, CycleMethod.NO_CYCLE, new Stop(0.0,
		    Color.web("#1c1715")), new Stop(0.5, Color.web("#181818")),
	    new Stop(1.0, Color.web("#3a3a3a")));
    private static final Paint FRAME2_STROKE = Color.web("#212121");
    private double size;
    private double frame1Ratio = 0.98;
    private double frame2Ratio = 0.90;
    private double frame3Ratio = 0.54;
    private double tickRatio = 0.48;
    private double tickRatioOffset = 0.1;
    private double targetTickRatioOffset = 0.15;
    private double temperatureTextYRatio = 0.5;
    private double lightEffectXRatio = 0.2;
    private double lightEffectYRatio = 0.13;
    private double lightEffectXRadiusRatio = 0.3;
    private double lightEffectYRadiusRatio = 0.17;
    private double lightEffectRotate = -45;
    private double nbArcs = 110;
    private double arcEmptyLength = 45;
    private double shadowXOffset = 0.1;
    private double shadowYOffset = 0.2;
    private double shadowSizeOffset = 0.25;

    private Color CURRENT_TICK_COLOR = Color.web("#ffffffff");
    private Color TARGET_TICK_COLOR = Color.web("#ffffffff");
    private Color TICK_COLOR = Color.web("#ffffffa0");

    private ObjectProperty<NumberFormat> numberFormatProperty;
    private DoubleProperty currentTemperatureProperty;
    private DoubleProperty targetTemperatureProperty;
    private DoubleProperty maxTemperatureProperty;
    private DoubleProperty minTemperatureProperty;
    private IntegerProperty reachTargetTemperatureDelayProperty;
    private DropShadow shadow;
    private ChangeListener<? super Number> targetTemperatureTickListener;

    public double getTargetTemperature() {
	return targetTemperatureProperty.get();
    }

    public void setTargetTemperature(final double value) {
	targetTemperatureProperty.set(value);
    }

    public DoubleProperty targetTemperatureProperty() {
	return targetTemperatureProperty;
    }

    public double getReachTargetTemperatureDelay() {
	return reachTargetTemperatureDelayProperty.get();
    }

    public void setReachTargetTemperatureDelay(final int value) {
	reachTargetTemperatureDelayProperty.set(value);
    }

    public IntegerProperty reachTargetTemperatureDelay() {
	return reachTargetTemperatureDelayProperty;
    }

    public double getCurrentTemperature() {
	return currentTemperatureProperty.get();
    }

    public void setCurrentTemperature(final double value) {
	currentTemperatureProperty.set(value);
    }

    public DoubleProperty currentTemperatureProperty() {
	return currentTemperatureProperty;
    }

    public NumberFormat getNumberFormat() {
	return numberFormatProperty.get();
    }

    public void setNumberFormat(final NumberFormat numberFormat) {
	numberFormatProperty.set(numberFormat);
    }

    public ObjectProperty<NumberFormat> numberFormatProperty() {
	return numberFormatProperty;
    }

    public NestNoCss() {
	currentTemperatureProperty = new SimpleDoubleProperty(18);
	targetTemperatureProperty = new SimpleDoubleProperty(24);
	minTemperatureProperty = new SimpleDoubleProperty(0);
	maxTemperatureProperty = new SimpleDoubleProperty(50);
	reachTargetTemperatureDelayProperty = new SimpleIntegerProperty(0);
	numberFormatProperty = new SimpleObjectProperty<NestNoCss.NumberFormat>(
		NumberFormat.AUTO);

	targetTemperatureTickListener = new ChangeListener<Number>() {

	    @Override
	    public void changed(final ObservableValue<? extends Number> obdVal,
		    final Number oldVal, final Number newVal) {
		// for(final RadialMenuItem item : arcs){
		// final double diff = Math.abs(item.getStartAngle() -
		// newVal.doubleValue());
		// if(diff <= 2){
		// if(item.getLength() == 0.7){
		// item.setLength(1.3);
		// item.setStartAngle(item.getStartAngle()-0.1);
		//
		// }
		// }
		// }
	    }
	};

	// getStylesheets().add(
	// getClass().getResource("nest.css").toExternalForm());

	// getStyleClass().setAll("nest");
	initGraphcis();
	registerListeners();
    }

    private void initGraphcis() {
	// frame = new Region();
	// frame.getStyleClass().setAll("frame");
	frame = new Circle();
	frame.setFill(FRAME_FILL);

	shadow = new DropShadow();
	shadow.setBlurType(BlurType.ONE_PASS_BOX);
	shadow.setColor(Color.rgb(0, 0, 0, 0.4));
	frame.setEffect(shadow);

	frame1 = new Circle();
	// frame1.getStyleClass().setAll("frame1");
	frame1.setFill(FRAME1_FILL);
	frame1.setStroke(FRAME1_STROKE);
	frame1.setStrokeWidth(2.0);

	frame2 = new Circle();
	// frame2.getStyleClass().setAll("frame2");
	frame2.setFill(FRAME2_FILL);
	frame2.setStroke(FRAME2_STROKE);

	frame3 = new Circle();
	// frame3.getStyleClass().setAll("frame3");
	frame3.setFill(Color.web("#c44f1a"));

	line = new SVGPath();
	line.setContent("M 0.75,1.806272 C 0.75,1.806272 67.422114,-2.659598 118.06708,1.085452 130.59357,2.011752 166.81696,11.039202 185.35089,11.189052 206.02921,11.356252 242.24677,2.052122 255.84883,1.085452 304.58057,-2.377808 372.89963,1.806272 372.89963,1.806272");
	// line.getStyleClass().setAll("line");
	line.setFill(Color.web("#ffffff00"));
	line.setStroke(Color.web("#4d4d4d"));
	line.setStrokeWidth(1.5);

	line1 = new SVGPath();
	line1.setContent("M 0.75,1.806272 C 0.75,1.806272 67.422114,-2.659598 118.06708,1.085452 130.59357,2.011752 166.81696,11.039202 185.35089,11.189052 206.02921,11.356252 242.24677,2.052122 255.84883,1.085452 304.58057,-2.377808 372.89963,1.806272 372.89963,1.806272");
	// line.getStyleClass().setAll("line1");
	line.setFill(Color.web("#ffffff00"));
	line.setStroke(Color.web("#141414"));
	line.setStrokeWidth(1.5);

	final double length = 0.7;
	final Group arcContainer = new Group();
	for (int i = 0; i < nbArcs; i++) {
	    final RadialMenuItem item = RadialMenuItemBuilder
		    .create()
		    .startAngle(
			    -length
				    / 2.0
				    - arcEmptyLength
				    + i
				    * ((360 - (arcEmptyLength * 2.0)) / (nbArcs - 1)))
		    .length(length).backgroundFill(TICK_COLOR)
		    .strokeVisible(false).build();
	    item.getStyleClass().setAll("ticks");
	    arcs.add(item);
	    arcContainer.getChildren().add(item);
	}

	currentTemperatureTick = RadialMenuItemBuilder.create().length(3.2)
		.backgroundFill(CURRENT_TICK_COLOR).strokeVisible(false)
		.clockwise(true).build();

	final DoubleBinding currentTickStartAngleBinding = new DoubleBinding() {

	    {
		super.bind(currentTemperatureProperty, maxTemperatureProperty,
			minTemperatureProperty);
	    }

	    @Override
	    protected double computeValue() {
		return -225
			+ (1 - (maxTemperatureProperty.get() - currentTemperatureProperty
				.get())
				/ (maxTemperatureProperty.get() - minTemperatureProperty
					.get())) * (360 - arcEmptyLength * 2.0)
			- currentTemperatureTick.getLength() / 2.0;
	    }
	};
	currentTemperatureTick.startAngleProperty().bind(
		currentTickStartAngleBinding);

	targetTemperatureTick = RadialMenuItemBuilder.create().length(3)
		.backgroundFill(TARGET_TICK_COLOR).strokeVisible(false)
		.clockwise(true).build();
	final DoubleBinding targetTickStartAngleBinding = new DoubleBinding() {
	    {
		super.bind(targetTemperatureProperty, maxTemperatureProperty,
			minTemperatureProperty);
	    }

	    @Override
	    protected double computeValue() {
		return -225
			+ (1 - (maxTemperatureProperty.get() - targetTemperatureProperty
				.get())
				/ (maxTemperatureProperty.get() - minTemperatureProperty
					.get())) * (360 - arcEmptyLength * 2.0)
			- targetTemperatureTick.getLength() / 2.0;
	    }
	};
	targetTemperatureTick.startAngleProperty().bind(
		targetTickStartAngleBinding);

	currentTemperatureText = new Text();
//	currentTemperatureText.getStyleClass().setAll("text");
	currentTemperatureText.setFill(Color.web("#ffffff"));
	currentTemperatureText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 86));

	final StringBinding textBinding = new StringBinding() {
	    {
		super.bind(targetTemperatureProperty, numberFormatProperty);
	    }

	    @Override
	    protected String computeValue() {
		return numberFormatProperty.get().format(
			targetTemperatureProperty.getValue());
	    }
	};
	currentTemperatureText.textProperty().bind(textBinding);

	reachTargetTemperatureDelayText = new Text();
//	reachTargetTemperatureDelayText.getStyleClass().setAll("delaytext");
	reachTargetTemperatureDelayText.setFill(Color.web("#ffffffff"));
	reachTargetTemperatureDelayText.setFont(Font.font(Font.getDefault().getName(), FontWeight.LIGHT, 18));

	final StringBinding delayTextBinding = new StringBinding() {
	    {
		super.bind(reachTargetTemperatureDelayProperty);
	    }

	    @Override
	    protected String computeValue() {
		return "IN " + reachTargetTemperatureDelayProperty.get()
			+ " MIN";
	    }
	};
	reachTargetTemperatureDelayText.textProperty().bind(delayTextBinding);

	lightEffect = new Ellipse();
	lightEffect.setFill(Color.rgb(255, 255, 255, 0.7));
	lightEffect.setEffect(new BoxBlur(90, 90, 5));
	lightEffect.setCache(true);

	getChildren().setAll(frame, frame1, frame2, frame3, line, line1, arcContainer,
		currentTemperatureTick, targetTemperatureTick,
		currentTemperatureText, reachTargetTemperatureDelayText);
    }

    private void registerListeners() {
	widthProperty().addListener(listener);
	heightProperty().addListener(listener);
	currentTemperatureText.boundsInLocalProperty().addListener(listener);
	reachTargetTemperatureDelayText.boundsInLocalProperty().addListener(
		listener);
    }

    private void resize() {
	size = getWidth() < getHeight() ? getWidth() : getHeight();
	// frame.setPrefSize(size, size);
	frame.setRadius(size / 2.0);
	frame.setTranslateX(size / 2.0);
	frame.setTranslateY(size / 2.0);

	frame1.setRadius(frame1Ratio * size / 2.0);
	frame1.setTranslateX(size / 2.0);
	frame1.setTranslateY(size / 2.0);
	shadow.setOffsetX(size * shadowXOffset);
	shadow.setOffsetY(size * shadowYOffset);
	shadow.setRadius(size * shadowSizeOffset);
	shadow.setSpread(0.099);

	frame2.setRadius(frame2Ratio * size / 2.0);
	frame2.setTranslateX(size / 2.0);
	frame2.setTranslateY(size / 2.0);

	frame3.setRadius(frame3Ratio * size / 2.0);
	frame3.setTranslateX(size / 2.0);
	frame3.setTranslateY(size / 2.0);

	final double scaleRatio = size / initialSize;
	line1.setScaleX(scaleRatio);
	line1.setScaleY(scaleRatio);
	final double lineWidth = line1.getBoundsInLocal().getWidth();
	line1.setTranslateX(size / 2.0 - lineWidth / 2.0);
	line1.setTranslateY(size * 408.72054 / initialSize);

	line.setScaleX(scaleRatio);
	line.setScaleY(scaleRatio);
	line.setTranslateX(size / 2.0 - lineWidth / 2.0);
	line.setTranslateY(size * 410.08419 / initialSize);

	for (final RadialMenuItem arc : arcs) {
	    arc.setTranslateX(size / 2.0);
	    arc.setTranslateY(size / 2.0);
	    arc.setRadius(tickRatio * size / 2.0);
	    arc.setInnerRadius((tickRatio - tickRatioOffset) * size / 2.0);
	}

	currentTemperatureTick.setTranslateX(size / 2.0);
	currentTemperatureTick.setTranslateY(size / 2.0);
	currentTemperatureTick.setRadius(tickRatio * size / 2.0);
	currentTemperatureTick.setInnerRadius((tickRatio - tickRatioOffset)
		* size / 2.0);

	targetTemperatureTick.setTranslateX(size / 2.0);
	targetTemperatureTick.setTranslateY(size / 2.0);
	targetTemperatureTick.setRadius(tickRatio * size / 2.0);
	targetTemperatureTick
		.setInnerRadius((tickRatio - targetTickRatioOffset) * size
			/ 2.0);
	targetTemperatureTick.startAngleProperty().addListener(
		targetTemperatureTickListener);

	currentTemperatureText.setScaleX(scaleRatio);
	currentTemperatureText.setScaleY(scaleRatio);
	final double textWidth = currentTemperatureText.getBoundsInLocal()
		.getWidth();
	final double textHeight = currentTemperatureText.getBoundsInLocal()
		.getHeight();
	currentTemperatureText.setTranslateX(size / 2.0 - textWidth / 2.0);
	currentTemperatureText.setTranslateY(temperatureTextYRatio * size
		+ textHeight / 4.0);

	reachTargetTemperatureDelayText.setScaleX(scaleRatio);
	reachTargetTemperatureDelayText.setScaleY(scaleRatio);
	final double text2Width = reachTargetTemperatureDelayText
		.getBoundsInLocal().getWidth();
	final double text2Height = reachTargetTemperatureDelayText
		.getBoundsInLocal().getHeight();
	reachTargetTemperatureDelayText.setTranslateX(size / 2.0 - text2Width
		/ 2.0);
	reachTargetTemperatureDelayText
		.setTranslateY((temperatureTextYRatio - 0.1) * size
			+ text2Height / 4.0);

	lightEffect.setRotate(lightEffectRotate);
	lightEffect.setTranslateX(lightEffectXRatio * size);
	lightEffect.setTranslateY(lightEffectYRatio * size);
	lightEffect.setRadiusX(lightEffectXRadiusRatio * size);
	lightEffect.setRadiusY(lightEffectYRadiusRatio * size);
    }

    private final class InternalListener implements InvalidationListener {

	@Override
	public void invalidated(final Observable value) {
	    resize();

	}
    }

    public static enum NumberFormat {
	AUTO("0"), STANDARD("0"), FRACTIONAL("0.0#"), SCIENTIFIC("0.##E0"), PERCENTAGE(
		"##0.0%");

	private final DecimalFormat DF;

	private NumberFormat(final String FORMAT_STRING) {
	    Locale.setDefault(new Locale("en", "US"));

	    DF = new DecimalFormat(FORMAT_STRING);
	}

	public String format(final Number NUMBER) {
	    return DF.format(NUMBER);
	}
    }

}
