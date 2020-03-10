/**
 * A CalcParser is a parser that parses a String containing a numeric
 * expression. We handle both real numbers and integers, the operators +,-,*, and / with the usual
 * precedence and associativity, and parentheses.
 */
public class CalcParser {
	/**
	 * We use a CalcLexer object to tokenize the input string.
	 */
	private CalcLexer lexer;

	/**
	 * The abstract syntax tree resulting from parsing the expression (if no error).
	 */
	private ParseTree ast;

	/**
	 * Constructor for CalcParser. 
	 * @param s the string to be parsed
	 */
	public CalcParser(String s) {
		/*
		 * First make a CalcLexer to hold the string. 
		 * This will get an error immediately if the first token is bad, so check for that.
		 */
		try {
			lexer = new CalcLexer(s);
			//Now parse the expression and get the result.
			ast = new ParseTree(parseExpression());
			//After the expression we should be at the end of the input.
			match(CalcLexer.EOLN_TOKEN);
		} catch (CalcError c) {
			System.out.println(c.getMessage());
			return;
		}
	}
	
	public ParseTree getAST() {
		return ast;
	}

	/**
	 * Match a given token and advance to the next. This utility is used by our
	 * parsing routines. If the given token does not match lexer.nextToken(), we
	 * generate an appropriate error message. Advancing to the next token may also
	 * cause an error.
	 * @param token the token that must match
	 * @throws CalcError
	 */
	private void match(int token) throws CalcError {
		/*
		 * First check that the current token matches the one we were passed; 
		 * if not, make an error.
		 */
		if (lexer.nextToken() != token) {
			if (token == CalcLexer.EOLN_TOKEN) {
				throw new CalcError("Unexpected text after the expression.");
			} else if (token == CalcLexer.NUMBER_TOKEN) {
				throw new CalcError("Parse error: Expected a number.");
			} else {
				throw new CalcError("Parse error: Expected a " + ((char) token) + ".");
			}
		}
		//Now advance to the next token.
		lexer.advance();
	}
	
	/**
	 * Parse an expression. If any error occurs we return immediately.
	 * @return the parse tree corresponding to the expression or garbage in case of errors.
	 * @throws CalcError
	 */
	private TreeNode parseExpression() throws CalcError {
		//<expression> ::= <addexp> { ('#' <addexp>) }
		TreeNode result = parseAddExp();
		while (true) {
			if (lexer.nextToken() == '#') {
				match('#'); result = new TreeNode("#",  result, parseAddExp());
			} else {
				return result;
			}
		}
	}

	/**
	 * Parse an addexp, a subexpression at the precedence level of + and -. If any error 
	 * occurs we return immediately.
	 * @return the sub tree corresponding to the addexp or garbage in case of errors.
	 * @throws CalcError
	 */
	private TreeNode parseAddExp() throws CalcError {
		//<addexp> ::= <mulexp> { ('+' <mulexp>) | ('-' <mulexp>) }
		TreeNode result = parseMulexp();
		while (true) {
			if (lexer.nextToken() == '+') {
				match('+'); result = new TreeNode("+",  result, parseMulexp());
			} else if (lexer.nextToken() == '-') {
				match('-'); result = new TreeNode("-",  result, parseMulexp());
			} else {
				return result;
			}
		}
	}
	
	/**
	 * Parse a mulexp, a subexpression at the precedence level of * and /. If any
	 * error occurs we return immediately.
	 * @return the sub-tree corresponding to the mulexp or garbage in case of errors.
	 * @throws CalcError
	 */
	private TreeNode parseMulexp() throws CalcError {
		//<mulexp> ::= <exponentexp> { ('*' <exponentexp>) | ('/' <exponentexp>) }
		TreeNode result = parseExponentexp();
		while (true) {
			if (lexer.nextToken() == '*') {
				//result *= parseExponentexp();
				match('*'); result = new TreeNode("*", result, parseExponentexp());
			} else if (lexer.nextToken() == '/') {
				//result /= parseExponentexp();
				match('/'); result = new TreeNode("/",  result, parseExponentexp());
			} else {
				return result;
			}
		}
	}
	
	/**
	 * Parse a exponentexp, a subexpression at the precedence level of ^. If any
	 * error occurs we return immediately.
	 * @return the sub-tree corresponding to the exponentexp or garbage in case of errors.
	 * @throws CalcError
	 */
	private TreeNode parseExponentexp() throws CalcError {
		//<exponentexp> ::= <rootexp> { ('^' <rootexp>) }
		TreeNode result = parseRootexp(); int i = 0;
		while (true) {
			if (lexer.nextToken() == '^') {
				match('^'); result = new TreeNode("^", result, parseRootexp());
				if (lexer.isEndCheckExponent()) {//corrects the order of the tree nodes if the second '^' operator has been found.
					i++;
					if (i == 1) {
						result = new TreeNode(result.getSymbol(), result.getRight(), result.getLeft());
					} else {
						result = new TreeNode(result.getSymbol(), result.getLeft().getRight(), new TreeNode(result.getSymbol(), result.getLeft().getLeft(), result.getRight()));
					}
				}
			} else {
				return result;
			}
		}
	}

	/**
	 * Parse a rootexp, which is a constant or parenthesized subexpression. If any
	 * error occurs we return immediately.
	 * @return the sub-tree corresponding to the rootexp or garbage in case of errors
	 * @throws CalcError
	 */
	private TreeNode parseRootexp() throws CalcError {
		TreeNode result = null;
		//<rootexp> ::= '(' <expression> ')'
		if (lexer.nextToken() == '(') {
			match('('); result = parseExpression(); match(')');
		} else if (lexer.nextToken() == CalcLexer.NUMBER_TOKEN) {
			// <rootexp> ::= number
			result = new TreeNode(Double.toString(lexer.getNum())); match(CalcLexer.NUMBER_TOKEN);
		} else {
			throw new CalcError("Parse Error: Expected a number or a parenthesis.");
		}
		return result;
	}
}