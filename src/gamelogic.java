import java.util.ArrayList;
import javax.imageio.IIOException;

import java.io.IOException;
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

/*Functionality:
to do after: ?word to see if it exists.
Points system
*/

public class gamelogic {

    private static String gameWord = "hello world";
    private static String blanks = "";
    private static int lives = 5;

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
        while (lives > 0) {
        System.out.println(blanks);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter a letter or guess the phrase: ");
        String userSelection = in.readLine();

      
            if (userSelection.length() > 1) {
                if (userSelection == gameWord) {
                    System.out.println("You are correct!");
                    lives = 0;
                } else {
                    System.out.println("Incorrect Guess");
                    lives = 0;
                }
            } else {
                Character userChar = userSelection.charAt(0);

                if (userSelection == "*") {

                    System.out.println("Creating new game");
                    lives = 0;
                }

                else if (userSelection == ".") {
                    System.out.println("Ending game");
                    lives = 0;
                } else {
                    if (gameWord.contains(userSelection)) {
                        for (int i = 0; i < gameWord.length(); i++) {

                            char[] blankChar = blanks.toCharArray();
                            if (gameWord.charAt(i) == userChar) {
                                blankChar[i] = userChar;
                                blanks = String.valueOf(blankChar);

                            }
                        }
                    } else {

                        lives = lives - 1;
                        System.out.println("You lost a life!");
                    }

                }

            }
        }
        return;
    }

    public static void main(String[] args) throws IOException {

      

        initblankArray();

        enterWord();



    }
}