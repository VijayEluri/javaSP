/*
 * AttributePanel.java
 *
 * Created on 2011-jun-21, 10:14:10
 */
package sequenceplanner.gui.view.attributepanel;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import sequenceplanner.datamodel.condition.Condition;
import sequenceplanner.model.Model;
import sequenceplanner.model.SOP.algorithms.ConditionsFromSopNode.ConditionType;
import sequenceplanner.model.data.ConditionData;
import sequenceplanner.model.data.OperationData;

/**
 * Class showing attributes of an {@link OperationData} object
 * @author Qw4z1
 */
public class AttributePanel extends JPanel {

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private OperationAttributeEditor operationAttributeEditor;
    private JTextField operationIdTextField;
    private ConditionListPanel postCondListPanel;
    private ConditionListPanel preCondListPanel;
    private DescriptionPanel descriptionPanel;
    private OperationData operationData;
    private Model mModel;

    /** Creates new form AttributePanel */
    public AttributePanel(OperationData od, final Model iModel) {
        this.operationData = od;
        this.mModel = iModel;
        initComponents();
        initVariables(od);
    }

    private void initComponents() {

        jSeparator1 = new JSeparator();
        operationIdTextField = new JTextField();
        operationIdTextField.setActionCommand("set name");
        operationAttributeEditor = new OperationAttributeEditor();
        preCondListPanel = new ConditionListPanel(operationAttributeEditor, operationData, ConditionType.PRE, mModel);
        postCondListPanel = new ConditionListPanel(operationAttributeEditor, operationData, ConditionType.POST, mModel);
        jLabel1 = new JLabel("Preconditions");
        jLabel2 = new JLabel("Postconditions");
        jScrollPane1 = new JScrollPane(preCondListPanel);
        jScrollPane2 = new JScrollPane(postCondListPanel);
        descriptionPanel = new DescriptionPanel();
        jSeparator2 = new JSeparator();

        operationIdTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        operationIdTextField.setText("Operation ID: ");
        operationAttributeEditor.setPreferredSize(new Dimension(300, 100));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(137, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addContainerGap(132, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addContainerGap()).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE).addComponent(operationAttributeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE).addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addComponent(operationIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(operationIdTextField).addGap(18, 18, 18).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(operationAttributeEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(30, Short.MAX_VALUE).addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)));
    }

    /**
     * Sets the ID label to display the id of the {@link OperationData} object.
     * @param id
     */
    public void setOperationName(String name) {
        this.operationIdTextField.setText(name);
        
    }

    /**
     * Sets the listobjects to display the conditions in the OperationData class.
     */
    public void setConditions() {
        preCondListPanel.clear();
        if (operationData.getConditions() != null) {
            //Extract each set of condition sets
            for (final ConditionData conditionDataKey : operationData.getConditions().keySet()) {
                Map<ConditionType, Condition> conditionMap = operationData.getConditions().get(conditionDataKey);
                //Split conditions into post and pre
                for (final ConditionType key : conditionMap.keySet()) {

                    //pre
                    if (key == ConditionType.PRE && !preCondListPanel.contains(conditionMap.get(key))) {
                        preCondListPanel.addCondition(conditionDataKey, conditionMap.get(key));
                        //post
                    } else if (key == ConditionType.POST && !postCondListPanel.contains(conditionMap.get(key))) {
                        postCondListPanel.addCondition(conditionDataKey, conditionMap.get(key));
                    }
                }
            }
        }
    }

    private void initVariables(OperationData od) {
        this.operationData = od;
        setConditions();
        this.setName(od.getName());
        this.setDescription(od.getDescription());
        setOperationName(operationData.getName());
    }

    /**
     * Adds a ActionListener to the savebutton
     * @param l the ActionListener
     */
    public void addEditorSaveListener(ActionListener l) {
        operationAttributeEditor.addSaveButtonListener(l);
    }

    /**
     * Adds an ActionListener to the OperationIdTextField
     * @param l the ActionListener
     */
    public void addOperationIdTextFieldListener(ActionListener l) {
        operationIdTextField.addActionListener(l);
    }

    /**
     * Adds an ActionListener to the DescriptionTextField
     * @param l the ActionListener
     */
    public void addDescriptionListeners(ActionListener l, FocusListener fl) {
        //Save description when ENTER is pressed.
        descriptionPanel.addActionListener(l);
        //Save description when focus is lost
        descriptionPanel.addFocusListener(fl);
    }

    /**
     * Method for getting the inner {@link OperationAttributeeEditor}
     * @return
     */
    public OperationAttributeEditor getEditor() {
        return operationAttributeEditor;
    }

    /**
     * Sets operationData to a new OperationData and updates the conditions.
     * @param od
     */
    public OperationData updateModel(OperationData od) {
        this.operationData = od;
        setOperationName(od.getName());
        setConditions();
        if (operationData.getDescription() != null) {
            setDescription(operationData.getDescription());
        }
        return this.operationData;
    }

    public JTextField getOperationIdTextField() {
        return operationIdTextField;
    }

    /**
     *
     * @param description
     */
    private void setDescription(String description) {
        descriptionPanel.setDescription(description);
    }

    public DescriptionPanel getDescriptionPanel() {
        return descriptionPanel;
    }

    public OperationData getOperationData() {
        return operationData;
    }
}
