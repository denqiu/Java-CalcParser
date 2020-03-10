public class ParseTree {
	private TreeNode root;

	public ParseTree(TreeNode t) {
		root = t;
	}

	public TreeNode getRoot() {
		return root;
	}
	
	@Override
	public String toString() {
		if (root != null) {
			return pretty_print(root, 0);
		} else {
			return "No tree";
		}
	}

	private String pretty_print(TreeNode current_node, int offset) {
		int spacer = 5;
		String print_string = "";
		TreeNode rightChild = current_node.getRight();
		if (rightChild != null) {
			print_string += pretty_print(rightChild, offset + spacer + current_node.getSymbol().length() - 1);
		}
		String indent = makeString(offset, " "), dots = current_node.isLeaf() ? "" : makeString(spacer, ".");
		print_string += "\n" + indent + current_node.getSymbol() + dots;
		TreeNode leftChild = current_node.getLeft();
		if (leftChild != null) {
			print_string += pretty_print(leftChild, offset + spacer);
		}
		return print_string;
	}

	private String makeString(int length, String s) {
		String result = "";
		for (int i = 0; i < length; i++) {
			if (s == "." && i == length - 1) {
				result += "|";
			} else {
				result += s;
			}
		}
		return result;
	}
}