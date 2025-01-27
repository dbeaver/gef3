/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef3.examples.digraph2.editpart;

import java.util.List;

import org.eclipse.draw2dl.ConnectionAnchor;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.gef3.ConnectionEditPart;
import org.eclipse.gef3.NodeEditPart;
import org.eclipse.gef3.Request;
import org.eclipse.gef3.examples.digraph1.editpart.Digraph1NodeEditPart;
import org.eclipse.gef3.examples.digraph2.figure.Digraph2ConnectionAnchor;
import org.eclipse.gef3.examples.digraph2.figure.Digraph2NodeFigure;
import org.eclipse.gef3.examples.digraph2.model.Digraph2Edge;
import org.eclipse.gef3.examples.digraph2.model.Digraph2Node;

/**
 * The edit part which describes a node in the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2NodeEditPart extends Digraph1NodeEditPart implements
		NodeEditPart {

	private Digraph2ConnectionAnchor sourceAnchor;

	private Digraph2ConnectionAnchor targetAnchor;

	/*
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		int number = ((Digraph2Node) getModel()).getNumber();
		Digraph2NodeFigure nodeFigure = new Digraph2NodeFigure(number);
		this.targetAnchor = new Digraph2ConnectionAnchor(nodeFigure);
		this.sourceAnchor = new Digraph2ConnectionAnchor(nodeFigure);
		return nodeFigure;
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	@Override
	protected List<Digraph2Edge> getModelSourceConnections() {
		return ((Digraph2Node) getModel()).getSourceEdges();
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	@Override
	protected List<Digraph2Edge> getModelTargetConnections() {
		return ((Digraph2Node) getModel()).getTargetEdges();
	}

	/*
	 * @see org.eclipse.gef3.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef3.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return this.sourceAnchor;
	}

	/*
	 * @see org.eclipse.gef3.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef3.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return this.sourceAnchor;
	}

	/*
	 * @see org.eclipse.gef3.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef3.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return this.targetAnchor;
	}

	/*
	 * @see org.eclipse.gef3.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef3.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return this.targetAnchor;
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractGraphicalEditPart#registerVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		Digraph2NodeFigure nodeFigure = (Digraph2NodeFigure) getFigure();
		Point location = nodeFigure.getRectangleFigure().getLocation();
		Dimension size = nodeFigure.getRectangleFigure().getSize();
		Digraph2GraphEditPart graph = (Digraph2GraphEditPart) getParent();
		Rectangle constraint = new Rectangle(location, size);
		graph.setLayoutConstraint(this, nodeFigure, constraint);
	}

}
