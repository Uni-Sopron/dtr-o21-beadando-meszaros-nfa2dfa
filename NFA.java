import java.util.*;

import javax.sound.sampled.SourceDataLine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NFA {

    @JsonProperty("states")
    ArrayList<Integer> states;
    @JsonProperty("alphabet")
    ArrayList<Character> alphabet;
    @JsonProperty("transitions")
    ArrayList<HashMap> transitions;
    @JsonProperty("initialState")
    int initialState;
    @JsonProperty("acceptingStates")
    ArrayList<Integer> acceptingStates;

    // Json beolvasáshoz kell
    public NFA() {

    }

    // Fontos információkat kiírja konzolra az NFA-ról
    public void print() {
        System.out.println("The NFA: ");
        System.out.println(states);
        System.out.println(alphabet);
        System.out.println(transitions);
        System.out.println(initialState);
        System.out.println(acceptingStates);
        System.out.println("");
    }

    // Szétválasztja azokat az átmeneteket ahol több betű is van
    public void splitLongTransitions() {

        ArrayList<HashMap> multipleTransitions = new ArrayList<HashMap>();

        ArrayList<HashMap> newTransitions = new ArrayList<HashMap>();

        for (HashMap transition : transitions) {

            if (transition.get("with").toString().length() > 1) {
                multipleTransitions.add(transition);
            }
            else{
                newTransitions.add(transition);
            }
        }
        for (HashMap multipleTransition : multipleTransitions) {

            int transitionLetters = multipleTransition.get("with").toString().length();

            for(int letter=0; letter < transitionLetters; letter++){

                HashMap singleTransition = new HashMap();

                if(letter==0){
                        singleTransition.put("from", multipleTransition.get("from"));
                        singleTransition.put("with", multipleTransition.get("with").toString().charAt(letter));
                        singleTransition.put("to", states.size()+1);
                        states.add(states.size()+1);
                }
                else if(letter==transitionLetters-1){
                        singleTransition.put("from", states.size());
                        singleTransition.put("with", multipleTransition.get("with").toString().charAt(letter));
                        singleTransition.put("to", multipleTransition.get("to"));
                }
                else{
                        singleTransition.put("from", states.size());
                        singleTransition.put("with", multipleTransition.get("with").toString().charAt(letter));
                        singleTransition.put("to", states.size()+1);
                        states.add(states.size()+1);
                }
                newTransitions.add(singleTransition);
            }
            transitions = newTransitions;
        }
    }

    //Új DFA állapotokat keres
    ArrayList<Integer> calculateSimulatedDFAState(ArrayList<Integer> existingDfaSate, char letter){

        ArrayList<Integer> newDfaState = new ArrayList<Integer>();

        // DFA állapoton belüli NFA állapotok
        for (int nfaState : existingDfaSate) {

             // Transitions
             for (HashMap transition : transitions) {

                    //Ellenőrzi hogy a from állapot és a betű egyezik e. Csak így tudtam char-t Stringgel összhasonlítani. 
                    if ((int) transition.get("from") == nfaState && transition.get("with").toString().equals(String.valueOf(letter))) {

                        //hozzáadja a to értékét a transition-nek ha még nem tartalmazta
                        if(!newDfaState.contains((int) transition.get("to"))){
                            newDfaState.add((int) transition.get("to"));
                        }                       
                }
            }
        }
        return newDfaState;
    }

    // Ellenőrzi hogy az NFA state-ből tovább lehet e lépni empty-vel
    ArrayList<Integer> emptyClosure (ArrayList<Integer> dfaState){

        for (HashMap transition2: transitions) {

                for (int dfaElement=0; dfaElement<dfaState.size(); dfaElement++){

                    if ( transition2.get("with").toString().length()==0 && (int) transition2.get("from") == dfaState.get(dfaElement)){

                        //hozzáadja a to értékét a transition2-nek ha még nem tartalmazta
                        if(!dfaState.contains((int) transition2.get("to"))){
                                dfaState.add((int) transition2.get("to"));
                        }
                    }
                }
            }
        return dfaState;
    }

    //Elfogadó állapotokat keres a DFA állapotokban
    public ArrayList<Integer> acceptingStateFinder(ArrayList<ArrayList<Integer>> dfaStates){

        ArrayList<Integer> acceptingDfaStates = new ArrayList<Integer>();

        for (ArrayList<Integer> dfaState : dfaStates) {

            for(int acceptingState : acceptingStates ){

                if(dfaState.contains(acceptingState)){
                    acceptingDfaStates.add(dfaStates.indexOf(dfaState)+1);
                }
            }            
        }
        return acceptingDfaStates;
    }


    // DFA-vá alakítja az NFA-t
    public DFA getEquivalentDFA() {

        splitLongTransitions();

        DFA dfa = new DFA();

        dfa.alphabet = alphabet;

        ArrayList<Integer> dfaInitialState = new ArrayList<Integer>();
        dfaInitialState.add(initialState);

        dfa.states.add(emptyClosure(dfaInitialState));

        // DFA állapotok
        for (int dfaState=0; dfaState<dfa.states.size(); dfaState++) {

            for(char letter : alphabet){

                ArrayList<Integer> newDfaState = emptyClosure(calculateSimulatedDFAState( dfa.states.get(dfaState), letter));

                if(!dfa.states.contains(newDfaState)){
                    dfa.states.add(newDfaState);
                }
                //Elmenti az új DFA transition-t.
                HashMap newTransition = new HashMap<>();
                                
                newTransition.put("with", letter);
                newTransition.put("from", dfaState+1);
                newTransition.put("to", dfa.states.indexOf(newDfaState)+1);

                dfa.transitions.add(newTransition);
            }           
        }

        dfa.acceptingStates = acceptingStateFinder(dfa.states);

        return dfa;
        
    } 
}
