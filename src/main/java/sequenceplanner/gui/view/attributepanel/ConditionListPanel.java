package sequenceplanner.gui.view.attributepanel;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sequenceplanner.condition.Condition;
import sequenceplanner.gui.controller.AttributeMouseAdapter;
import sequenceplanner.model.SOP.ConditionsFromSopNode.ConditionType;
import sequenceplanner.model.data.OperationData;

/**
 * Panel showing a list of Conditions. 
 * @author Qw4z1
 */
public class ConditionListPanel extends JPanel implements IConditionListPanel {

    private HashMap<String, Condition> conditionList;
    private JPanel internalPanel;
    JLabel conditionLabel;
    OperationAttributeEditor editor;
    OperationData opData;
    ConditionType type;

    public ConditionListPanel(OperationAttributeEditor editor, OperationData opData, ConditionType type) {
        this.editor = editor;
        this.opData = opData;
        this.type = type;
        conditionList = new HashMap<String, Condition>();
        init();
    }

    private void init() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    @Override
    public void addCondition(String key, Condition condition) {
        conditionList.put(key, condition);
        updateList();

    }

    private void updateList() {
        System.out.println("updateList CLP");
        if (conditionList != null) {
            this.removeAll();
            for (String key : conditionList.keySet()) {
                if (conditionList.get(key) != null) {
                    System.out.println("kommer hit");
                    internalPanel = new JPanel();
                    internalPanel.setLayout(new BoxLayout(internalPanel, BoxLayout.X_AXIS));
                    conditionLabel = new JLabel(key + " " + conditionList.get(key).toString());
                    conditionLabel.setVisible(true);
                    internalPanel.add(conditionLabel);
                    this.add(internalPanel);
                    internalPanel.setVisible(true);
                    addConditionListener(new AttributeMouseAdapter(editor, this));
                } else {
                    this.removeAll();

                }
                this.updateUI();
            }

        } else {
            System.out.println("removeall");
            this.removeAll();
            conditionList.clear();
            this.repaint();
            this.updateUI();
        }
        this.updateUI();
    }

    @Override
    public void deleteCondition(String conditionKey) {
        opData.getGlobalConditions().remove(conditionKey);
        conditionList.remove(conditionKey);
        opData.decreaseAlgebraicCounter();
        this.updateList();
    }

    @Override
    public void editCondition(String conditionKey) throws NullPointerException {
        String conditionString = "";

        //To extract the original input string
        if (type == ConditionType.PRE) {
            if (opData.getGlobalConditions().get(conditionKey).get(ConditionType.PRE).hasGuard()) {
                System.out.println("Muu");
                conditionString = stringTrim(conditionString + opData.getGlobalConditions().get(conditionKey).get(ConditionType.PRE).getGuard().toString());

            } else if (opData.getGlobalConditions().get(conditionKey).get(ConditionType.PRE).hasAction()) {
//                Action is not yet supported in the condition parser, but prepared here:
//                conditionString = stringTrim(conditionString + opData.getGlobalConditions().get(conditionKey).get(ConditionType.PRE).getAction().toString());
            }
        } else if (type == ConditionType.POST) {
            if (opData.getGlobalConditions().get(conditionKey).get(ConditionType.POST).hasGuard()) {
                conditionString = stringTrim(conditionString + opData.getGlobalConditions().get(conditionKey).get(ConditionType.POST).getGuard().toString());

            } else if (opData.getGlobalConditions().get(conditionKey).get(ConditionType.POST).hasAction()) {
//                Action is not yet supported in the condition parser, but prepared here:
//                conditionString = stringTrim(conditionString + opData.getGlobalConditions().get(conditionKey).get(ConditionType.POST).getAction().toString());
                
            }
        }
        //Place the String in the input text window
        editor.setConditionString(conditionString);
        //deleteCondition(conditionKey);
    }

    public String stringTrim(String conditionString) {
        String conditionString2 = "";
        String[] st = conditionString.split("and");

        st[0] = st[0].substring(1);
        for (String x : st) {
            conditionString2 = conditionString2 + "id" + x + "and";
        }
        //Remove the last )and
        conditionString = conditionString2.substring(0, conditionString2.length() - 4);

        String[] st2 = conditionString.split("or");
        conditionString2 = "";
        for (String x : st2) {
            conditionString2 = conditionString2 + "id" + x + "or";
        }
        //Remove the last or and the double "id"
        conditionString = conditionString2.substring(2, conditionString2.length() - 2);

        return conditionString;
    }

    @Override
    public boolean contains(Condition condition) {
        return conditionList.containsValue(condition);
    }

    void clear() {
        conditionList.clear();
        this.removeAll();
        this.repaint();
    }
    /*
     * Adds ActionListener to the conditions that are listed
     */

    public void addConditionListener(AttributeMouseAdapter l) {
        conditionLabel.addMouseListener(l);
    }
}
