/* 
   Copyright (c) 2012, Kristofer Bengtsson, Sekvensa AB, Chalmers University of Technology
   Developed with the sponsorship of the Defense Advanced Research Projects Agency (DARPA).
   Permission is hereby granted, free of charge, to any person obtaining a copy of this data, including any
   software or models in source or binary form, specifications, algorithms, and documentation (collectively
   "the Data"), to deal in the Data without restriction, including without limitation the rights to use, copy,
   modify, merge, publish, distribute, sublicense, and/or sell copies of the Data, and to permit persons to
   whom the Data is furnished to do so, subject to the following conditions:
   The above copyright notice and this permission notice shall be included in all copies or substantial
   portions of the Data.
   THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
   INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
   PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS,
   SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
   CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
   OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR
   OTHER DEALINGS IN THE DATA.
*/


package sequenceplanner.IO.XML.IntentionalXML;

import java.util.ArrayDeque;
import sequenceplanner.datamodel.condition.Condition;
import sequenceplanner.datamodel.condition.ConditionOperator;
import sequenceplanner.datamodel.condition.ConditionStatement;
import sequenceplanner.datamodel.product.Seam;
import sequenceplanner.model.Model;
import sequenceplanner.model.SOP.SopNode;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode.ConditionType;
import sequenceplanner.model.TreeNode;
import sequenceplanner.model.data.ConditionData;
import sequenceplanner.model.data.OperationData;
import sequenceplanner.model.data.ResourceVariableData;

/**
 * This class should prbably be moved to a general place, but for now 
 * it is specific for when parsing intentional XML
 * 
 * @author kbe
 */
public enum CreateBooking {
    INSTANCE;    

    public void createBookingForSeams(Model model){
        // Obeserve, no hierarchy is handled!!!
        for (TreeNode n : model.getAllOperations()){
            if (n.getNodeData() instanceof OperationData){
                createSeamBooking((OperationData)n.getNodeData(),model);
            }
        }
        
        
//        for (SopNode sop : model.sops){
//            createSeamBooking(sop,model);
//            break;
//        }
    }
    
    public void createBookingForResources(Model model, boolean restrictedMode){
         if (model.sops == null || model.sops.isEmpty()){
            //Test to only tun one operation at the time
                ResourceVariableData var = new ResourceVariableData("aLocker", model.newId());
                var.setType(ResourceVariableData.BINARY);
                var.setInitialValue(0);
                var.setMax(1);
                var.setMin(0);
                TreeNode variable = new TreeNode(var);
                model.insertChild(model.getResourceRoot(), variable); 
             
            for (TreeNode n :model.getAllOperations()){  
                if (n.getNodeData() instanceof OperationData){
                    OperationData od = (OperationData)n.getNodeData();
                    if (restrictedMode)
                        addBookingConditions("aLocker",od,model);
                    else
                        addBookingConditions(od.resource,od,model);
                }
            }
        }
         
        for (SopNode sop : model.sops){
            createResourceBooking(sop,model);
            break;
        }
       
    }
        
    private void createResourceBooking(SopNode sop,Model model) {
        if (sop == null) return;
        
        if (sop.getOperation() != null && !sop.getOperation().resource.isEmpty() 
                                       && childrenHaveSameResource(sop,sop.getOperation().resource)){
            addBookingConditions(sop.getOperation().resource, sop.getOperation(), model);
        } else {
            for (SopNode n : sop.getFirstNodesInSequencesAsSet()){
                createResourceBooking(n,model);
            }
        }
        
        createResourceBooking(sop.getSuccessorNode(),model);        
    }
    
    private boolean childrenHaveSameResource(SopNode sop, String resource) {
        ArrayDeque<SopNode> stack = new ArrayDeque<SopNode>();
        for (SopNode n : sop.getFirstNodesInSequencesAsSet())
            stack.push(n);
               
        while (!stack.isEmpty()){
            SopNode n = stack.pop();
            if (n.getOperation() != null && !n.getOperation().resource.isEmpty()
                    && !n.getOperation().resource.equals(resource)){
                return false;
            } else {
                for (SopNode k : n.getFirstNodesInSequencesAsSet())
                    stack.push(k);        
            }                
        }
        return true;
    }
    
    private void createSeamBooking(SopNode sop,Model model) {
        // For now, seams should not be on children
        if (sop == null) return;
        for (SopNode n : sop.getFirstNodesInSequencesAsSet()){
            if (n.getOperation() != null && !n.getOperation().seam.isEmpty()){
                for (Seam seam : model.seams){
                    if (seam.getName().equals(n.getOperation().seam)){
                        for (String s : seam.getBlocks()){
                            addBookingConditions(s,n.getOperation(),model);
                        }
                    }
                }               
            } else {
                createSeamBooking(n,model);                
            }
        }
        createSeamBooking(sop.getSuccessorNode(),model);
    }
    
    private void createSeamBooking(OperationData od,Model model) {
        for (Seam seam : model.seams){
            if (seam.getName().equals(od.seam)){
                for (String s : seam.getBlocks()){
                    addBookingConditions(s,od,model);
                }
            }
        }     
    }
    
    
    // Refactor following methods

    private void addBookingConditions(String varName, OperationData op,Model model) {
        int id = getVariableId(varName,model);
        Condition pre = new Condition();
        Condition post = new Condition();
        ConditionStatement preGuard = new ConditionStatement("id" + id, ConditionStatement.Operator.Equal,"0");
        ConditionStatement preAction = new ConditionStatement("id" + id, ConditionStatement.Operator.Assign,"1");
        ConditionStatement postAction = new ConditionStatement("id" + id, ConditionStatement.Operator.Assign,"0");
        
        pre.getGuard().appendElement(ConditionOperator.Type.AND, preGuard);
        pre.getAction().appendElement(ConditionOperator.Type.SEMIKOLON, preAction);
        post.getAction().appendElement(ConditionOperator.Type.SEMIKOLON, postAction);
        
        ConditionData cd = new ConditionData("Booking");
        op.addCondition(cd, ConditionType.PRE, pre);
        op.addCondition(cd, ConditionType.POST, post);
        
    }
    
    
    private int getVariableId(String name, Model model){
        for (TreeNode n : model.getAllVariables()){
            if (n.getNodeData().getName().equals(name)){
                return n.getNodeData().getId();
            }
        }
        return -1;
    }
    
}