import java.util.*;

import javax.sound.sampled.SourceDataLine;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NFA {

    @JsonProperty("states")
    ArrayList<Integer> states;
    @JsonProperty("alphabet")
    ArrayList alphabet;
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

            /*HashMap transitionTo = new HashMap<>(multipleTransitions.get(multipleTransition));

            String with = transitionTo.get("with").toString();

            transitionTo.replace("to", states.size() + 1);
            transitionTo.replace("with", with.charAt(0));
            transitions.add(transitionTo);

            HashMap transitionFrom = new HashMap<>(multipleTransitions.get(multipleTransition));
            transitionFrom = multipleTransitions.get(multipleTransition);

            transitionFrom.replace("from", states.size() + 1);
            transitionFrom.replace("with", with.charAt(1));
            transitions.add(transitionFrom);

            states.add(states.size() + 1); */

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
        for (int dfaStates = 0; dfaStates < dfa.states.size(); dfaStates++) {

            

            // DFA állapoton belüli NFA állapotok
            for (int nfaStates = 0; nfaStates < dfa.states.get(dfaStates).size(); nfaStates++) {
                

                // Alphabet
                for (int character = 0; character < alphabet.size(); character++) {


                    // Transitions
                    for (int transition = 0; transition < transitions.size(); transition++) {
                        
                        ArrayList<Integer> dfaState = new ArrayList<Integer>();

                        if ((int) transitions.get(transition).get("from") == dfa.states.get(dfaStates).get(nfaStates) && transitions.get(transition).get("with").toString().equals(alphabet.get(character))) {

                            int nfaState = (int) transitions.get(transition).get("to");

                            if (!dfaState.contains(nfaState)) {
                                dfaState.add(nfaState);
                            }

                            for (int m = 0; m < transitions.size(); m++) {

                                if (transitions.get(m).get("with").toString() == "" && (int) transitions.get(m).get("from") == nfaState);

                                if (!dfaState.contains((int) transitions.get(m).get("to"))) {
                                    dfaState.add((int) transitions.get(m).get("to"));
                                }
                            }
                        }
                            if (!dfa.states.contains(dfaState)) {
                                dfa.states.add(dfaState);
                        }
                    }
                }
            }
        }
        System.out.println(dfa.states);
    } 
}
