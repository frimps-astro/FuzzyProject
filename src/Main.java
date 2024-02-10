import ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        UserInterface userInterface = UserInterface.getInstance();
        userInterface.createUI();
    }
}
