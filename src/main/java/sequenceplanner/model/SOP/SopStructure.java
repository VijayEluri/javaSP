package sequenceplanner.model.SOP;

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
public class SopStructure implements ISopStructure{
    private ISopNode node;

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
    
}
