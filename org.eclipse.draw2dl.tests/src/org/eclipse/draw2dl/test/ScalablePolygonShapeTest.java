/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alexander Shatalin (Borland) - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.test;

import junit.framework.TestCase;

import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.SWTGraphics;
import org.eclipse.draw2dl.ScalablePolygonShape;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.PointList;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ScalablePolygonShapeTest extends TestCase {

	private static final int RECTANGLE_START = 1;

	private static final int RECTANGLE_END = 5;

	private static final PointList RECTANGLE_POINTS = new PointList(new int[] {
			RECTANGLE_START, RECTANGLE_START, RECTANGLE_END, RECTANGLE_START,
			RECTANGLE_END, RECTANGLE_END, RECTANGLE_START, RECTANGLE_END });

	private static final int LINE_WIDTH = 2;

	private static final Rectangle RECTANGLE_BOUNDS = new Rectangle(0, 0,
			RECTANGLE_END + LINE_WIDTH, RECTANGLE_END + LINE_WIDTH);

	private static final Rectangle RECTANGLE_DOUBLED_BOUNDS = new Rectangle(0,
			0, RECTANGLE_END * 2 + LINE_WIDTH, RECTANGLE_END * 2 + LINE_WIDTH);

	public void testScaledPointsEquality() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		scalablePolygon.setBounds(RECTANGLE_BOUNDS);
		scalablePolygon.setLineWidth(LINE_WIDTH);
		PointList scaledPoints = scalablePolygon.getScaledPoints();
		checkScaledPointsNotChanged(scalablePolygon, scaledPoints);
	}

	public void testPointsUnchangedOnScaling() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		PointList points = RECTANGLE_POINTS.getCopy();
		scalablePolygon.setPoints(points);
		scalablePolygon.setBounds(RECTANGLE_BOUNDS);
		scalablePolygon.getScaledPoints();
		scalablePolygon.setBounds(RECTANGLE_DOUBLED_BOUNDS);
		scalablePolygon.getScaledPoints();
		assertEquals(points, scalablePolygon.getPoints());
		checkEquals(RECTANGLE_POINTS, points);
	}

	public void testSmallBounds() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		scalablePolygon.setLineWidth(LINE_WIDTH);
		scalablePolygon.setBounds(new Rectangle(0, 0, 0, 0));
		int[] scaledPoints = scalablePolygon.getScaledPoints().toIntArray();
		for (int i = 0; i < scaledPoints.length;) {
			assertEquals(LINE_WIDTH / 2, scaledPoints[i++]);
		}

		// width < lineWidth
		scalablePolygon.setBounds(new Rectangle(0, 0, 1, 100));
		scaledPoints = scalablePolygon.getScaledPoints().toIntArray();
		for (int i = 0; i < scaledPoints.length; i++) {
			assertEquals(LINE_WIDTH / 2, scaledPoints[i++]);
		}

		// height < lineWidth
		scalablePolygon.setBounds(new Rectangle(0, 0, 100, 1));
		scaledPoints = scalablePolygon.getScaledPoints().toIntArray();
		for (int i = 1; i < scaledPoints.length; i++) {
			assertEquals(LINE_WIDTH / 2, scaledPoints[i++]);
		}
	}

	public void testScaledPointsUpdateOnPointsChanging() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setBounds(RECTANGLE_BOUNDS);
		scalablePolygon.setLineWidth(LINE_WIDTH);
		PointList scaledPoints = scalablePolygon.getScaledPoints();
		assertEquals(0, scaledPoints.size());

		scalablePolygon.addPoint(new Point(0, 0));
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				1);

		scalablePolygon.removeAllPoints();
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				0);

		scalablePolygon.setPoints(RECTANGLE_POINTS.getCopy());
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				RECTANGLE_POINTS.size());

		scalablePolygon.setStart(new Point(2, 2));
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size());

		scalablePolygon.setEnd(new Point(2, 5));
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size());

		scalablePolygon.setPoint(new Point(5, 2), 1);
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size());

		scalablePolygon.insertPoint(new Point(3, 4), 1);
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size() + 1);

		scalablePolygon.removePoint(1);
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size() - 1);
	}

	public void testScaledPointsUpdateOnBoundsChanging() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		scalablePolygon.setBounds(RECTANGLE_BOUNDS);
		PointList scaledPoints = scalablePolygon.getScaledPoints();

		Rectangle bounds = RECTANGLE_BOUNDS.getCopy();
		scalablePolygon.setBounds(bounds.resize(10, 10));
		scaledPoints = checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size());

		// Size of the figure is the same, so scaledPoints should not be
		// changed.
		scalablePolygon.setBounds(bounds.translate(10, 10));
		checkScaledPointsNotChanged(scalablePolygon, scaledPoints);
	}

	public void testScaledPointsUpdateOnSetLineWidth() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setLineWidth(LINE_WIDTH);
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		PointList scaledPoints = scalablePolygon.getScaledPoints();

		scalablePolygon.setLineWidth(LINE_WIDTH * 2);
		checkScaledPointsChanged(scalablePolygon, scaledPoints,
				scaledPoints.size());
	}

	public void testScaling() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		scalablePolygon.setLineWidth(LINE_WIDTH);
		scalablePolygon.setBounds(RECTANGLE_DOUBLED_BOUNDS);
		checkScaledPointsNotChanged(scalablePolygon, new PointList(new int[] {
				3, 3, 11, 3, 11, 11, 3, 11 }));
	}

	public void off_testContainsPoints() {
		ScalablePolygonShape scalablePolygon = new ScalablePolygonShape();
		scalablePolygon.setPoints(RECTANGLE_POINTS);
		scalablePolygon.setLineWidth(1);
		scalablePolygon.setBounds(RECTANGLE_DOUBLED_BOUNDS);
		scalablePolygon.setOutline(true);
		scalablePolygon.setFill(true);
		scalablePolygon.setBackgroundColor(ColorConstants.black);
		scalablePolygon.setForegroundColor(ColorConstants.black);

		Display display = Display.getDefault();
		int imageSize = RECTANGLE_END * 3;
		Image image = new Image(display, imageSize, imageSize);
		GC gc = new GC(image);
		// Filling initial image with white color
		gc.setBackground(ColorConstants.white);
		gc.setForeground(ColorConstants.white);
		gc.fillRectangle(0, 0, imageSize, imageSize);
		gc.drawRectangle(0, 0, imageSize, imageSize);

		gc.setBackground(ColorConstants.black);
		gc.setForeground(ColorConstants.black);
		SWTGraphics graphics = new SWTGraphics(gc);
		scalablePolygon.paint(graphics);
		graphics.dispose();
		gc.dispose();
		ImageData imageData = image.getImageData();
		for (int x = 0; x < imageSize; x++) {
			for (int y = 0; y < imageSize; y++) {
				boolean isPolygonPoint = imageData.getPixel(x, y) == 0;
				assertTrue(
						"Point (" + x + "," + y + ") is"
								+ (isPolygonPoint ? " " : " not ")
								+ "a point of polygon",
						scalablePolygon.containsPoint(new Point(x, y)) == isPolygonPoint);
			}
		}
	}

	private PointList checkScaledPointsChanged(
			ScalablePolygonShape scalablePolygon, PointList oldScaledPoints,
			int expectedSize) {
		PointList newScaledPoints = scalablePolygon.getScaledPoints();
		checkNotEquals(oldScaledPoints, newScaledPoints);
		assertEquals(expectedSize, newScaledPoints.size());
		return newScaledPoints;
	}

	private void checkNotEquals(PointList original, PointList modified) {
		if (original.size() != modified.size()) {
			return;
		}
		int[] originalIntArray = original.toIntArray();
		int[] modifiedIntArray = modified.toIntArray();
		for (int i = 0; i < modifiedIntArray.length; i++) {
			if (originalIntArray[i] != modifiedIntArray[i]) {
				return;
			}
		}
		fail("Passed point lists are equals");
	}

	private PointList checkScaledPointsNotChanged(
			ScalablePolygonShape scalablePolygon, PointList oldScaledPoints) {
		PointList newScaledPoints = scalablePolygon.getScaledPoints();
		checkEquals(oldScaledPoints, newScaledPoints);
		return newScaledPoints;
	}

	private void checkEquals(PointList original, PointList modified) {
		if (original.size() != modified.size()) {
			fail("Passed point lists are not equals");
		}
		int[] originalIntArray = original.toIntArray();
		int[] modifiedIntArray = modified.toIntArray();
		for (int i = 0; i < modifiedIntArray.length; i++) {
			if (originalIntArray[i] != modifiedIntArray[i]) {
				fail("Passed point lists are not equals");
			}
		}
	}

}
