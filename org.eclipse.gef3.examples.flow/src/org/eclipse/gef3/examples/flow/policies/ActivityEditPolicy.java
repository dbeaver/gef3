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
package org.eclipse.gef3.examples.flow.policies;

import org.eclipse.gef3.commands.Command;
import org.eclipse.gef3.editpolicies.ComponentEditPolicy;
import org.eclipse.gef3.examples.flow.model.Activity;
import org.eclipse.gef3.examples.flow.model.StructuredActivity;
import org.eclipse.gef3.examples.flow.model.commands.DeleteCommand;
import org.eclipse.gef3.requests.GroupRequest;

/**
 * @author Daniel Lee
 */
public class ActivityEditPolicy extends ComponentEditPolicy {

	/**
	 * @see ComponentEditPolicy#createDeleteCommand(GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		StructuredActivity parent = (StructuredActivity) (getHost().getParent()
				.getModel());
		DeleteCommand deleteCmd = new DeleteCommand();
		deleteCmd.setParent(parent);
		deleteCmd.setChild((Activity) (getHost().getModel()));
		return deleteCmd;
	}

}
