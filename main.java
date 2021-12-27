import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class main {

    public static void main(String[] args){
        
        NFA nfa = JsonToNFA();

        nfa.splitLongTransitions();

        nfa.print();

        DFA dfa = nfa.getEquivalentDFA();

        DFAtoJSON(dfa);
        
    }
    
    //Json beolvasás
    public static NFA JsonToNFA(){

        ObjectMapper objectMapper= new ObjectMapper();

        NFA nfa=new NFA();

    try{
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        System.out.println("Enter the name of the NFA file: ");
        String filename = reader.readLine();

        nfa = objectMapper.readValue(new File("./"+filename), NFA.class);
        } catch (JsonProcessingException e){
            System.out.println("JsonProcessingException");
            System.out.println(e.getMessage());
        } catch (IOException io){
            System.out.println("file reading failure");
        } 
        return nfa;        
}   
    //JSON exportálás
    public static void DFAtoJSON(DFA dfa){

        try{
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        System.out.println("Enter the name of the DFA file: ");
        String filename = reader.readLine();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.writeValue(new File("./"+filename), dfa);
        }
        catch(IOException e){
            System.out.println("file writing failure: ");
        }
    }
}