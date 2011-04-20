package sequenceplanner.model.SOP;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sequenceplanner.model.data.OperationData;

/**
 * Remove unnecessary nodes recursively in a {@link ISopNode}.<br/>
 * No sequenceing can occur in the {@link ISopNode} entered.
 * @author patrik
 */
public class ResolveSopNode {

    private ISopNodeToolbox mSNToolbox = new SopNodeToolboxSetOfOperations();

    public ResolveSopNode(final ISopNode iNode) {
        resolveNodesOfTheSameTypeOnTheSameLevel(iNode);
        resolveNodesOfTheSameTypeOnSucceededLevels(iNode);
        resolveNodesOfTheSameTypeOnTheSameLevel(iNode);
        resolveNodesOfTheSameTypeWithIntermediateSOP(iNode);
        resolveNodesOfTheSameTypeOnTheSameLevel(iNode);
    }

    /**
     * E.g.:---------------<br/>
     * node1: type operation, sequence set {node2,node6}<br/>
     * node2: type parallel, sequence set {node7}<br/>
     * node6: type operation, sequence set {}<br/>
     * node7: type SOP, sequence set {node3}<br/>
     * node3: type parallel, sequence set {node4,node5}<br/>
     * node4: type operation, sequence set {}<br/>
     * node5: type operation, sequence set {}<br/>
     * resolve(node1) gives:<br/>
     * node1: type operation, sequence set {node2,node6}<br/>
     * node2: type parallel, sequence set {node4,node5}<br/>
     * node6: type operation, sequence set {}<br/>
     * node4: type operation, sequence set {}<br/>
     * node5: type operation, sequence set {}<br/>
     * --------------------<br/>
     * @param iNode root node to resolve
     */
    private void resolveNodesOfTheSameTypeWithIntermediateSOP(final ISopNode iNode) {
        if (!(iNode.getNodeType() instanceof OperationData)) {
            final String rootNodeType = (String) iNode.getNodeType();
            if (!rootNodeType.equals("SOP")) {
                for (final ISopNode childNode : iNode.getFirstNodesInSequencesAsSet()) {
                    if (!(childNode.getNodeType() instanceof OperationData)) {
                        final String childNodeType = (String) childNode.getNodeType();
                        if (childNodeType.equals("SOP") && childNode.getFirstNodesInSequencesAsSet().size() == 1) {
                            final ISopNode childChildNode = childNode.getFirstNodesInSequencesAsSet().iterator().next();
                            if (!(childChildNode.getNodeType() instanceof OperationData)) {
                                final String childChildNodeType = (String) childChildNode.getNodeType();
                                if (rootNodeType.equals(childChildNodeType)) {
                                    //Move children to childChild from childChild to root
                                    for (final ISopNode childChildChildNode : childChildNode.getFirstNodesInSequencesAsSet()) {
                                        iNode.addNodeToSequenceSet(childChildChildNode);
                                    }
                                    //Remove child
                                    mSNToolbox.removeNode(childNode, iNode);
                                }
                            }
                        }
                    }
                }
            }
        }

        //Go to level below
        for (final ISopNode node : iNode.getFirstNodesInSequencesAsSet()) {
            resolveNodesOfTheSameTypeWithIntermediateSOP(node);
        }
    }

    /**
     * E.g.:---------------<br/>
     * node1: type operation, sequence set {node2}<br/>
     * node2: type parallel, sequence set {node3,node6}<br/>
     * node6: type operation, sequence set {}<br/>
     * node3: type parallel, sequence set {node4,node5}<br/>
     * node4: type operation, sequence set {}<br/>
     * node5: type operation, sequence set {}<br/>
     * resolveNodesOfTheSameTypeOnSucceededLevels(node1) gives:<br/>
     * node1: type operation, sequence set {node2}<br/>
     * node2: type parallel, sequence set {node6,node4,node5}<br/>
     * node4: type operation, sequence set {}<br/>
     * node5: type operation, sequence set {}<br/>
     * node6: type operation, sequence set {}<br/>
     * --------------------<br/>
     * @param iNode root to resolve
     */
    private void resolveNodesOfTheSameTypeOnSucceededLevels(final ISopNode iNode) {
        if (!(iNode.getNodeType() instanceof OperationData)) {
            final String rootNodeType = (String) iNode.getNodeType();
            for (final ISopNode childNode : iNode.getFirstNodesInSequencesAsSet()) {
                if (!(childNode.getNodeType() instanceof OperationData)) {
                    final String childNodeType = (String) childNode.getNodeType();
                    if (rootNodeType.equals(childNodeType)) {
                        //Move children to child from child to root
                        for (final ISopNode childChildNode : childNode.getFirstNodesInSequencesAsSet()) {
                            iNode.addNodeToSequenceSet(childChildNode);
                        }
                        //Remove child
                        mSNToolbox.removeNode(childNode, iNode);
                    }
                }
            }
        }

        //Go to level below
        for (final ISopNode node : iNode.getFirstNodesInSequencesAsSet()) {
            resolveNodesOfTheSameTypeOnSucceededLevels(node);
        }
    }

    /**
     * E.g.<br/>
     * node1: type operation, sequence set {node2,node3,node4}<br/>
     * node2: type alternative, sequence set {node5,node6}<br/>
     * node3: type alternative, sequence set {node6,node7}<br/>
     * node4: type alternative, sequence set {node7,node5}<br/>
     * resolveNodesOfTheSameTypeOnTheSameLevel(node1) gives:<br/>
     * node1: type operation, sequence set {node2}<br/>
     * node2: type alternative, sequence set {node5,node6,node7}<br/>
     * @param iNode root to resolve
     */
    private void resolveNodesOfTheSameTypeOnTheSameLevel(final ISopNode iNode) {
        //Collect information
        Map<String, Set<ISopNode>> nodeTypeSetMap = new HashMap<String, Set<ISopNode>>();
        for (final ISopNode node : iNode.getFirstNodesInSequencesAsSet()) {
            //All node types except OperationData are of interest
            if (!(node.getNodeType() instanceof OperationData)) {
                final String nodeType = (String) node.getNodeType();
                if (!nodeTypeSetMap.containsKey(nodeType)) {
                    nodeTypeSetMap.put(nodeType, new HashSet<ISopNode>());
                }
                nodeTypeSetMap.get(nodeType).add(node);
            }
        }

        //Work with information, i.e. merge nodes.
        for (final String key : nodeTypeSetMap.keySet()) {
            final Set<ISopNode> nodeSet = nodeTypeSetMap.get(key);
            if (nodeSet.size() > 1) {
                //create new node
                final ISopNode newNode = mSNToolbox.createNode(key, iNode);
                //move to new node
                for (final ISopNode node : nodeSet) {
                    for (final ISopNode nodeToMove : node.getFirstNodesInSequencesAsSet()) {
                        newNode.addNodeToSequenceSet(nodeToMove);
                    }
                    //Remove old node
                    mSNToolbox.removeNode(node, iNode);
                }
            }
        }

        //Go to level below
        for (final ISopNode node : iNode.getFirstNodesInSequencesAsSet()) {
            resolveNodesOfTheSameTypeOnTheSameLevel(node);
        }
    }
}
