public class TestInterpreter {
	private static int testNumber = 1;
	
	public static void main(String[] args) {
		testEquals(interpret("2^3^2"), 512);
		testEquals(interpret("2^3"), 8);
		testEquals(interpret("2+3*2^2"), 14);
		testEquals(interpret("2^3"), 8);
		testEquals(interpret("2+3/2^2#3/2*4+2^3"),14);
		testEquals(interpret("2^(2^2#3^1)"),16);
		testEquals(interpret("2^1^2#3^1"),3);
		testEquals(interpret("(2+3)/2^(2#3)/2*(4+2)^3"),67.5);
		testEquals(interpret("1+2^3/4-5+6/2+(2-4)^2"),5);
		testEquals(interpret("1+2^3/4-5+6/2+(2-4)^2#6"),6);
	}
	
	/**
	 * This method evaluates a parse tree
	 * @param pt The AST (abstract syntax tree) to be evaluated
	 * @return the result of the evaluation
	 */
	private static double evaluate(ParseTree pt) {	
		System.out.println(pt.toString());
		return compute(parse(pt.getRoot()));
	}
	
	private static Object[] parse(TreeNode currentRoot) {		
		TreeNode[] getRoot = new TreeNode[] {currentRoot.getLeft(), currentRoot.getRight()};
		Object[] parse = new Object[3];
		String symbol;
		TreeNode nextRoot;
		int j = 0;
		for (int i = 0; j < getRoot.length; j++) {
			if (parse[j] == null) {
				nextRoot = getRoot[i];
				if (nextRoot != null) {
					symbol = nextRoot.getSymbol();
					parse[j] = (Character.isDigit(symbol.charAt(0))) ? Double.valueOf(symbol) : parse(nextRoot);
					i++;
				} 
			}
		}
		parse[j] = currentRoot.getSymbol();
		return parse;
	}
	
	private static double compute(Object[] parseTree) {
		for (int i = 0; i < parseTree.length; i++) {
			if (parseTree[i] instanceof Object[]) {
				parseTree[i] = compute((Object[]) parseTree[i]);
			}
		}
		Double left, right, evaluate = null; 
		if (parseTree[0] != null && parseTree[1] != null) {
			left = (Double) parseTree[0]; right = (Double) parseTree[1];
			String operator = (String) parseTree[2];
			if (operator == "^") {
				evaluate = Math.pow(left, right);
			} else if (operator == "*") {
				evaluate = left*right;
			} else if (operator == "/") {
				evaluate = left/right;
			} else if (operator == "+") {
				evaluate = left+right;
			} else if (operator == "-") {
				evaluate = left-right;
			} else if (operator == "#") {
				evaluate = Math.max(left, right);
			}
		}
		return evaluate;
	}

	private static double interpret(String exp) {
		return evaluate(new CalcParser(exp).getAST());
	}

	private static void testEquals(double actual, double expected) {
		System.out.println("\nTest " + testNumber + " results");
		try {
			System.out.println("\t" + "Actual result = " + actual);
			System.out.println("\t" + "Expected result = " + expected);
			System.out.println("\tTest " + testNumber + " " + translate(actual == expected));
		} catch (Exception e) {
			System.out.println("\tTest " + testNumber + " failed");
		} finally {
			testNumber++;
		}
	}

	private static String translate(boolean b) {
		return (b) ? "passed" : "failed";
	}
}