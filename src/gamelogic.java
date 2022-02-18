

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*Functionality:
to do after: ?word to see if it exists.
Points system
*/

public class gamelogic {

    private static String gameWord = "hello world";
    private static String blanks = "";
    private static int failedAttemptsFactor = 1;

    public static void initblankArray() throws IOException {
        for (int counter = 0; counter < gameWord.length(); counter++) {
            if (gameWord.charAt(counter) != ' ') {
                blanks = blanks.concat("_");
            } else {
                blanks = blanks.concat(" ");
            }
        }
        return;
    }

    public static void enterWord() throws IOException {

        /*
         * BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
         * System.out.print("Please guess a letter or solve the phrase: ");
         * String userGeneratedRequest = in.readLine();
         */
        while (failedAttemptsFactor > 0) {
            System.out.println(blanks + failedAttemptsFactor);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please enter a letter or guess the phrase: ");
            String userSelection = in.readLine();

            if (userSelection.length() > 1) {

                System.out.println("user selection: " + userSelection);
                System.out.println("gameWord: " + gameWord);

                if (userSelection.equals(gameWord)) {
                    System.out.println("You are correct!");
                    
                } else {
                    System.out.println("Incorrect Guess");
                    failedAttemptsFactor -=1;
                }
            } else {
                Character userChar = userSelection.charAt(0);

                if (userSelection.equals("*")) 
                {

                    System.out.println("Creating new game");
                    //loop to top
                }

                else if (userSelection.equals(".")) {
                    System.out.println("Ending game");
                    return;
                } else {
                    if (gameWord.contains(userSelection)) {
                        for (int i = 0; i < gameWord.length(); i++) {

                            char[] blankChar = blanks.toCharArray();
                            if (gameWord.charAt(i) == userChar) {
                                blankChar[i] = userChar;
                                blanks = String.valueOf(blankChar);
                            }
                        }
                    } 
                    
                    else {

                        failedAttemptsFactor = failedAttemptsFactor - 1;
                        System.out.println("Letter not found");
                    }

                }

            }
            if(failedAttemptsFactor == 0)
            {
                System.out.println("You lose");
            }
         }
        return;
    }

    public static void main(String[] args) throws IOException {

        initblankArray();
        enterWord();

    }
}