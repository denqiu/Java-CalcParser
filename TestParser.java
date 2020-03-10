import java.util.Scanner;

public class TestParser {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter statement: ");
		String expression = s.nextLine();
		System.out.println(expression + " parses as " + new CalcParser(expression).getAST());
		s.close();
	}
}