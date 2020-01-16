import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;


//    Activity two reponses
/**
 * 4. 
 * Firstly, the file containing the review in imputted as a parameter and is converted to a string using the textToString method
 * A netReview value variable was defined as well as a placeholder for each word in the review
 * The placeholder variable is defined by passing the review through a for loop, where if at a particular index the char is a space,
 * the placeholder word is passed through the sentimentVal method and the value is added onto the netRev variable
 * This process is done over and over again until the string is completely gone through
 * The netRev is returned at the end
 * 
 * 5.
 * a. Yes, the ratings make sense because I tested my function my printing the sentament value and ensuring that the value correlates
 * to the proper rating defined in the assignement
 * b. One way that I can edit the totalSentiment method is by perhaps using a logistic function to "average" out all the sentiments in the cvc file
 * or I could use a logistic function or perhaps multiply the end sentiment by some constant to reduce the severity of the final result
 * 
 * 6. 
 * a. The total sentament can be any double value because the value is determenined by the number of words in the review, and a review can be any length
 * In the method, it defaults to 0 if the sentament is more than 15, and a review of 0 is bad, but a Tot Sent of > 15 is good
 * b. He could fix this by adding an if statement with the expression (totalSentiment > 15) and returns 5, and then make the first initializing if statment 
 * to an else if statement
 */

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  public static double totalSentiment(String fileName)
  {
    double netRev = 0;
    String temp = "";
    String text = textToString(fileName);
      
    text = text.replaceAll("\\p{Punct}", "");

      
    for(int i = 0; i < text.length(); ++i)
    {
      if(text.substring(i, i+1).equals(" "))
      {
        netRev += Review.sentimentVal(temp);

        temp = "";
      }
      else temp += text.substring(i, i+1);
    }
    return netRev;
  }

  public static int starRating(String fileName)
  {
    String text = textToString(fileName);
    double round = 2.5 * Math.tanh(totalSentiment(text)) + 3;
    return (int)round;
  }

  public static void main(String args[])
  {
    System.out.println(Review.totalSentiment("26WestReview.txt"));
  }

}

