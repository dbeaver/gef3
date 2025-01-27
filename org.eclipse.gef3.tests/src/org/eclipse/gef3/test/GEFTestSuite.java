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
package org.eclipse.gef3.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The main test suite for GEF.
 * 
 * @author Eric Bordeau
 */
public class GEFTestSuite extends TestSuite {

	public static Test suite() {
		return new GEFTestSuite();
	}

	/**
	 * Constructs a new GEFTestSuite. Add any JUnit tests to the suite here.
	 */
	public GEFTestSuite() {
		addTest(new TestSuite(PaletteCustomizerTest.class));
		addTest(new TestSuite(ToolUtilitiesTest.class));
		addTest(new TestSuite(DragEditPartsTrackerTest.class));
		addTest(new TestSuite(CommandStackTest.class));
	}

}
