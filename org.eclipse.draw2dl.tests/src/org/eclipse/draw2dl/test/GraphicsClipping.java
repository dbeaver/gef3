/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

import org.eclipse.draw2dl.SWTGraphics;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class GraphicsClipping extends TestCase {

	private Image image;
	private GC gc;
	private org.eclipse.draw2dl.SWTGraphics graphics;

	public void testSimpleClip() {
		org.eclipse.draw2dl.geometry.Rectangle rect = new org.eclipse.draw2dl.geometry.Rectangle(14, 21, 30, 40);
		graphics.setClip(rect);
		graphics.drawPoint(0, 0);
		assertEquals(gcClipping(), rect);

		rect.translate(5, 5);
		graphics.clipRect(rect);
		graphics.drawPoint(0, 0);
		rect.resize(-5, -5);
		assertEquals(rect, gcClipping());
	}

	private org.eclipse.draw2dl.geometry.Rectangle gcClipping() {
		return new org.eclipse.draw2dl.geometry.Rectangle(gc.getClipping());
	}

	private org.eclipse.draw2dl.geometry.Rectangle graphicsClip() {
		return graphics.getClip(new org.eclipse.draw2dl.geometry.Rectangle());
	}

	public void testTranslatedClip() {
		org.eclipse.draw2dl.geometry.Rectangle rect = new org.eclipse.draw2dl.geometry.Rectangle(14, 21, 300, 400);
		graphics.setClip(rect);
		graphics.translate(9, 7);

		graphics.drawPoint(0, 0);
		assertEquals(rect, gcClipping());

		rect.translate(-9, -7);
		assertEquals(rect, graphicsClip());

		org.eclipse.draw2dl.geometry.Rectangle intersect = new org.eclipse.draw2dl.geometry.Rectangle(50, 50, 50, 50);
		graphics.clipRect(intersect);
		rect.intersect(intersect);

		graphics.drawPoint(0, 0);
		assertEquals(graphicsClip(), rect);

		rect.translate(9, 7);
		assertEquals(gcClipping(), rect);
	}

	public void testZoomedClip() {
		org.eclipse.draw2dl.geometry.Rectangle rect = new Rectangle(14, 21, 300, 400);
		graphics.setClip(rect);
		graphics.scale(2.0);
		graphics.translate(90, 0);
		graphics.drawPoint(0, 0);

		rect.scale(0.5);
		rect.translate(-90, 0);
		assertEquals(graphicsClip(), rect);
	}

	protected void setUp() throws Exception {
		image = new Image(Display.getDefault(), 800, 600);
		gc = new GC(image);
		graphics = new SWTGraphics(gc);
	}

	protected void tearDown() throws Exception {
		graphics.dispose();
		gc.dispose();
		image.dispose();
	}

}
