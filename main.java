import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.sound.sampled.SourceDataLine;

public class main {

    public static void main(String[] args){
        
        NFA nfa = JsonToNFA();

        nfa.transitionSeparator();

        nfa.print();

        nfa.dfaConverting();
        
    }
    
    //Json beolvasás
    public static NFA JsonToNFA(){

        ObjectMapper objectMapper= new ObjectMapper();

        NFA nfa=new NFA();

    try{
        nfa = objectMapper.readValue(new File("./nfa.json"), NFA.class);
        } catch (JsonProcessingException e){
            System.out.println("JsonProcessingException");
            System.out.println(e.getMessage());
        } catch (IOException io){
            System.out.println("fájlolvalási hiba");
        } 
        return nfa;        
}              
}