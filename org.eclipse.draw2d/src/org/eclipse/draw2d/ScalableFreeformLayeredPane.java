/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane 
	extends FreeformLayeredPane 
	implements ScalableFigure
{

private double scale = 1.0;

/**
 * @see org.eclipse.draw2d.Figure#getClientArea()
 */
public Rectangle getClientArea(Rectangle rect) {
	super.getClientArea(rect);
	rect.width /= scale;
	rect.height /= scale;
	rect.x /= scale;
	rect.y /= scale;
	return rect;
}

public double getScale() {
	return scale;
}

/**
 *  * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics) */
protected void paintClientArea(Graphics graphics) {
	if (getChildren().isEmpty())
		return;
	if (scale == 1.0) {
		super.paintClientArea(graphics);
	} else {
		ScaledGraphics g = new ScaledGraphics(graphics);
		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
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
 * @param newZoom The new zoom level */
public void setScale(double newZoom) {
	scale = newZoom;
	superFireMoved();
	getFreeformHelper().invalidate();
	repaint();
}

/**
 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
 */
public void translateToParent(Translatable t) {
	t.performScale(scale);
}

/**
 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
 */
public void translateFromParent(Translatable t) {
	t.performScale(1 / scale);
}

/**
 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
 */
protected final boolean useLocalCoordinates() {
	return false;
}

}