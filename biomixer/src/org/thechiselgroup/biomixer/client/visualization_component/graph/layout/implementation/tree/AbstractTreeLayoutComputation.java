/*******************************************************************************
 * Copyright 2012 David Rusk 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 *******************************************************************************/
package org.thechiselgroup.biomixer.client.visualization_component.graph.layout.implementation.tree;

import java.util.List;
import java.util.Set;

import org.thechiselgroup.biomixer.client.core.error_handling.ErrorHandler;
import org.thechiselgroup.biomixer.client.core.geometry.PointDouble;
import org.thechiselgroup.biomixer.client.core.util.executor.Executor;
import org.thechiselgroup.biomixer.client.visualization_component.graph.layout.LayoutGraph;
import org.thechiselgroup.biomixer.client.visualization_component.graph.layout.LayoutNode;
import org.thechiselgroup.biomixer.client.visualization_component.graph.layout.animations.NodeAnimator;
import org.thechiselgroup.biomixer.client.visualization_component.graph.layout.implementation.AbstractLayoutComputation;

/**
 * The computations for the {@link VerticalTreeLayoutAlgorith} and
 * {@link HorizontalTreeLayoutAlgorithm} are basically the same except for their
 * orientation. This class contains the common functionality, and has template
 * methods for any calculations specific to the vertical or horizontal tree.
 * 
 * @author drusk
 * 
 */
public abstract class AbstractTreeLayoutComputation extends
        AbstractLayoutComputation {

    private static final int animationDuration = 1000;

    private final boolean reversed;

    /**
     * Traversing the dag normally results in a vertical tree with arrows
     * pointing up or horizontal tree with arrows pointing left. To get a
     * vertical tree with arrows pointing down, or a horizontal tree with arrows
     * pointing right, set <code>reversed</code> to true
     */
    protected AbstractTreeLayoutComputation(LayoutGraph graph,
            Executor executor, ErrorHandler errorHandler,
            NodeAnimator nodeAnimator, boolean reversed) {
        super(graph, executor, errorHandler, nodeAnimator);
        this.reversed = reversed;
    }

    @Override
    protected boolean computeIteration() throws RuntimeException {
        /*
         * XXX need to handle the case where there are cycles in the graph.
         */
        CycleDetector cycleDetector = new CycleDetector(graph);

        /* Anchor the nodes in cycles */
        for (LayoutNode node : cycleDetector.getNodesInCycles()) {
            node.setAnchored(true);
        }

        /*
         * Build up the directed acyclic graph structure from the remaining
         * nodes
         */
        List<DirectedAcyclicGraph> dagsOnGraph = new DirectedAcyclicGraphBuilder()
                .getDirectedAcyclicGraphs(graph);
        int numDagsOnGraph = dagsOnGraph.size();
        assert numDagsOnGraph >= 1 || cycleDetector.hasCycles();

        double availableSecondaryDimensionForEachTree = getAvailableSecondaryDimensionForEachTree(numDagsOnGraph
                + cycleDetector.getNumberOfCycles());

        // traverse each dag
        for (int i = 0; i < dagsOnGraph.size(); i++) {
            DirectedAcyclicGraph dag = dagsOnGraph.get(i);

            double primaryDimensionSpacing = getPrimaryDimensionSpacing(dag
                    .getNumberOfNodesOnLongestPath());
            double currentPrimaryDimension = primaryDimensionSpacing;

            /*
             * Traverse primary dimension, but process along secondary dimension
             * at each step. For a vertical tree that is not reversed, this will
             * be like a typewriter's movement.
             */
            if (reversed) {
                for (int j = dag.getNumberOfNodesOnLongestPath() - 1; j >= 0; j--) {
                    processNodesAlongSecondaryDimension(i, j, dag,
                            availableSecondaryDimensionForEachTree,
                            currentPrimaryDimension);
                    currentPrimaryDimension += primaryDimensionSpacing;
                }
            } else {
                for (int j = 0; j < dag.getNumberOfNodesOnLongestPath(); j++) {
                    processNodesAlongSecondaryDimension(i, j, dag,
                            availableSecondaryDimensionForEachTree,
                            currentPrimaryDimension);
                    currentPrimaryDimension += primaryDimensionSpacing;
                }
            }
        }

        /* Unanchor the nodes in cycles */
        for (LayoutNode node : cycleDetector.getNodesInCycles()) {
            node.setAnchored(false);
        }

        /* Place the nodes in cycles */
        List<Set<LayoutNode>> cycles = cycleDetector.getCycles();
        for (int i = 0; i < cycles.size(); i++) {
            Set<LayoutNode> cycle = cycles.get(i);

            /*
             * XXX place these nodes in a straight line along the primary
             * dimension
             */
            double secondaryDimension = availableSecondaryDimensionForEachTree
                    * (numDagsOnGraph + i + 0.5);
            double primaryDimensionSpacing = getPrimaryDimensionSpacing(cycle
                    .size());
            double currentPrimaryDimension = primaryDimensionSpacing;

            for (LayoutNode node : cycle) {
                PointDouble topLeft = getTopLeftForCentreAt(
                        currentPrimaryDimension, secondaryDimension, node);
                animateTo(node, topLeft, animationDuration);
                currentPrimaryDimension += primaryDimensionSpacing;
            }
        }

        // this is not a continuous layout
        return false;
    }

    protected abstract double getAvailableSecondaryDimensionForEachTree(
            int numDagsOnGraph);

    protected abstract double getPrimaryDimensionSpacing(
            int numberOfNodesOnLongestPath);

    protected abstract PointDouble getTopLeftForCentreAt(
            double currentPrimaryDimension, double currentSecondaryDimension,
            LayoutNode node);

    private void processNodesAlongSecondaryDimension(int i, int j,
            DirectedAcyclicGraph dag,
            double availableSecondaryDimensionForEachTree,
            double currentPrimaryDimension) {

        List<DirectedAcyclicGraphNode> nodesAtDepth = dag
                .getNodesAtDistanceFromRoot(j);

        double secondaryDimensionSpacing = availableSecondaryDimensionForEachTree
                / (nodesAtDepth.size() + 1);

        double currentSecondaryDimension = i
                * availableSecondaryDimensionForEachTree
                + secondaryDimensionSpacing;

        for (DirectedAcyclicGraphNode dagNode : nodesAtDepth) {
            LayoutNode layoutNode = dagNode.getLayoutNode();
            PointDouble topLeft = getTopLeftForCentreAt(
                    currentPrimaryDimension, currentSecondaryDimension,
                    layoutNode);
            animateTo(layoutNode, topLeft, animationDuration);
            currentSecondaryDimension += secondaryDimensionSpacing;
        }

    }
}
