/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.examples.swt;

import java.util.Iterator;

import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.Ellipse;
import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.FlowLayout;
import org.eclipse.draw2dl.FreeformLayout;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.ImageFigure;
import org.eclipse.draw2dl.Label;
import org.eclipse.draw2dl.MarginBorder;
import org.eclipse.draw2dl.PolylineShape;
import org.eclipse.draw2dl.geometry.Point;
import org.eclipse.draw2dl.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

/**
 * 
 * This snippet shows how to create a curved connection using Zest.
 * 
 * @author Ian Bull
 * 
 */
public class GraphSnippet13 {

	public static IFigure createPersonFigure(Image headImage) {
		Figure person = new Figure();
		person.setLayoutManager(new FreeformLayout());
		IFigure head = null;
		if (headImage != null) {
			headImage = new Image(headImage.getDevice(), headImage
					.getImageData().scaledTo(40, 50));
			head = new ImageFigure(headImage);
		} else
			head = new Ellipse();
		head.setSize(40, 50);

		PolylineShape body = new PolylineShape();
		body.setLineWidth(1);
		body.setStart(new Point(20, 40));
		body.setEnd(new Point(20, 100));
		body.setBounds(new Rectangle(0, 0, 40, 100));

		PolylineShape leftLeg = new PolylineShape();
		leftLeg.setLineWidth(1);
		leftLeg.setStart(new Point(20, 100));
		leftLeg.setEnd(new Point(0, 130));
		leftLeg.setBounds(new Rectangle(0, 0, 40, 130));

		PolylineShape rightLeg = new PolylineShape();
		rightLeg.setLineWidth(1);
		rightLeg.setStart(new Point(20, 100));
		rightLeg.setEnd(new Point(40, 130));
		rightLeg.setBounds(new Rectangle(0, 0, 40, 130));

		PolylineShape leftArm = new PolylineShape();
		leftArm.setLineWidth(1);
		leftArm.setStart(new Point(20, 60));
		leftArm.setEnd(new Point(0, 50));
		leftArm.setBounds(new Rectangle(0, 0, 40, 130));

		PolylineShape rightArm = new PolylineShape();
		rightArm.setLineWidth(1);
		rightArm.setStart(new Point(20, 60));
		rightArm.setEnd(new Point(40, 50));
		rightArm.setBounds(new Rectangle(0, 0, 40, 130));

		person.add(head);
		person.add(body);
		person.add(leftLeg);
		person.add(rightLeg);
		person.add(leftArm);
		person.add(rightArm);
		person.setSize(40, 130);
		return person;
	}

	public static void main(String[] args) {
		final Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("GraphSnippet11");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		final Graph g = new Graph(shell, SWT.NONE);
		g.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				Iterator iter = g.getSelection().iterator();
				while (iter.hasNext()) {
					Object o = iter.next();
					if (o instanceof CGraphNode) {
						IFigure figure = ((CGraphNode) o).getFigure();
						figure.setBackgroundColor(ColorConstants.blue);
						figure.setForegroundColor(ColorConstants.blue);
					}
				}
				iter = g.getNodes().iterator();
				while (iter.hasNext()) {
					Object o = iter.next();
					if (o instanceof CGraphNode) {
						if (!g.getSelection().contains(o)) {
							((CGraphNode) o).getFigure().setBackgroundColor(
									ColorConstants.black);
							((CGraphNode) o).getFigure().setForegroundColor(
									ColorConstants.black);
						}
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Image zx = new Image(d, "icons/zxsnow.png");
		IFigure tooltip = new Figure();
		tooltip.setBorder(new MarginBorder(5, 5, 5, 5));
		FlowLayout layout = new FlowLayout(false);
		layout.setMajorSpacing(3);
		layout.setMinorAlignment(3);
		tooltip.setLayoutManager(new FlowLayout(false));
		tooltip.add(new ImageFigure(zx));
		tooltip.add(new Label("Name: " + "Chris Aniszczyk"));
		tooltip.add(new Label("Location: " + "Austin, Texas"));

		Image ibull = new Image(d, "icons/ibull.jpg");
		GraphContainer c1 = new GraphContainer(g, SWT.NONE);
		c1.setText("Canada");
		GraphContainer c2 = new GraphContainer(g, SWT.NONE);
		c2.setText("USA");

		GraphNode n1 = new GraphNode(c1, SWT.NONE, "Ian B.");
		n1.setSize(200, 100);
		GraphNode n2 = new GraphNode(c2, SWT.NONE, "Chris A.");
		n2.setTooltip(tooltip);

		GraphConnection connection = new GraphConnection(g,
				ZestStyles.CONNECTIONS_DIRECTED, n1, n2);
		connection.setCurveDepth(-30);
		GraphConnection connection2 = new GraphConnection(g,
				ZestStyles.CONNECTIONS_DIRECTED, n2, n1);
		connection2.setCurveDepth(-30);

		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		zx.dispose();
		ibull.dispose();
	}

}
