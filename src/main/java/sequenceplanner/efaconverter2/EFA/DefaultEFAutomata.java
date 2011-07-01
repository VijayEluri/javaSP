
package sequenceplanner.efaconverter2.EFA;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.supremica.external.avocades.common.Module;

/**
 *
 * @author Mohammad Reza Shoaei
 * @version 21062011
 */

public class DefaultEFAutomata implements Iterable<DefaultEFAutomaton>{

    private Module module;
    private HashMap<String, DefaultEFAutomaton> automatons;
    
    public DefaultEFAutomata(String name){
        module = new Module(name, false);
        automatons = new HashMap<String, DefaultEFAutomaton>();
    }

    public void addEvent(String iEvent){
        module.addEvent(iEvent);
    }

    public void addEvent(String iName, String iKind){
        module.addEvent(iName, iKind);
    }

    public void addAllEvent(Collection<String> iEvents){
        for(String e : iEvents)
            module.addEvent(e);
    }

    public Collection<String> getAlphabet(){
        return module.getEvents();
    }

    public boolean addEFAutomaton(DefaultEFAutomaton iAutomaton) {
        boolean check = false;
        if(!automatons.containsKey(iAutomaton.getName())){
            automatons.put(iAutomaton.getName(), iAutomaton);
            module.addAutomaton(iAutomaton.getAutomaton());
            check = true;
        }
        return check;
    }

    public DefaultEFAutomaton getEFAutomaton(String iName) {
        return automatons.get(iName);
    }

    public Module getThisModule() {
        return module;
    }

    public void addEFAutomata(DefaultEFAutomata iAutomata){
        for(String e : iAutomata.getAlphabet())
            this.addEvent(e);

        for(Iterator<DefaultEFAutomaton> itr = iAutomata.iterator(); itr.hasNext();)
            this.addEFAutomaton(itr.next());
    }

    @Override
    public Iterator<DefaultEFAutomaton> iterator() {
        return (Iterator<DefaultEFAutomaton>) automatons.values().iterator();
    }

    public boolean addIntegralVariable(String iName, int iMin, int iMax, int iInitialValue) {
        boolean check = false;
        if(!automatons.containsKey(iName)){
        DefaultEFAutomaton var = new DefaultEFAutomaton(iName, this);
        var.addVariable(iMin, iMin, iInitialValue);
        automatons.put(iName, var);
        //module.addAutomaton(var);
            check = true;
        }
        return check;
    }

    public boolean containsAutomaton(DefaultEFAutomaton iAutomaton){
        return automatons.containsKey(iAutomaton.getName());
    }
    
    public Collection<DefaultEFAutomaton> getEFAutomatons(){
        return automatons.values();
    }
}