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
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * Draws a rectangle whose size is determined by the bounds set to it.
 */
public class RectangleFigure extends org.eclipse.draw2dl.Shape {
	/**
	 * Creates a RectangleFigure.
	 */
	public RectangleFigure() {
	}

	/**
	 * @see org.eclipse.draw2dl.Shape#fillShape(org.eclipse.draw2dl.Graphics)
	 */
	protected void fillShape(org.eclipse.draw2dl.Graphics graphics) {
		graphics.fillRectangle(getBounds());
	}

	/**
	 * @see Shape#outlineShape(org.eclipse.draw2dl.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
		int inset1 = (int) Math.floor(lineInset);
		int inset2 = (int) Math.ceil(lineInset);

		Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
		r.x += inset1;
		r.y += inset1;
		r.width -= inset1 + inset2;
		r.height -= inset1 + inset2;

		graphics.drawRectangle(r);
	}
}
