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

        for (int i = 0; i < transitions.size(); i++) {

            String s = transitions.get(i).get("with").toString();

            if (s.length() > 1) {
                doubleTransitions.add(transitions.get(i));
                transitions.remove(i);
            }
        }
        for (int i = 0; i < doubleTransitions.size(); i++) {

            HashMap transitionTo = new HashMap<>(doubleTransitions.get(i));

            String with = transitionTo.get("with").toString();

            transitionTo.replace("to", states.size() + 1);
            transitionTo.replace("with", with.charAt(0));
            transitions.add(transitionTo);

            HashMap transitionFrom = new HashMap<>(doubleTransitions.get(i));
            transitionFrom = doubleTransitions.get(i);

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
        for (int i = 0; i < dfa.states.size(); i++) {

            

            // DFA állapoton belüli NFA állapotok
            for (int j = 0; j < dfa.states.get(i).size(); j++) {
                

                // Alphabet
                for (int k = 0; k < alphabet.size(); k++) {


                    // Transitions
                    for (int l = 0; l < transitions.size(); l++) {
                        
                        ArrayList<Integer> dfaState = new ArrayList<Integer>();

                        if ((int) transitions.get(l).get("from") == dfa.states.get(i).get(j) && transitions.get(l).get("with").toString().equals(alphabet.get(k))) {

                            int nfaState = (int) transitions.get(l).get("to");

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
