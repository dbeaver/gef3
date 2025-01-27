/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef3.examples.flow.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2dl.AbsoluteBendpoint;
import org.eclipse.draw2dl.BendpointConnectionRouter;
import org.eclipse.draw2dl.Connection;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PolygonDecoration;
import org.eclipse.draw2dl.PolylineConnection;
import org.eclipse.draw2dl.graph.CompoundDirectedGraph;
import org.eclipse.draw2dl.graph.Edge;
import org.eclipse.draw2dl.graph.Node;
import org.eclipse.draw2dl.graph.NodeList;
import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.editparts.AbstractConnectionEditPart;
import org.eclipse.gef3.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef3.examples.flow.policies.TransitionEditPolicy;
import org.eclipse.gef3.editparts.AbstractEditPart;

/**
 * @author hudsonr Created on Jul 16, 2003
 */
public class TransitionPart extends AbstractConnectionEditPart {

	protected void applyGraphResults(CompoundDirectedGraph graph, Map map) {
		Edge e = (Edge) map.get(this);
		NodeList nodes = e.vNodes;
		PolylineConnection conn = (PolylineConnection) getConnectionFigure();
		conn.setTargetDecoration(new PolygonDecoration());
		if (nodes != null) {
			List bends = new ArrayList();
			for (int i = 0; i < nodes.size(); i++) {
				Node vn = nodes.getNode(i);
				int x = vn.x;
				int y = vn.y;
				if (e.isFeedback()) {
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
					bends.add(new AbsoluteBendpoint(x, y));
				} else {
					bends.add(new AbsoluteBendpoint(x, y));
					bends.add(new AbsoluteBendpoint(x, y + vn.height));
				}
			}
			conn.setRoutingConstraint(bends);
		} else {
			conn.setRoutingConstraint(Collections.EMPTY_LIST);
		}
	}

	/**
	 * @see AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new TransitionEditPolicy());
	}

	/**
	 * @see AbstractConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		PolylineConnection conn = (PolylineConnection) super.createFigure();
		conn.setConnectionRouter(new BendpointConnectionRouter() {
			public void route(Connection conn) {
				GraphAnimation.recordInitialState(conn);
				if (!GraphAnimation.playbackState(conn))
					super.route(conn);
			}
		});

		conn.setTargetDecoration(new PolygonDecoration());
		return conn;
	}

	/**
	 * @see org.eclipse.gef3.EditPart#setSelected(int)
	 */
	public void setSelected(int value) {
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
			((PolylineConnection) getFigure()).setLineWidth(2);
		else
			((PolylineConnection) getFigure()).setLineWidth(1);
	}

	public void contributeToGraph(CompoundDirectedGraph graph, Map map) {
		GraphAnimation.recordInitialState(getConnectionFigure());
		Node source = (Node) map.get(getSource());
		Node target = (Node) map.get(getTarget());
		Edge e = new Edge(this, source, target);
		e.weight = 2;
		graph.edges.add(e);
		map.put(this, e);
	}

}
