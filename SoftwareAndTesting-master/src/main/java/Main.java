import forms.MainGUI;

public class Main {
    public static void main(String[] args) {

        MainGUI mainGUI = new MainGUI("CarX");
        mainGUI.setVisible(true);
        mainGUI.setExtendedState(mainGUI.MAXIMIZED_BOTH);
    }
}

