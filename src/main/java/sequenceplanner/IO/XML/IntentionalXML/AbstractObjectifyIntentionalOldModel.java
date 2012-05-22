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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sequenceplanner.IO.XML.ObjectifyXML;
import sequenceplanner.datamodel.condition.Condition;
import sequenceplanner.datamodel.condition.ConditionElement;
import sequenceplanner.datamodel.condition.ConditionExpression;
import sequenceplanner.datamodel.condition.ConditionOperator;
import sequenceplanner.datamodel.condition.ConditionStatement;
import sequenceplanner.model.Model;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode.ConditionType;
import sequenceplanner.model.TreeNode;
import sequenceplanner.model.data.ConditionData;
import sequenceplanner.model.data.OperationData;
import sequenceplanner.model.data.ResourceVariableData;
import sequenceplanner.visualization.algorithms.ISupremicaInteractionForVisualization.Type;

/**
 * This objectifier takes elements from an xml file from 
 * intentional software DWB, and adds them to the old SP model.
 * 
 * Current implementation takes the parent tag for the objects, e.g. operations
 * which include operation tags.
 * 
 * TODO: When a new model is created, this class will be divided into one general
 * part for the XML-structure, and one specific for each model.
 * 
 * @author kbe
 */
public abstract class AbstractObjectifyIntentionalOldModel implements ObjectifyXML {

    private final String elementTag;
    private final String rootTag;
    private static final Class model = Model.class;
    private static final String xsi = "SeamAssembly.xsd";
    
    private final ConditionData condDataType = new  ConditionData("DWB"); 

    protected AbstractObjectifyIntentionalOldModel(String rootTag,String elementTag) {
        this.rootTag = rootTag;
        this.elementTag = elementTag;
    }
        
    
    @Override
    public String getRootTag() {
        return rootTag;
    }
    
    @Override
    public boolean validateRootTag(Element e){
        if (!e.getTagName().equals(rootTag)) return false;
        //if (!e.getAttribute("xsi:noNamespaceSchemaLocation").equals(xsi)) return false;
        
        return true;
    }

    @Override
    public String getElementTag() {
        return elementTag;
    }

    @Override
    public Class getModelClass() {
        return model;
    }
    
    
    @Override
    public boolean addModelToElement(Object model, Element e){
        if (!(this.model.isInstance(model))) return false;
        if (!e.getTagName().equals(rootTag)) return false;
//        if (!e.hasAttribute("xmlns:xsi"))
//            e.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//        if (!e.hasAttribute("xsi:noNamespaceSchemaLocation"))
//            e.setAttribute("xsi:noNamespaceSchemaLocation", "SeamAssembly.xsd");
        
        return createElements((Model)model,e);
    }

    @Override
    public boolean addElementToModel(Element e, Object model) {
        if (!(this.model.isInstance(model))) return false;    
        if (e == null) return false;
        if (!(e.getTagName().toLowerCase().equals(elementTag.toLowerCase()))) return false;
                       
        return addElements(e,(Model) model);
    }
    
    // Currently only adds everything flat
    // Assume that elementTag is parent to object tags! If not so, override this
    protected boolean addElements(Element e, Model m){
        boolean added = false;
        for (Element obj : getChildren(e))
            added = added | addElement(obj,m);
        return added;
    }
        
    protected abstract boolean addElement(Element e, Model m);

    protected abstract boolean createElements(Model model, Element e);

    protected List<Element> getChildren(Element e){
        List<Element> set = new LinkedList<Element>();
        if (e==null) return set;
        NodeList list = e.getChildNodes();
        for (int i=0 ; i<list.getLength() ; i++){
            if (list.item(i) instanceof Element)
                set.add((Element) list.item(i));
        }
        
        return set;
    }
    
    protected String getName(Element e){
        for (Element child : getChildren(e)){
            if (child.getTagName().toLowerCase().equals("names")){
                for (Element name : getChildren(child)){
                    if (name.getTagName().toLowerCase().equals("name"))
                        return name.getTextContent();                   
                }
            }
        }
        return "";
    }
    
    protected String getTarget(Element e){
        for (Element child : getChildren(e)){
            if (child.getTagName().toLowerCase().equals("target")){
                for (Element name : getChildren(child)){
                    return name.getTagName();                 
                }
            }
        }
        return "";
    }
    

    
}