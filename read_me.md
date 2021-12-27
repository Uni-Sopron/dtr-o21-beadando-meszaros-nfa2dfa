# NFA2DFA

A program NFA-t álakít át DFA-vá. Az NFA-at JSON fileból olvassa be, a fájl nevét parancssorból kéri be. Az átalakított DFA-t pedig JSON fájlba exportálja, a DFA fájl nevét szintén parancssorból kéri be. Az example_nfa.json fájl egy példa a várt fájl formátumára. Az NFA JSON fájlnak a program mappájában kell lenni.

This program converts NFA to DFA. Reads the NFA from a JSON file, the file name is specified via command line. The converted DFA is exported to a JSON file and the name of the DFA JSON file is also specified via command line. The example_nfa.json file is an example of the expected file format. The NFA JSON file should be in the programs folder. 

## Könyvtárak - Libraries:

Jackson library-t használtam a json beolvasáshoz és írásához, a három .jar fájl kell a program futtatásához. 

I used Jackson library to read and write JSON files. Three .jar files needed in the programs folder to run the program.

1. jackson-annotations-2.12.2.jar
2. jackson-core-2.12.2.jar
3. jackson-databind-2.12.2.jar

## Futtatás parancssorból - Run from command line: 

 javac -cp .;./jackson-annotations-2.12.2.jar;./jackson-core-2.12.2.jar;./jackson-databind-2.12.2.jar *.java

 java -cp .;./jackson-annotations-2.12.2.jar;./jackson-core-2.12.2.jar;./jackson-databind-2.12.2.jar main
