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

import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PolylineConnection;
import org.eclipse.gef3.EditPolicy;
import org.eclipse.gef3.editparts.AbstractConnectionEditPart;
import org.eclipse.gef3.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef3.examples.digraph2.policy.Digraph2ConnectionEditPolicy;

/**
 * The edit part which describes an edge in the directed graph.
 * 
 * @author Anthony Hunter
 */
public class Digraph2EdgeEditPart extends AbstractConnectionEditPart {

	/*
	 * @see org.eclipse.gef3.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new Digraph2ConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
	}

	/*
	 * @see org.eclipse.gef3.editparts.AbstractConnectionEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new PolylineConnection();
	}

}
