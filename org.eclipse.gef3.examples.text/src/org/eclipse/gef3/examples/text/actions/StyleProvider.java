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

package org.eclipse.gef3.examples.text.actions;

/**
 * @since 3.1
 */
public interface StyleProvider {

	void addStyleListener(StyleListener listener);

	Object getStyle(String styleID);

	Object getStyleState(String styleID);

	void removeStyleListener(StyleListener listener);

	void setStyle(String styleID, Object newValue);

}
