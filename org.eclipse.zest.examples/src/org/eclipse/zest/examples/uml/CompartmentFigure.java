/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.examples.uml;

import org.eclipse.draw2dl.AbstractBorder;
import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.ToolbarLayout;
import org.eclipse.draw2dl.geometry.Insets;

public class CompartmentFigure extends Figure {

	public CompartmentFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(2);
		setLayoutManager(layout);
		setBorder(new CompartmentFigureBorder());
	}

	class CompartmentFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
}
