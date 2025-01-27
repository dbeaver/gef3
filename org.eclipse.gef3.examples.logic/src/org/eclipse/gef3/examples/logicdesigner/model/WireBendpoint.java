/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.logicdesigner.model;

import org.eclipse.draw2dl.Bendpoint;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;

public class WireBendpoint implements java.io.Serializable, Bendpoint {

	private static final long serialVersionUID = 2637650104131990387L;
	private float weight = 0.5f;
	private Dimension d1, d2;

	public WireBendpoint() {
	}

	public Dimension getFirstRelativeDimension() {
		return d1;
	}

	public Point getLocation() {
		return null;
	}

	public Dimension getSecondRelativeDimension() {
		return d2;
	}

	public float getWeight() {
		return weight;
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setWeight(float w) {
		weight = w;
	}

}
