import java.util.*;

public class DFA {
    public ArrayList<ArrayList<Integer>> states = new ArrayList<ArrayList<Integer>>();
    public ArrayList<Character> alphabet = new ArrayList<Character>();
    public ArrayList<Integer> transitions = new ArrayList<Integer>();
    public int initialState = 1;
    public ArrayList<Integer> acceptingStates = new ArrayList<Integer>();

    public void print(){
        System.out.println(states);
        System.out.println(alphabet);
        System.out.println(transitions);
        System.out.println(initialState);
        System.out.println(acceptingStates);
    }
}
