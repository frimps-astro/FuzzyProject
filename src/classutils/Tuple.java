package classutils;

public class Tuple {
    private String left, right;

    public Tuple(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "left='" + left + '\'' +
                ", right='" + right + '\'' +
                '}';
    }
}
