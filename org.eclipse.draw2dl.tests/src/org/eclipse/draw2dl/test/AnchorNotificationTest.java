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

import org.eclipse.draw2dl.*;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * Tests the notification of anchors and connection figures.
 * 
 * <pre>
 * @Contents figure
 *   @LocalCoordinates
 *      @connection
 *      @endpoint1 
 *    @LocalCoordinates
 *      @endpoint2
 * </pre>
 * 
 * Moving the outermost localcoordinate figure should not notify. Moving either
 * endpoint should notify. Moving the innermost localcoordinates figure should
 * notify.
 * 
 * @since 3.1
 */

public class AnchorNotificationTest extends TestCase {

	int count;

	/**
	 * @since 3.1
	 */
	public class TestPolylineConnection extends org.eclipse.draw2dl.PolylineConnection {

		/**
		 * @see org.eclipse.draw2dl.PolylineConnection#anchorMoved(org.eclipse.draw2dl.ConnectionAnchor)
		 */
		public void anchorMoved(org.eclipse.draw2dl.ConnectionAnchor anchor) {
			super.anchorMoved(anchor);
			count++;
		}

	}

	private LocalCoordinates commonAncestor;
	private PolylineConnection conn;
	private org.eclipse.draw2dl.Figure end;
	private org.eclipse.draw2dl.Figure start;
	private LocalCoordinates nestedCoordinates;

	public class LocalCoordinates extends org.eclipse.draw2dl.Figure {
		protected boolean useLocalCoordinates() {
			return true;
		}
	}

	void moveAll() {
		commonAncestor.translate(1, 1);
		nestedCoordinates.translate(1, 1);
		end.translate(1, 1);
		start.translate(1, 1);
	}

	/**
	 * @since 3.1
	 */
	protected void setUp() {
		org.eclipse.draw2dl.Figure contents = new org.eclipse.draw2dl.Figure();
		contents.addNotify();
		contents.setBounds(new org.eclipse.draw2dl.geometry.Rectangle(0, 0, 100, 100));
		contents.add(commonAncestor = new LocalCoordinates());
		commonAncestor.setBounds(new org.eclipse.draw2dl.geometry.Rectangle(10, 10, 80, 80));
		commonAncestor.add(start = new org.eclipse.draw2dl.Figure());
		start.setBounds(new org.eclipse.draw2dl.geometry.Rectangle(0, 0, 10, 10));
		commonAncestor.add(nestedCoordinates = new LocalCoordinates());
		commonAncestor.add(conn = new TestPolylineConnection());

		nestedCoordinates.add(end = new Figure());
		end.setBounds(new Rectangle(60, 60, 10, 10));
		conn.setSourceAnchor(new org.eclipse.draw2dl.ChopboxAnchor(start));
		conn.setTargetAnchor(new ChopboxAnchor(end));
	}

	public void testMoveSource() {
		count = 0;
		start.translate(10, 10);
		assertTrue(count == 1);
	}

	public void testMoveTarget() {
		count = 0;
		end.translate(1, 1);
		assertTrue(count == 1);
	}

	public void testMoveTargetParent() {
		count = 0;
		nestedCoordinates.translate(10, 10);
		assertTrue("Count != 1 :" + count, count == 1);
	}

	public void testRetargetTargetAnchor() {
		count = 0;
		ConnectionAnchor old = conn.getTargetAnchor();
		conn.setTargetAnchor(new XYAnchor(new Point(20, 30)));
		end.translate(-5, -5);
		nestedCoordinates.translate(-1, -1);
		assertTrue(count == 0);
		conn.setTargetAnchor(old);
	}

	public void testRemoveConnection() {
		count = 0;
		conn.getParent().remove(conn);
		moveAll();
		assertTrue(count == 0);
		commonAncestor.add(conn);
		start.translate(1, 0);
		assertTrue(count == 1);
	}

	public void testMoveEverything() {
		count = 0;
		commonAncestor.translate(5, 5);
		assertTrue(count == 0);
	}

}
