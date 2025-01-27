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
package org.eclipse.draw2dl.graph;

/**
 * Performs a graph layout on a <code>CompoundDirectedGraph</code>. The input
 * format is the same as for {@link org.eclipse.draw2dl.graph.DirectedGraphLayout}. All nodes, including
 * subgraphs and their children, should be added to the
 * {@link DirectedGraph#nodes} field.
 * <P>
 * The requirements for this algorithm are the same as those of
 * <code>DirectedGraphLayout</code>, with the following exceptions:
 * <UL>
 * <LI>There is an implied edge between a subgraph and each of its member nodes.
 * These edges form the containment graph <EM>T</EM>. Thus, the compound
 * directed graph <EM>CG</EM> is said to be connected iff Union(<EM>G</EM>,
 * <EM>T</EM>) is connected, where G represents the given nodes (including
 * subgraphs) and edges.
 * 
 * <LI>This algorithm will remove any compound cycles found in the input graph
 * <em>G</em> by inverting edges according to a heuristic until no more cycles
 * are found. A compound cycle is defined as: a cycle comprised of edges from
 * <EM>G</EM>, <EM>T</EM>, and <em>T<SUP>-1</SUP></em>, in the form
 * (c<SUP>*</SUP>e<SUP>+</SUP>p<SUP>*</SUP>e<SUP>+</SUP>)*, where
 * <em>T<SUP>-1</SUP></em> is the backwards graph of <EM>T</EM>, c element of T,
 * e element of G, and p element of T<SUP>-1</SUP>.
 * </UL>
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
public final class CompoundDirectedGraphLayout extends DirectedGraphLayout {

	void init() {
		steps.add(new org.eclipse.draw2dl.graph.CompoundTransposeMetrics());
		steps.add(new org.eclipse.draw2dl.graph.CompoundBreakCycles());
		steps.add(new org.eclipse.draw2dl.graph.RouteEdges());
		steps.add(new org.eclipse.draw2dl.graph.ConvertCompoundGraph());
		steps.add(new org.eclipse.draw2dl.graph.InitialRankSolver());
		steps.add(new org.eclipse.draw2dl.graph.TightSpanningTreeSolver());
		steps.add(new org.eclipse.draw2dl.graph.RankAssignmentSolver());
		steps.add(new org.eclipse.draw2dl.graph.CompoundPopulateRanks());
		steps.add(new org.eclipse.draw2dl.graph.CompoundVerticalPlacement());
		steps.add(new org.eclipse.draw2dl.graph.MinCross(new org.eclipse.draw2dl.graph.CompoundRankSorter()));
		steps.add(new org.eclipse.draw2dl.graph.SortSubgraphs());
		steps.add(new org.eclipse.draw2dl.graph.CompoundHorizontalPlacement());
	}

}
