import java.util.StringTokenizer;

/**
 * A CalcLexer provides a simple scanner for a 
 * CalcParser. We hold the string being parsed, and the
 * CalcParser uses us to read the string as a sequence
 * of tokens.
 */
public class CalcLexer {
  /**
   * The string being parsed, held in a 
   * StringTokenizer.
   */
  private StringTokenizer tokens;

  /**
   * The current token.
   */
  private int tokenChar;

  /**
   * If the current token is NUMBER_TOKEN, this is 
   * the number in question.
   */
  private double tokenNum;
  
  private boolean startCheckExponent, endCheckExponent;

  /**
   * Non-character values for tokenChar. By choosing 
   * negative values we are certain not to collide 
   * with any char values stored in the int tokenChar.
   */
  public static final int NUMBER_TOKEN = -1, EOLN_TOKEN = -2;

  /**
   * Constructor for a CalcLexer. Our parameter is the string to be tokenized.
   * @param s the String to be tokenized
   * @throws CalcError 
   */
  public CalcLexer(String s) throws CalcError {
	/*
	 * We use a StringTokenizer to tokenize the string. 
	 * Our delimiters are the operators, parens, and white space. 
	 * By making the third parameter true we instruct the StringTokenizer 
	 * to return those delimiters as tokens.
	 */
	  tokens = new StringTokenizer(s, "\t\n\r#+-*/^()", true);
	/*
	 * Start by advancing to the first token. 
	 * Note that this may get an error, which
	 * would set our errorMessage instead of setting tokenChar.
	 */
	  System.out.println("token count = " + tokens.countTokens());
	  advance();
  }

  /**
   * Advance to the next token. We don't return anything; 
   * the caller must use nextToken() to see what that token is.
   * @throws CalcError 
   */
  public void advance() throws CalcError {
	/*
	 * White space is returned as a token by our StringTokenizer, 
	 * but we will loop until something other than white space has been found.
	 */
    while (true) {
      //If we're at the end, make it an EOLN_TOKEN.
      if (!tokens.hasMoreTokens()) {
        tokenChar = EOLN_TOKEN;
        return;
      }
      //Get a token--if it looks like a number, make it a NUMBER_TOKEN.
      String s = tokens.nextToken();
      System.out.println("next token = " + s);
      char c = s.charAt(0);
      if (Character.isWhitespace(c)) {
    	  System.out.println("Skipping whitespace");
    	  continue;
      }
      if (s.length() > 1 || Character.isDigit(c)) {
    	  try {
    		  tokenNum = Double.valueOf(s).doubleValue();
    		  tokenChar = NUMBER_TOKEN;
    	  } catch (NumberFormatException n) {
    		  throw new CalcError("Lexer error: Illegal format for a number");
    	  }
    	  return;
      } else if (!Character.isWhitespace(c)) {
    	  // Any other single character that is not white space is a token.
    	  tokenChar = c; 
    	  if (tokenChar == '^') {
    		  if (startCheckExponent) {//At this point the second '^' operator has been found. Process repeats.
				startCheckExponent = false; endCheckExponent = true;
    		  } else {//starts searching for the second '^' operator
        		  startCheckExponent = true; endCheckExponent = false;
    		  }
    	  } else {
    		  if (startCheckExponent) {//Occurs if an operator other than the second '^' operator has been found.
        		  startCheckExponent = endCheckExponent = false;
        	  }
    	  }
    	  return;
      }
    }
  }

  /**
   * Return the value of a numeric token. This should 
   * only be called when nextToken() reports a NUMBER_TOKEN.
   * @return the double value of the number
   */
  public double getNum() {
	  return tokenNum;
  }

  /**
   * Return the next token. Repeated calls will
   * return the same token again; the caller should
   * use advance() to advance to another token.
   * @return the next token as an int
   */
  public int nextToken() {
	  return tokenChar;
  }
 
  /**
   * Determines whether the '^' operator has been found at least twice in a row.
   * @return true if '^' is the next operator to be found after the first '^' operator otherwise false.
   */
  public boolean isEndCheckExponent() {
	return endCheckExponent;
  }
}