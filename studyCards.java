package FlashCards;

import java.io.File;
//import java.util.Scanner;
import java.util.*;
import java.io.PrintWriter;

public class studyCards {


    public static void main(String[] args) {
        /**     ALl of the Maps we need for the cards       **/
        HashMap<Integer, flashCard> allCards = new HashMap<>(); //Holds all cards
        HashMap<Integer, flashCard> knownCards = new HashMap<>(); //Will hold known cards
        HashMap<Integer, flashCard> unknownCards = new HashMap<>(); //Will hold unknown cards

        /**     All of the variables we need        **/
        Scanner in = new Scanner(System.in); //Scanner for userInput
        String userInput = ""; //Will come into play later when the user answers the questions
        int cardsUsed = 0;


        String fileName = ""; //File Name declaration

        System.out.println("Please input the file name: "); //User adds file name
        fileName = in.next();
        in.nextLine();


        try {
            File inputFile = new File(fileName);
            Scanner input = new Scanner(inputFile);

            /**     Card Value Getter       **/
            int cardNum = 0;
            while(input.hasNextLine()){
                input.nextLine();
                cardNum++;
            }
            input = new Scanner(inputFile);
            String[][] cardvalues = new String [cardNum][2]; //Array to obtain card values

            int iterator = cardNum;
            while(input.hasNextLine()){
                cardvalues[cardNum - iterator] = input.nextLine().split("\t");
                iterator--;
            }
            /**     END OF CARD VALUE GETTER        **/


            /**     CREATES THE FLASHCARDS       **/
                for (int j = 0; j < cardNum; j++) {
                    allCards.put(j + 1, new flashCard(cardvalues[j][0], cardvalues[j][1]));
                }
                /**     END OF CREATE FLASHCARDS        **/


                /**     What the program runs when studying     **/
                do {
                    int randomKey = (int) (Math.random() * cardNum) + 1; //Generates a random key

                    randomKey = noRepeat(randomKey, cardNum, knownCards, unknownCards);
                    correctAnswer(question(randomKey, allCards), randomKey, allCards.get(randomKey), knownCards, unknownCards);

                    System.out.println("Would you like to continue?");
                    userInput = in.next();
                    in.nextLine();
                    cardsUsed++;
                } while (!userInput.equals("stop") && !userInput.equals("STOP") && !userInput.equals("Stop") && cardsUsed < cardNum);
                /**     End of study time       **/


                /**     Studying finished       **/
                System.out.println("Now that you're done studying here are all the terms you did not feel comfortable with.");
                for (int i = 1; i <= cardNum; i++) {
                    if (unknownCards.containsKey(i)) {
                        System.out.printf("\n-%s\n", unknownCards.get(i).getTerm());
                    }
                }
                System.out.print("All of these terms have been put into the unknownCards.txt file.\n" +
                        "If you would like to study only those cards load this again and type unknownCards as the file name.\n");
                try {
                    PrintWriter out = new PrintWriter("unknownCards");
                    outputToFile(cardNum, unknownCards, out);
                    out.close();
                } catch (Exception ex) {
                    System.out.println("output not found");
                }
                /**     End of studying finished        **/
        } catch(Exception ex){
            System.out.println("File could not be opened.");
            //userInput = "stop";
        }
        /**     END OF CARD VALUE GETTER        **/
    }



    /**     Prompts a question to the user      **/
    public static int question(int randomKey,HashMap<Integer, flashCard> allCards){
        Scanner in = new Scanner(System.in);
        String input = "";
        int answer = -1;
        String spaces1 = "";
        System.out.println("Enter \"STOP\" to quit.\n"); // Prompt the user with the quit key

        System.out.print("What's the definition?\n+-");
        for(int ch = 0; ch < allCards.get(randomKey).getTerm().length(); ch++){
            System.out.print("-");
            spaces1 = spaces1 + " ";
        }
        System.out.println("-+");

        System.out.printf("| %s |\n| %s |\n| %s |\n", spaces1, allCards.get(randomKey).getTerm(), spaces1);

        System.out.print("+-");
        for(int ch = 0; ch < allCards.get(randomKey).getTerm().length(); ch++){
            System.out.print("-");
        }
        System.out.println("-+\n");

        in.nextLine();

        while(answer == -1) {
            String spaces2 = "";
            System.out.print("The true definition was\n+-");
            for(int ch = 0; ch < allCards.get(randomKey).getDefinition().length(); ch++){
                System.out.print("-");
                spaces2 = spaces2 + " ";
            }
            System.out.println("-+");

            System.out.printf("| %s |\n| %s |\n| %s |\n", spaces2, allCards.get(randomKey).getDefinition(), spaces2);

            System.out.print("+-");
            for(int ch = 0; ch < allCards.get(randomKey).getDefinition().length(); ch++){
                System.out.print("-");
            }
            System.out.println("-+");
            input = in.nextLine();

            System.out.println("Did you get it correct?(Type 1 for yes or 0 for no)");
            input = in.nextLine();

            if (input.equals("1") || input.equals("yes") || input.equals("YES") || input.equals("y")) {
                answer = 1;
            } else if(input.equals("0") || input.equals("no") || input.equals("NO") || input.equals("n")) {
                answer = 0;
            } else {
                System.out.println("\nINVALID INPUT");
            }
        }
        return answer;
    }

    /**     CHECKS FOR CORRECT ANSWER AND ADDS TO CORRECT HASHMAP       **/
    public static void correctAnswer(int x, Integer key,flashCard flashcard1, HashMap<Integer, flashCard> known, HashMap<Integer, flashCard> unknown){
        if(x != 0){
            known.putIfAbsent(key, flashcard1);
        } else {
            unknown.putIfAbsent(key, flashcard1);
        }
    }

    /**     Makes it so that cards aren't shown more than once       **/
    public static int noRepeat(int randomKey, int cardNum, HashMap<Integer, flashCard> known, HashMap<Integer, flashCard> unknown){
        while(known.containsKey(randomKey) || unknown.containsKey(randomKey)){
            randomKey = (int)((Math.random() * cardNum) + 1);
        }
        return randomKey;
    }

    /**     Outputs to new tsv file     **/
    public static void outputToFile(int cardNum,HashMap<Integer, flashCard> unknownCards, PrintWriter out){
        for(int i = 0; i < cardNum; i++){
            if(unknownCards.containsKey(i)){
                out.printf("%s\t%s\n", unknownCards.get(i).getTerm(), unknownCards.get(i).getDefinition());
            }
        }
    }
}

class flashCard {
    private String term;
    private String definition;

    public flashCard() {
        this.term = "BLANK";
        this.definition = "BLANK";
    }

    public flashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public String getTerm() {
        return this.term;
    }

    public String getDefinition() {
        return this.definition;
    }

    public String toString() {
        return String.format("Term: %s && Definition: %s", this.getTerm(), this.getDefinition());
    }
}