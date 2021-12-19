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
    ArrayList acceptingStates;

    // Json beolvasáshoz kell
    public NFA() {

    }

    public void print() {
        System.out.println(states);
        System.out.println(alphabet);
        System.out.println(transitions);
        System.out.println(initialState);
        System.out.println(acceptingStates);
    }

    public void transitionSeparator() {

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
                HashMap singleTransition =new HashMap();

                if(letter==0){
                        singleTransition.put("from", multipleTransition.get("from"));
                        singleTransition.put("with", multipleTransition.get("with").toString().charAt(letter));
                        singleTransition.put("to", states.size()+1);
                        states.add(states.size()+1);
                }
                if(letter==transitionLetters-1){
                        singleTransition.put("from", states.size());
                        singleTransition.put("with", multipleTransition.get("with").toString().charAt(letter));
                        singleTransition.put("to", multipleTransition.get("to"));
                }
                if(letter !=0 && letter!=transitionLetters-1){
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

    public void dfaConverting() {

        transitionSeparator();

        print();

        DFA dfa = new DFA();

        ArrayList<Integer> dfaInitialState = new ArrayList<Integer>();
        dfaInitialState.add(initialState);

        dfa.states.add(dfaInitialState);

        // DFA állapotok
        for (int dfaState=0; dfaState<dfa.states.size(); dfaState++) {

            // DFA állapoton belüli NFA állapotok
            for (int nfaState=0; nfaState<dfa.states.get(dfaState).size(); nfaState++) {

                ArrayList<Integer> newDfaState = new ArrayList<Integer>();

                 // Transitions
                 for (HashMap transition : transitions) {   
                
                    // Alphabet
                    for (char character : alphabet) {                    
                        
                        if ((int) transition.get("from") == dfa.states.get(dfaState).get(nfaState) && transition.get("with").equals(character)) {

                            //Ellenőrzi hogy szerepel e már a DFA state-ben az NFA state
                            if (newDfaState.contains(transition.get("to"))) {
                                newDfaState.add((int)transition.get("to"));
                            }
                            
                            //Ellenőrzi hogy az NFA state-ből tovább lehet e lépni empty-vel
                            for (int m = 0; m < transitions.size(); m++) {

                                if (transitions.get(m).get("with").toString() == "" && (int) transitions.get(m).get("from") == (int) transition.get("to"));

                                if (!newDfaState.contains( transitions.get(m).get("to"))) {
                                    newDfaState.add((int) transitions.get(m).get("to"));
                                }
                            }
                        }
                            if (!dfa.states.contains(newDfaState)) {
                                dfa.states.add(newDfaState);
                        }
                    }
                }
            }
        }
        System.out.println(dfa.states);
    } 

}
