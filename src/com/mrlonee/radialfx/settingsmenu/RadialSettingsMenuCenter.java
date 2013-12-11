/**
 * RadialMenuEnhancedCenter
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

package com.mrlonee.radialfx.settingsmenu;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;

public class RadialSettingsMenuCenter extends Group {

    private final Map<Object, Node> centerGraphics = new HashMap<Object, Node>();
    private FadeTransition hideTransition;
    private FadeTransition showTransition;
    private Group showTransitionGroup = new Group();
    private Group hideTransitionGroup = new Group();

    RadialSettingsMenuCenter() {
	this.getChildren().add(this.hideTransitionGroup);
	this.getChildren().add(this.showTransitionGroup);
	this.showTransition = FadeTransitionBuilder.create()
		.duration(Duration.millis(400)).node(this.showTransitionGroup)
		.fromValue(0.0).toValue(1.0).build();
	this.hideTransition = FadeTransitionBuilder.create()
		.duration(Duration.millis(400)).node(this.hideTransitionGroup)
		.fromValue(1.0).toValue(0.0).build();
    }

    void addCenterItem(Object key, Node centerGraphic) {
	this.centerGraphics.put(key, centerGraphic);
	centerGraphic.setTranslateX(-centerGraphic.getBoundsInLocal()
		.getWidth() / 2.0);
	centerGraphic.setTranslateY(-centerGraphic.getBoundsInLocal()
		.getHeight() / 2.0);
    }

    void displayCenter(final Object key) {
	final Node node = this.centerGraphics.get(key);
	this.showTransitionGroup.getChildren().setAll(node);
	this.showTransition.playFromStart();
    }

    void hideCenter(final Object key) {
	final Node node = this.centerGraphics.get(key);
	this.hideTransitionGroup.getChildren().setAll(node);
	this.hideTransition.playFromStart();
    }
}
