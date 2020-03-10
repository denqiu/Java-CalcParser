@SuppressWarnings("serial")
public class CalcError extends Exception {
	private String message;

	public CalcError(String message) {
		super(); this.message = message;
	}

	public String getMessage() {
		return message;
	}
}