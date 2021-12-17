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

        ArrayList<HashMap> doubleTransitions = new ArrayList<HashMap>();

        ArrayList<HashMap> newTransitionList = new ArrayList<HashMap>();

        for (int dfaStates = 0; dfaStates < transitions.size(); dfaStates++) {

            String s = transitions.get(dfaStates).get("with").toString();

            if (s.length() > 1) {
                doubleTransitions.add(transitions.get(dfaStates));
                transitions.remove(dfaStates);
            }
        }
        for (int dfaStates = 0; dfaStates < doubleTransitions.size(); dfaStates++) {

            HashMap transitionTo = new HashMap<>(doubleTransitions.get(dfaStates));

            String with = transitionTo.get("with").toString();

            transitionTo.replace("to", states.size() + 1);
            transitionTo.replace("with", with.charAt(0));
            transitions.add(transitionTo);

            HashMap transitionFrom = new HashMap<>(doubleTransitions.get(dfaStates));
            transitionFrom = doubleTransitions.get(dfaStates);

            transitionFrom.replace("from", states.size() + 1);
            transitionFrom.replace("with", with.charAt(1));
            transitions.add(transitionFrom);

            states.add(states.size() + 1);
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
