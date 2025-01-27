/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
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

import org.eclipse.draw2dl.geometry.PrecisionPoint;

/**
 * JUnit Tests for PrecisionPoint.
 * 
 * @author Anthony Hunter
 */
public class PrecisionPointTest extends TestCase {

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=227977
	 */
	public void testEquals() {
		PrecisionPoint p1 = new PrecisionPoint(0.1, 0.1);
		PrecisionPoint p2 = new PrecisionPoint(0.2, 0.2);
		assertFalse(p1.equals(p2));
	}

	public void testTranslate() {
		PrecisionPoint p1 = new PrecisionPoint(0.1, 0.1);
		PrecisionPoint p2 = new PrecisionPoint(0.2, 0.2);
		assertTrue(p2.equals(p1.getTranslated(0.1, 0.1)));
		assertTrue(p2.equals(p1.getTranslated(p1)));

	}
}
