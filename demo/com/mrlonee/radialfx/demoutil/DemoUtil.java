package com.mrlonee.radialfx.demoutil;

import java.text.DecimalFormat;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ColorPickerBuilder;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class DemoUtil extends VBox {

    private final DecimalFormat twoDForm = new DecimalFormat("#.##");

    public DemoUtil() {
    }

    public void addAngleControl(final String title, final DoubleProperty prop) {
	Slider slider = this.addSliderControl(title, prop);
	slider.setMin(0);
	slider.setMax(360);
    }

    public void addRadiusControl(String title, DoubleProperty prop) {
	Slider slider = this.addSliderControl(title, prop);
	slider.setMin(0);
	slider.setMax(500);
    }

    private Slider addSliderControl(final String title,
	    final DoubleProperty prop) {
	final Slider slider = new Slider();
	slider.setValue(prop.get());
	prop.bind(slider.valueProperty());
	VBox box = new VBox();
	Text titleText = new Text(title);

	titleText.textProperty().bind(new StringBinding() {
	    {
		super.bind(slider.valueProperty());
	    }

	    @Override
	    protected String computeValue() {
		return title + " : "
			+ DemoUtil.this.twoDForm.format(slider.getValue());
	    }

	});
	box.getChildren().addAll(titleText, slider);
	this.getChildren().add(box);
	return slider;
    }

    public void addColorControl(final String title,
	    final ObjectProperty<Paint> paintProperty) {
	final ColorPicker colorPicker = ColorPickerBuilder.create()
		.value((Color) paintProperty.get()).build();

	paintProperty.bind(colorPicker.valueProperty());
	VBox box = new VBox();
	Text titleText = new Text(title);

	titleText.textProperty().bind(new StringBinding() {
	    {
		super.bind(colorPicker.valueProperty());
	    }

	    @Override
	    protected String computeValue() {
		return title + " : " + colorPicker.getValue().toString();
	    }

	});
	box.getChildren().addAll(titleText, colorPicker);
	this.getChildren().add(box);
    }

    public void addBooleanControl(final String title,
	    final BooleanProperty boolProp) {
	final CheckBox check = new CheckBox();
	check.setSelected(boolProp.get());
	boolProp.bind(check.selectedProperty());

	VBox box = new VBox();
	Text titleText = new Text(title);

	titleText.textProperty().bind(new StringBinding() {
	    {
		super.bind(check.selectedProperty());
	    }

	    @Override
	    protected String computeValue() {
		return title + " : "
			+ String.valueOf(check.selectedProperty().get());
	    }

	});
	box.getChildren().addAll(titleText, check);
	this.getChildren().add(box);

    }
}
