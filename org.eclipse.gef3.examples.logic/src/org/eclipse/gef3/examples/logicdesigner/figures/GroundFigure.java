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
package org.eclipse.gef3.examples.logicdesigner.figures;

import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * @author danlee
 */
public class GroundFigure extends OutputFigure {

	public static final Dimension SIZE = new Dimension(15, 15);

	/**
	 * Constructor for GroundFigure.
	 */
	public GroundFigure() {
		super();
	}

	/**
	 * @see org.eclipse.draw2dl.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return SIZE;
	}

	/**
	 * @see org.eclipse.draw2dl.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		g.setBackgroundColor(ColorConstants.yellow);

		g.fillOval(r);
		r.height--;
		r.width--;
		g.drawOval(r);
		g.translate(r.getLocation());

		// Draw the "V"
		g.drawLine(3, 4, 5, 9);
		g.drawLine(5, 9, 7, 4);

		// Draw the "0"
		g.drawOval(7, 8, 3, 3);
	}

}
