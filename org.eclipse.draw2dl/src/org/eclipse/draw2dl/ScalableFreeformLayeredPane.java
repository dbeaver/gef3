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
import org.eclipse.draw2dl.geometry.Translatable;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane extends FreeformLayeredPane implements
    ScalableFigure {

	private double scale = 1.0;

	/**
	 * @see Figure#getClientArea()
	 */
	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);
		rect.width /= scale;
		rect.height /= scale;
		rect.x /= scale;
		rect.y /= scale;
		return rect;
	}

	/**
	 * Returns the current zoom scale level.
	 * 
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * @see IFigure#isCoordinateSystem()
	 */
	public boolean isCoordinateSystem() {
		return true;
	}

	/**
	 * @see Figure#paintClientArea(org.eclipse.draw2dl.Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty())
			return;
		if (scale == 1.0) {
			super.paintClientArea(graphics);
		} else {
			org.eclipse.draw2dl.ScaledGraphics g = new ScaledGraphics(graphics);
			boolean optimizeClip = getBorder() == null
					|| getBorder().isOpaque();
			if (!optimizeClip)
				g.clipRect(getBounds().getCropped(getInsets()));
			g.scale(scale);
			g.pushState();
			paintChildren(g);
			g.dispose();
			graphics.restoreState();
		}
	}

	/**
	 * Sets the zoom level
	 * 
	 * @param newZoom
	 *            The new zoom level
	 */
	public void setScale(double newZoom) {
		if (scale == newZoom)
			return;
		scale = newZoom;
		superFireMoved(); // For AncestorListener compatibility
		getFreeformHelper().invalidate();
		repaint();
	}

	/**
	 * @see Figure#translateToParent(Translatable)
	 */
	public void translateToParent(Translatable t) {
		t.performScale(scale);
	}

	/**
	 * @see Figure#translateFromParent(Translatable)
	 */
	public void translateFromParent(Translatable t) {
		t.performScale(1 / scale);
	}

	/**
	 * @see Figure#useLocalCoordinates()
	 */
	protected final boolean useLocalCoordinates() {
		return false;
	}

}
