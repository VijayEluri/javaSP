/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequenceplanner.gui.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import sequenceplanner.gui.view.AttributeClickMenu;
import sequenceplanner.gui.view.attributepanel.ConditionListPanel;
import sequenceplanner.gui.view.attributepanel.OperationAttributeEditor;

/**
 *
 * @author Peter
 */
public class AttributeMouseAdapter extends MouseAdapter {

    //private ConditionListPanel condList = new ConditionListPanel();
    private Component clickedComponent;
    private String conditionValue;
    private String conditionKey;
    private AttributeClickMenu menu;
    private OperationAttributeEditor editor;

    public AttributeMouseAdapter(OperationAttributeEditor editor) {
        this.editor = editor;
        System.out.println("XxXXxXxXXXxXXXxx");
    }

    /*    @Override
    public void mouseEntered(MouseEvent e) {
    System.out.println("TTT");
    popup(e);
    }*/
    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("RRRR");
        popup(e);
    }

    /**
     * Creates a AttributeClickMenu for clicked node
     *
     * @param e a MouseEvent
     */
    private void popup(MouseEvent e) {
        System.out.println("OOO");
        //Also need to check if pre or post panel
        if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
            System.out.println("Click: " + e.getX() + e.getY());

            clickedComponent = (JLabel) e.getSource();
            getConditionValue(clickedComponent);


            if (clickedComponent != null) {
                if (clickedComponent instanceof JLabel) {
                    if (!conditionValue.equals("")) {
                        menu = new AttributeClickMenu(clickedComponent, new MenuListener(), true);
                        System.out.println("The click is on condition panel");
                    }else{
                        menu = new AttributeClickMenu(clickedComponent, new MenuListener(), false);
                    }
                    menu.showAttributePanelMenu(e);
                }
            }
        }
    }

    /**
     * Listens for actions in AttributeClickMenu
     */
    public class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("DELETE_VALUE")) {
                System.out.println("DELETE_VALUE");
                //AttrController.delete(cond)& delete panel
                //condList.deleteCondition(conditionValue);
                //flytta delete hit..
            }
            if (command.equals("EDIT_VALUE")) {
                System.out.println("EDIT_VALUE");
                //AttrController.displayCondInField(Cond)
                System.out.println("Edit: "+ conditionKey + " Ed: " + editor.toString());
                editor.setConditionString(conditionKey);

            }
        }
    }

    public void getConditionValue(Component conditionLabel){
        JLabel condition = (JLabel)conditionLabel;
        Pattern conditionValuePattern = Pattern.compile("Algebraic (\\d)");
        Matcher m1 = conditionValuePattern.matcher(condition.getText());
        if(m1.find()){
            conditionValue = m1.group();
            conditionKey = condition.getText().substring(m1.end());
            System.out.println("Deeeee: " + conditionKey);
            //Note: m�ste h�mta keyn fr�n modellen, f�r inte med den riktiga upps�ttningen annars

        }else conditionValue = "";
    }
}
