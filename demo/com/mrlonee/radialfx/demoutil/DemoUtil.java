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
package com.mrlonee.radialfx.demoutil;

import java.text.DecimalFormat;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ColorPickerBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.util.Callback;

public class DemoUtil extends VBox {

    private final DecimalFormat twoDForm = new DecimalFormat("#.##");

    public DemoUtil() {
    }

    public void addAngleControl(final String title, final DoubleProperty prop) {
	final Slider slider = addSliderControl(title, prop);
	slider.setMin(0);
	slider.setMax(360);
    }

    public void addRadiusControl(final String title, final DoubleProperty prop) {
	final Slider slider = addSliderControl(title, prop);
	slider.setMin(0);
	slider.setMax(500);
    }

    private Slider addSliderControl(final String title,
	    final DoubleProperty prop) {
	final Slider slider = new Slider();
	slider.setValue(prop.get());
	prop.bind(slider.valueProperty());
	final VBox box = new VBox();
	final Text titleText = new Text(title);

	titleText.textProperty().bind(new StringBinding() {
	    {
		super.bind(slider.valueProperty());
	    }

	    @Override
	    protected String computeValue() {
		return title + " : " + twoDForm.format(slider.getValue());
	    }

	});
	box.getChildren().addAll(titleText, slider);
	getChildren().add(box);
	return slider;
    }

    public void addColorControl(final String title,
	    final ObjectProperty<Paint> paintProperty) {
	final ColorPicker colorPicker = ColorPickerBuilder.create()
		.value((Color) paintProperty.get()).build();

	paintProperty.bind(colorPicker.valueProperty());
	final VBox box = new VBox();
	final Text titleText = new Text(title);

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
	getChildren().add(box);
    }

    public void addBooleanControl(final String title,
	    final BooleanProperty boolProp) {
	final CheckBox check = new CheckBox();
	check.setSelected(boolProp.get());
	boolProp.bind(check.selectedProperty());

	final VBox box = new VBox();
	final Text titleText = new Text(title);

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
	getChildren().add(box);

    }

    public void addGraphicControl(final String title,
	    final ObjectProperty<Node> graphicProperty) {

	final Node circle  = CircleBuilder.create().radius(4).fill(Color.ORANGE).build();
	final Node square  = RectangleBuilder.create().width(8).height(8).build();
	final Node text  = TextBuilder.create().text("test").build();

	final ComboBox<Node> choices = new ComboBox<Node>(FXCollections.observableArrayList(circle, square, text));
	choices.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
	    @Override
	    public ListCell<Node> call(final ListView<Node> param) {
		final ListCell<Node> cell = new ListCell<Node>() {
		    @Override
		    public void updateItem(final Node item, final boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
			    setText(item.getClass().getSimpleName());
			} else {
			    setText(null);
			}
		    }
		};
		return cell;
	    }
	});
	choices.getSelectionModel().select(0);
	graphicProperty.bind(choices.valueProperty());

	final VBox box = new VBox();
	final Text titleText = new Text(title);

	titleText.textProperty().bind(new StringBinding() {
	    {
		super.bind(choices.selectionModelProperty());
	    }

	    @Override
	    protected String computeValue() {
		return title + " : "
			+ String.valueOf(choices.selectionModelProperty().get().getSelectedItem().getClass().getSimpleName());
	    }

	});
	box.getChildren().addAll(titleText, choices);
	getChildren().add(box);

    }
}
