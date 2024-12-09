package Pack;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public abstract class Window extends javax.swing.JFrame {

    public Connection con;
    protected JTextField TFUsername, TFPhone_Number;
    protected JPasswordField PFPassword, PFConfirm_Password;
    protected JButton btnMain;
    protected JPanel Main_Panel;

    public Connection setData() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException classNotFoundException) {
            JOptionPane.showMessageDialog(null, classNotFoundException);
        }
        try {
            String sqlUsername = "postgres";
            String sqlPassword = "rustam1004";
            String sqlUrl = "jdbc:postgresql://localhost:5432/Java_Project";

            con = DriverManager.getConnection(sqlUrl, sqlUsername, sqlPassword);

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, sQLException);
        }
        return con;
    }

    public void keyTyped(KeyEvent e) {
    }

    public Window() {
    }

}
