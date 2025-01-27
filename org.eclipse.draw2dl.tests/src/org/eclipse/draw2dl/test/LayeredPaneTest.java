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

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.LayeredPane;

public class LayeredPaneTest extends TestCase {

	private LayeredPane pane;
	private IFigure fig1;
	private IFigure fig2;
	private IFigure fig3;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		pane = new LayeredPane();
	}

	public void testIndexOutOfBounds() {
		fig1 = new Figure();
		fig2 = new Figure();
		fig3 = new Figure();

		pane.add(fig1);
		pane.add(fig2);

		pane.remove(fig1);
		boolean failed = false;

		try {
			pane.add(fig3);
		} catch (IndexOutOfBoundsException e) {
			failed = true;
		}

		assertEquals(false, failed);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		pane = null;
	}

}
