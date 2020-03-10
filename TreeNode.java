public class TreeNode {
	private String symbol;
	private TreeNode left, right;

	public TreeNode(String symbol, TreeNode left, TreeNode right) {
		this.symbol = symbol; this.left = left; this.right = right;
	}

	public TreeNode(String symbol) {
		this(symbol, null, null);
	}

	public TreeNode getRight() {
		return right;
	}

	public TreeNode getLeft() {
		return left;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public boolean isLeaf() {
		return left == null && right == null;
	}

	public String getSymbol() {
		return symbol;
	}
}