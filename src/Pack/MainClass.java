package Pack;

public class MainClass {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login_Window lw = new Login_Window();
                lw.setVisible(true);
            }
        });
    }
}
