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

import java.util.HashSet;
import java.util.Set;

/**
 * Calculates the X-coordinates for nodes in a compound directed graph.
 * 
 * @author Randy Hudson
 * @since 2.1.2
 */
class CompoundHorizontalPlacement extends org.eclipse.draw2dl.graph.HorizontalPlacement {

	class LeftRight {
		// $TODO Delete and use NodePair class, equivalent
		Object left, right;

		LeftRight(Object l, Object r) {
			left = l;
			right = r;
		}

		public boolean equals(Object obj) {
			LeftRight entry = (LeftRight) obj;
			return entry.left.equals(left) && entry.right.equals(right);
		}

		public int hashCode() {
			return left.hashCode() ^ right.hashCode();
		}
	}

	Set entries = new HashSet();

	/**
	 * @see org.eclipse.graph.HorizontalPlacement#applyGPrime()
	 */
	void applyGPrime() {
		super.applyGPrime();
		NodeList subgraphs = ((org.eclipse.draw2dl.graph.CompoundDirectedGraph) graph).subgraphs;
		for (int i = 0; i < subgraphs.size(); i++) {
			org.eclipse.draw2dl.graph.Subgraph s = (org.eclipse.draw2dl.graph.Subgraph) subgraphs.get(i);
			s.x = s.left.x;
			s.width = s.right.x + s.right.width - s.x;
		}
	}

	/**
	 * @see org.eclipse.draw2dl.graph.HorizontalPlacement#buildRankSeparators(org.eclipse.draw2dl.graph.RankList)
	 */
	void buildRankSeparators(RankList ranks) {
		org.eclipse.draw2dl.graph.CompoundDirectedGraph g = (CompoundDirectedGraph) graph;

		Rank rank;
		for (int row = 0; row < g.ranks.size(); row++) {
			rank = g.ranks.getRank(row);
			org.eclipse.draw2dl.graph.Node n = null, prev = null;
			for (int j = 0; j < rank.size(); j++) {
				n = rank.getNode(j);
				if (prev == null) {
					org.eclipse.draw2dl.graph.Node left = addSeparatorsLeft(n, null);
					if (left != null) {
						org.eclipse.draw2dl.graph.Edge e = new org.eclipse.draw2dl.graph.Edge(graphLeft, getPrime(left), 0, 0);
						prime.edges.add(e);
						e.delta = graph.getPadding(n).left
								+ graph.getMargin().left;
					}

				} else {
					org.eclipse.draw2dl.graph.Subgraph s = org.eclipse.draw2dl.graph.GraphUtilities.getCommonAncestor(prev, n);
					org.eclipse.draw2dl.graph.Node left = addSeparatorsRight(prev, s);
					org.eclipse.draw2dl.graph.Node right = addSeparatorsLeft(n, s);
					createEdge(left, right);
				}
				prev = n;
			}
			if (n != null)
				addSeparatorsRight(n, null);
		}
	}

	void createEdge(org.eclipse.draw2dl.graph.Node left, org.eclipse.draw2dl.graph.Node right) {
		LeftRight entry = new LeftRight(left, right);
		if (entries.contains(entry))
			return;
		entries.add(entry);
		int separation = left.width + graph.getPadding(left).right
				+ graph.getPadding(right).left;
		prime.edges
				.add(new org.eclipse.draw2dl.graph.Edge(getPrime(left), getPrime(right), separation, 0));
	}

	org.eclipse.draw2dl.graph.Node addSeparatorsLeft(org.eclipse.draw2dl.graph.Node n, org.eclipse.draw2dl.graph.Subgraph graph) {
		org.eclipse.draw2dl.graph.Subgraph parent = n.getParent();
		while (parent != graph && parent != null) {
			createEdge(getLeft(parent), n);
			n = parent.left;
			parent = parent.getParent();
		}
		return n;
	}

	org.eclipse.draw2dl.graph.Node addSeparatorsRight(org.eclipse.draw2dl.graph.Node n, org.eclipse.draw2dl.graph.Subgraph graph) {
		org.eclipse.draw2dl.graph.Subgraph parent = n.getParent();
		while (parent != graph && parent != null) {
			createEdge(n, getRight(parent));
			n = parent.right;
			parent = parent.getParent();
		}
		return n;
	}

	org.eclipse.draw2dl.graph.Node getLeft(org.eclipse.draw2dl.graph.Subgraph s) {
		if (s.left == null) {
			s.left = new org.eclipse.draw2dl.graph.SubgraphBoundary(s, graph.getPadding(s), 1);
			s.left.rank = (s.head.rank + s.tail.rank) / 2;

			org.eclipse.draw2dl.graph.Node head = getPrime(s.head);
			org.eclipse.draw2dl.graph.Node tail = getPrime(s.tail);
			org.eclipse.draw2dl.graph.Node left = getPrime(s.left);
			org.eclipse.draw2dl.graph.Node right = getPrime(getRight(s));
			prime.edges.add(new org.eclipse.draw2dl.graph.Edge(left, right, s.width, 0));
			prime.edges.add(new org.eclipse.draw2dl.graph.Edge(left, head, 0, 1));
			prime.edges.add(new org.eclipse.draw2dl.graph.Edge(head, right, 0, 1));
			prime.edges.add(new org.eclipse.draw2dl.graph.Edge(left, tail, 0, 1));
			prime.edges.add(new Edge(tail, right, 0, 1));
		}
		return s.left;
	}

	org.eclipse.draw2dl.graph.Node getRight(Subgraph s) {
		if (s.right == null) {
			s.right = new org.eclipse.draw2dl.graph.SubgraphBoundary(s, graph.getPadding(s), 3);
			s.right.rank = (s.head.rank + s.tail.rank) / 2;
		}
		return s.right;
	}

	org.eclipse.draw2dl.graph.Node getPrime(org.eclipse.draw2dl.graph.Node n) {
		org.eclipse.draw2dl.graph.Node nPrime = get(n);
		if (nPrime == null) {
			nPrime = new Node(n);
			prime.nodes.add(nPrime);
			map(n, nPrime);
		}
		return nPrime;
	}

	public void visit(DirectedGraph g) {
		super.visit(g);
	}

}
