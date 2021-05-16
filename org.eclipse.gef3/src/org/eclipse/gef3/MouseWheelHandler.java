/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

/**
 * MouseWheelHandlers can be
 * {@link org.eclipse.gef3.EditPartViewer#setProperty(String, Object) registered}
 * on an EditPartViewer with keys generated by the KeyGenerator. GEF tools
 * delegate handling of mouse-wheel events to MouseWheelHandlers, if there are
 * no drag trackers active.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public interface MouseWheelHandler {

	/**
	 * Handles mouse-wheel events. If the given event was handled in some way,
	 * its {@link Event#doit doit} field should be set to false so as to prevent
	 * further processing of that event.
	 * 
	 * @param event
	 *            The SWT event that was generated as a result of the
	 *            mouse-wheel scrolling
	 * @param viewer
	 *            The originating viewer
	 */
	void handleMouseWheel(Event event, EditPartViewer viewer);

	/**
	 * A utility class used to generate keys from a given stateMask. A
	 * MouseWheelHandler registered with such a key would only be asked to
	 * handle mouse-wheel events that have the same stateMask as the one that
	 * was used to generate the key.
	 * 
	 * @author Pratik Shah
	 * @since 3.1
	 */
	public class KeyGenerator {
		/**
		 * The returned String is guaranteed to be equal for two different
		 * invocations with the same stateMask.
		 * <p>
		 * Valid stateMasks are SWT.NONE, SWT.CTRL, SWT.COMMAND, SWT.ALT,
		 * SWT.SHIFT, SWT.MOD1, SWT.MOD2, SWT.MOD3, SWT.MOD4, SWT.BUTTON* or any
		 * combination thereof.
		 * 
		 * @param stateMask
		 *            the state indicating which buttons/modifiers are active
		 * @return key for the given stateMask
		 * @throws IllegalArgumentException
		 *             if the given stateMask is not valid
		 */
		public static String getKey(int stateMask) {
			if ((stateMask & ~(SWT.BUTTON_MASK | SWT.MODIFIER_MASK)) != 0)
				throw new IllegalArgumentException(
						"Illegal state: " + stateMask); //$NON-NLS-1$
			return "MouseWheelHandler(" + stateMask + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}