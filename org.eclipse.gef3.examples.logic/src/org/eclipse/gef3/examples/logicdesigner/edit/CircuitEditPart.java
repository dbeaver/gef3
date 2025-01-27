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
package org.eclipse.gef3.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2dl.ConnectionAnchor;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.IScrollableFigure;
import org.eclipse.draw2dl.XYLayout;
import org.eclipse.draw2dl.geometry.Rectangle;

import org.eclipse.gef3.AccessibleAnchorProvider;
import org.eclipse.gef3.AutoexposeHelper;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.ExposeHelper;
import org.eclipse.gef3.MouseWheelHelper;
import org.eclipse.gef3.editparts.IScrollableEditPart;
import org.eclipse.gef3.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef3.editparts.ViewportExposeHelper;
import org.eclipse.gef3.editparts.ViewportMouseWheelHelper;
import org.eclipse.gef3.editpolicies.ScrollableSelectionFeedbackEditPolicy;

import org.eclipse.gef3.examples.logicdesigner.figures.CircuitFigure;
import org.eclipse.gef3.examples.logicdesigner.figures.FigureFactory;

/**
 * Holds a circuit, which is a container capable of holding other
 * LogicEditParts.
 */
public class CircuitEditPart extends LogicContainerEditPart implements
		IScrollableEditPart {

	private static final String SCROLLABLE_SELECTION_FEEDBACK = "SCROLLABLE_SELECTION_FEEDBACK"; //$NON-NLS-1$

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new LogicXYLayoutEditPolicy(
				(XYLayout) getContentPane().getLayoutManager()));
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new ContainerHighlightEditPolicy());
		installEditPolicy(SCROLLABLE_SELECTION_FEEDBACK,
				new ScrollableSelectionFeedbackEditPolicy());
	}

	/**
	 * Creates a new Circuit Figure and returns it.
	 * 
	 * @return Figure representing the circuit.
	 */
	protected IFigure createFigure() {
		return FigureFactory.createNewCircuit();
	}

	public Object getAdapter(Class key) {
		if (key == AutoexposeHelper.class)
			return new ViewportAutoexposeHelper(this);
		if (key == ExposeHelper.class)
			return new ViewportExposeHelper(this);
		if (key == AccessibleAnchorProvider.class)
			return new DefaultAccessibleAnchorProvider() {
				public List getSourceAnchorLocations() {
					List list = new ArrayList();
					Vector sourceAnchors = getNodeFigure()
							.getSourceConnectionAnchors();
					Vector targetAnchors = getNodeFigure()
							.getTargetConnectionAnchors();
					for (int i = 0; i < sourceAnchors.size(); i++) {
						ConnectionAnchor sourceAnchor = (ConnectionAnchor) sourceAnchors
								.get(i);
						ConnectionAnchor targetAnchor = (ConnectionAnchor) targetAnchors
								.get(i);
						list.add(new Rectangle(
								sourceAnchor.getReferencePoint(), targetAnchor
										.getReferencePoint()).getCenter());
					}
					return list;
				}

				public List getTargetAnchorLocations() {
					return getSourceAnchorLocations();
				}
			};
		if (key == MouseWheelHelper.class)
			return new ViewportMouseWheelHelper(this);
		return super.getAdapter(key);
	}

	/**
	 * Returns the Figure of this as a CircuitFigure.
	 * 
	 * @return CircuitFigure of this.
	 */
	protected CircuitFigure getCircuitBoardFigure() {
		return (CircuitFigure) getFigure();
	}

	public IFigure getContentPane() {
		return getCircuitBoardFigure().getContentsPane();
	}

	public IScrollableFigure getScrollableFigure() {
		return (IScrollableFigure) getFigure();
	}

}
