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
package org.eclipse.draw2dl.test;

import junit.framework.TestCase;

import org.eclipse.draw2dl.RectangleFigure;
import org.eclipse.draw2dl.XYLayout;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;

public class XYLayoutTest extends TestCase {

	protected org.eclipse.draw2dl.XYLayout layout;
	protected org.eclipse.draw2dl.RectangleFigure figure;
	protected org.eclipse.draw2dl.RectangleFigure contents;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPreferredSize() {
		layout = new XYLayout();
		contents = new org.eclipse.draw2dl.RectangleFigure();
		contents.setLayoutManager(layout);

		figure = new RectangleFigure();
		contents.add(figure, new Rectangle(0, 0, 100, -1));
		figure.setPreferredSize(100, 150);

		Dimension d = contents.getPreferredSize();

		assertEquals(100, d.width);
		assertEquals(150, d.height);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
