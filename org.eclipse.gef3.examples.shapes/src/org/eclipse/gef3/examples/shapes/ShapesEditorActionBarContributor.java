/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
�* http://www.eclipse.org/legal/epl-v10.html
�*
�* Contributors:
�*����Elias Volanakis - initial API and implementation
�*******************************************************************************/
package org.eclipse.gef3.examples.shapes;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef3.ui.actions.ActionBarContributor;
import org.eclipse.gef3.ui.actions.DeleteRetargetAction;
import org.eclipse.gef3.ui.actions.RedoRetargetAction;
import org.eclipse.gef3.ui.actions.UndoRetargetAction;

/**
 * Contributes actions to a toolbar. This class is tied to the editor in the
 * definition of editor-extension (see plugin.xml).
 * 
 * @author Elias Volanakis
 */
public class ShapesEditorActionBarContributor extends ActionBarContributor {

	/**
	 * Create actions managed by this contributor.
	 * 
	 * @see org.eclipse.gef3.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
	}

	/**
	 * Add actions to the given toolbar.
	 * 
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef3.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		// currently none
	}

}