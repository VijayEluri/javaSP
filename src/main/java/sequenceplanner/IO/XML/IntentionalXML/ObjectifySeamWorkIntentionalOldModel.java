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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sequenceplanner.IO.XML.ObjectifyXML;
import sequenceplanner.datamodel.condition.Condition;
import sequenceplanner.datamodel.condition.ConditionOperator;
import sequenceplanner.datamodel.condition.ConditionOperator.Type;
import sequenceplanner.datamodel.condition.ConditionStatement;
import sequenceplanner.datamodel.product.Seam;
import sequenceplanner.model.Model;
import sequenceplanner.model.SOP.*;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode.ConditionType;
import sequenceplanner.model.TreeNode;
import sequenceplanner.model.data.ConditionData;
import sequenceplanner.model.data.OperationData;
import sequenceplanner.model.data.ResourceVariableData;
import sequenceplanner.model.data.ViewData;


/**
 * This objectifier takes variables elements from an xml file from 
 * intentional software DWB, and adds the variables to the old SP model.
 * 
 * Currently all variables are added flat and not in a resource.
 * 
 * @author kbe
 */
public class ObjectifySeamWorkIntentionalOldModel extends AbstractObjectifyIntentionalOldModel {

    private static final String elementTag = "seamwork";
    private static final String rootTag = "ProcessPlanForExport";
    private static final String objectTag = "preplanalt";
    
    private final ConditionData condDataType = new ConditionData("DWB"); 
    private String seam="";

    public ObjectifySeamWorkIntentionalOldModel() {
        super(rootTag,elementTag);
    }
        
    
    @Override
    protected boolean createElements(Model model, Element e){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    @Override
    protected boolean addElement(Element e, Model m){
        if (e.getTagName().toLowerCase().equals("seam")){
            for (Element child : getChildren(e)){
                if (child.getTagName().toLowerCase().equals("seamref")){
                    seam = getTarget(child);
                    break;
                }
            }
        }
        
        if (!e.getTagName().toLowerCase().equals(objectTag.toLowerCase())) return false;
        SopNode sop = createSOP(e, m);
        //System.out.println(sop.toString());
        return saveSOPToModel(sop,m);
        
    }
    

    private SopNode createSOP(Element e, Model m) {  
        SopNode root = new SopNodeEmpty();
        SopNode alt = new SopNodeAlternative();
        root.addNodeToSequenceSet(alt);
        for (Element child : getChildren(e)){
            SopNode n = resolveSopNode(child, m);
            if (n != null) alt.addNodeToSequenceSet(n);
        }
        
        return root;
        
    }

    private SopNode resolveSopNode(Element e, Model m) {
        SopNode node = null;
        if (e.getElementsByTagName("isOperation").getLength()==0) return node;
             
        OperationData data = createOperation(e,m);
        if (data != null) 
            node = new SopNodeOperation(data);
            
        // do not support child operations          
//        for (Element child : getChildren(e)){
//            SopNode n = resolveSopNode(child, m);
//            if (n != null) node.addNodeToSequenceSet(n);
//        }
        
        return node;
    }
    
    
    private OperationData createOperation(Element e, Model m) {
        String name = getName(e); 
        if (name.isEmpty()) return null;
        
        TagNameMapper.INSTANCE.addTageNameType(name, "operation");
                      
        OperationData od = new OperationData(name,m.newId());
      
        parseOperationContent(e,od,m); 
        od.seam = seam;
        m.createModelOperationNode(od); 
        return od;
    }
    
    
    
    private boolean parseOperationContent(Element e, OperationData od, Model m) { 
        if (!e.getAttribute("id").isEmpty()) od.guid = e.getAttribute("id");
        for (Element child : getChildren(e)){                                   
            if (child.getTagName().toLowerCase().equals("preconditions"))
                appendCondition(child,od,m,ConditionType.PRE,true,false);
            else if (child.getTagName().toLowerCase().equals("postconditions"))
                appendCondition(child,od,m,ConditionType.POST,true,false);
            else if (child.getTagName().toLowerCase().equals("preeffects"))
                appendCondition(child,od,m,ConditionType.PRE,false,true);
            else if (child.getTagName().toLowerCase().equals("posteffects"))
                appendCondition(child,od,m,ConditionType.POST,false,true);
            
            else if (child.getTagName().toLowerCase().equals("duration")){
                int cost = -1;
                try{
                    for (Element intLit : getChildren(child)){
                        if (intLit.getTagName().toLowerCase().equals("intlit")){
                            if (intLit.getTextContent().isEmpty()) cost = 1;
                            else cost = Integer.parseInt(intLit.getTextContent());
                            break;
                        }
                    }                   
                } catch (NumberFormatException ex){
                    cost = -1;
                }
                od.timecost = cost;
            }
            else if (child.getTagName().toLowerCase().equals("allocation")){
                addResource(child,od,m);
            }
            else if (child.getTagName().toLowerCase().equals("actuals")){
                for (Element guid : getChildren(child)){
                    if (guid.hasAttribute("idisa")){
                        if (od.guid.isEmpty())
                            od.guid = guid.getAttribute("idisa");
                        break;
                    }
                }
            }
        
        
        }
            // find more content here
        
        return true;
    }
    
    private void addResource(Element e, OperationData od, Model m){
        for (Element child : getChildren(e)){
            od.resource = child.getTagName();
            for (TreeNode n : m.getAllVariables()){
                if (n.getNodeData().getName().equals(child.getTagName())){
                    return; 
                }
            }
        
            ResourceVariableData var = new ResourceVariableData(child.getTagName(), m.newId());
            var.setType(ResourceVariableData.BINARY);
            var.setInitialValue(0);
            var.setMax(1);
            var.setMin(0);
            TreeNode variable = new TreeNode(var);
            m.insertChild(m.getResourceRoot(), variable);          
            TagNameMapper.INSTANCE.addTageNameType(child.getTagName(), "resource");
        }       
        
    }
    
    
    private boolean appendCondition(Element e,
                                    OperationData od, 
                                    Model m, 
                                    ConditionType condType,
                                    boolean guard,
                                    boolean action)
    {
        if ((guard && action) || (!guard && !action)) return false;
        
        Condition condition = new Condition();   
        if (guard) condition.setGuard(ObjectifyIntentionalExpressions.INSTANCE.createExpression(e,m));
        else if (action) condition.setAction(ObjectifyIntentionalExpressions.INSTANCE.createExpression(e,m));
        
        ConditionData typeName = null;
        if (!e.getAttribute("name").isEmpty()) typeName = new ConditionData(e.getAttribute("name"));
        else typeName = condDataType;
        od.addCondition(typeName, condType, condition);
           
        return true;
    }
 
    
    private boolean saveSOPToModel(SopNode sop, Model m) {
        m.sops.add(sop);
        
        ConditionsFromSopNode cfsn = new ConditionsFromSopNode(sop);
        Map<OperationData, Map<ConditionType, Condition>> map = cfsn.getmOperationConditionMap();
        for (TreeNode node : m.getAllOperations()){
            Map<ConditionType, Condition> cond = map.get((OperationData)node.getNodeData());
            if (cond != null){
                ((OperationData) node.getNodeData()).addCondition(
                        new ConditionData("IDW_SOP"), 
                        ConditionType.PRE,
                        cond.get(ConditionType.PRE));
                ((OperationData) node.getNodeData()).addCondition(
                        new ConditionData("IDW_SOP"), 
                        ConditionType.POST,
                        cond.get(ConditionType.POST));
                }
        }
        
        
        return true;
        
       
    }
    




    
}