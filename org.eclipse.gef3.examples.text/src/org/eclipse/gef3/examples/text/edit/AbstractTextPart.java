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

package org.eclipse.gef3.examples.text.edit;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2dl.text.FlowFigure;
import org.eclipse.gef3.DragTracker;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef3.examples.text.model.ModelElement;
import org.eclipse.gef3.examples.text.tools.SelectionRangeDragTracker;

/**
 * @since 3.1
 */
public abstract class AbstractTextPart extends AbstractGraphicalEditPart
		implements TextEditPart, PropertyChangeListener {

	public boolean acceptsCaret() {
		return true;
	}

	/**
	 * @see org.eclipse.gef3.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		ModelElement model = (ModelElement) getModel();
		model.addPropertyChangeListener(this);
	}

	/**
	 * @see org.eclipse.gef3.EditPart#deactivate()
	 */
	public void deactivate() {
		ModelElement model = (ModelElement) getModel();
		model.removePropertyChangeListener(this);
		super.deactivate();
	}

	public DragTracker getDragTracker(Request request) {
		return new SelectionRangeDragTracker(this);
	}

	protected TextEditPart getTextParent() {
		return (TextEditPart) getParent();
	}

	public void setSelection(int start, int end) {
		FlowFigure ff = (FlowFigure) getFigure();
		ff.setSelection(start, end);
	}

}
