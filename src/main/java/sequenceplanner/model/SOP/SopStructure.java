package sequenceplanner.model.SOP;

import java.util.LinkedList;
import sequenceplanner.view.operationView.graphextension.Cell;

/**
 *
 * @author Qw4z1
 * *Till viktor*
 * Vi m�ste ha en Linked List f�r varje sekvens d�r ny root "Before ->Operation"
 * l�ggs till som ny "addFirst". L�ggs en ny operation till "after" s� l�ggs den
 * i sist i listan. L�ggs en parallell eller alternativ till s� m�ste de l�nkas
 * ihop i en annan lista via listan.
 *
 */
public class SopStructure implements ISopStructure {

    private ASopNode node;
    private LinkedList<SopSequence> sopSeqs = new LinkedList<SopSequence>();

    public SopStructure(Cell cell, ASopNode sopNode, boolean before) {
        //If the cell exists in the sequence, the new cell should be added
        //*This is not really true, since the cell can exists within two
        //sequences in the same OpView. So have to rethink this structure*
       /* for (SopSequence sopSeq :sopSeqs)  {
            if (sopSeq.contains(sopNode)) {
                getSopSequence().addSopNode(sopNode);
            } else {
                //*******Fixa till Lista!*******
                SopSequence sopSeq = new SopSequence(sopNode, cell, before);

            }
        }*/
    }

    @Override
    public void addNode(ISopNode node) {
    }

    @Override
    public void addNodeToRoot(ISopNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addNodeToSequence(ISopNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addSopSequence(SopSequence seq) {
        sopSeqs.add(seq);
    }
    //Should be a list that is returned

    public LinkedList<SopSequence> getSopSequence() {
        return sopSeqs;
    }
}
