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

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * This class implements the {@link org.eclipse.draw2dl.LayoutManager} interface
 * using the XY Layout algorithm. This lays out the components using the layout
 * constraints as defined by each component.
 */
public class XYLayout extends org.eclipse.draw2dl.AbstractLayout {

	/** The layout contraints */
	protected Map constraints = new HashMap();

	/**
	 * Calculates and returns the preferred size of the input figure. Since in
	 * XYLayout the location of the child should be preserved, the preferred
	 * size would be a region which would hold all the children of the input
	 * figure. If no constraint is set, that child is ignored for calculation.
	 * If width and height are not positive, the preferred dimensions of the
	 * child are taken.
	 * 
	 * @see AbstractLayout#calculatePreferredSize(org.eclipse.draw2dl.IFigure, int, int)
	 * @since 2.0
	 */
	protected Dimension calculatePreferredSize(org.eclipse.draw2dl.IFigure f, int wHint, int hHint) {
		Rectangle rect = new Rectangle();
		for (IFigure child : f.getChildren()) {
			Rectangle r = (Rectangle) constraints.get(child);
			if (r == null)
				continue;

			if (r.width == -1 || r.height == -1) {
				Dimension preferredSize = child.getPreferredSize(r.width,
					r.height);
				r = r.getCopy();
				if (r.width == -1)
					r.width = preferredSize.width;
				if (r.height == -1)
					r.height = preferredSize.height;
			}
			rect.union(r);
		}
		Dimension d = rect.getSize();
		Insets insets = f.getInsets();
		return new Dimension(d.width + insets.getWidth(), d.height
				+ insets.getHeight()).union(getBorderPreferredSize(f));
	}

	/**
	 * @see org.eclipse.draw2dl.LayoutManager#getConstraint(org.eclipse.draw2dl.IFigure)
	 */
	public Object getConstraint(org.eclipse.draw2dl.IFigure figure) {
		return constraints.get(figure);
	}

	/**
	 * Returns the origin for the given figure.
	 * 
	 * @param parent
	 *            the figure whose origin is requested
	 * @return the origin
	 */
	public Point getOrigin(org.eclipse.draw2dl.IFigure parent) {
		return parent.getClientArea().getLocation();
	}

	/**
	 * Implements the algorithm to layout the components of the given container
	 * figure. Each component is laid out using its own layout constraint
	 * specifying its size and position.
	 * 
	 * @see org.eclipse.draw2dl.LayoutManager#layout(org.eclipse.draw2dl.IFigure)
	 */
	public void layout(org.eclipse.draw2dl.IFigure parent) {
		Iterator children = parent.getChildren().iterator();
		Point offset = getOrigin(parent);
		org.eclipse.draw2dl.IFigure f;
		while (children.hasNext()) {
			f = (org.eclipse.draw2dl.IFigure) children.next();
			Rectangle bounds = (Rectangle) getConstraint(f);
			if (bounds == null)
				continue;

			if (bounds.width == -1 || bounds.height == -1) {
				Dimension preferredSize = f.getPreferredSize(bounds.width,
						bounds.height);
				bounds = bounds.getCopy();
				if (bounds.width == -1)
					bounds.width = preferredSize.width;
				if (bounds.height == -1)
					bounds.height = preferredSize.height;
			}
			bounds = bounds.getTranslated(offset);
			f.setBounds(bounds);
		}
	}

	/**
	 * @see org.eclipse.draw2dl.LayoutManager#remove(org.eclipse.draw2dl.IFigure)
	 */
	public void remove(org.eclipse.draw2dl.IFigure figure) {
		super.remove(figure);
		constraints.remove(figure);
	}

	/**
	 * Sets the layout constraint of the given figure. The constraints can only
	 * be of type {@link Rectangle}.
	 * 
	 * @see LayoutManager#setConstraint(org.eclipse.draw2dl.IFigure, Object)
	 * @since 2.0
	 */
	public void setConstraint(IFigure figure, Object newConstraint) {
		super.setConstraint(figure, newConstraint);
		if (newConstraint != null)
			constraints.put(figure, newConstraint);
	}

}
