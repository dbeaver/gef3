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
package org.eclipse.gef3.tools;

import org.eclipse.jface.viewers.CellEditor;

/**
 * Constraint for placing {@link org.eclipse.jface.viewers.CellEditor
 * CellEditors}.
 */
public interface CellEditorLocator {

	/**
	 * Relocates a CellEditor.
	 * 
	 * @param celleditor
	 *            the CellEditor
	 */
	void relocate(CellEditor celleditor);

}
