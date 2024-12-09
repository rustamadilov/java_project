package Pack;

import java.awt.HeadlessException;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login_Window extends Window {

    private javax.swing.JCheckBox JCShow_password;
    private javax.swing.JLabel Label_Login;
    private javax.swing.JLabel Label_Password;
    private javax.swing.JLabel Label_Phone_Number;
    private javax.swing.JLabel Label_To_Registration;
    private javax.swing.JLabel Label_Username;
    private javax.swing.JButton btnMain;
    private javax.swing.JButton btnTo_Registration;
    private javax.swing.JPanel Main_Panel;

    public Login_Window() {
        super();
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {

        Main_Panel = new javax.swing.JPanel();
        Main_Panel.setBackground(new java.awt.Color(102, 255, 255));

        Label_Username = new javax.swing.JLabel();
        Label_Password = new javax.swing.JLabel();
        TFUsername = new javax.swing.JTextField();
        btnMain = new javax.swing.JButton();
        btnTo_Registration = new javax.swing.JButton();
        Label_Login = new javax.swing.JLabel();
        Label_To_Registration = new javax.swing.JLabel();
        PFPassword = new javax.swing.JPasswordField();
        JCShow_password = new javax.swing.JCheckBox();
        Label_Phone_Number = new javax.swing.JLabel();
        TFPhone_Number = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_Panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        Main_Panel.setPreferredSize(new java.awt.Dimension(290, 310));

        Label_Username.setText("Username");

        Label_Password.setText("Password");

        btnMain.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMain.setText("Login");
        btnMain.setAlignmentY(0.0F);
        btnMain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMain.setMaximumSize(new java.awt.Dimension(75, 25));
        btnMain.setMinimumSize(new java.awt.Dimension(75, 25));
        btnMain.setPreferredSize(new java.awt.Dimension(75, 25));
        btnMain.addActionListener((java.awt.event.ActionEvent evt) -> {
            try {
                Class.forName("org.postgresql.Driver");
                String sqlUrl = "jdbc:postgresql://localhost:5432/Java_Project?user=postgres&password=rustam1004";
                Connection con1 = DriverManager.getConnection(sqlUrl);
                String sqlQuery = "SELECT \"username\", \"password\", \"phone_number\" "
                        + "FROM public.\"contact_data\" " + "WHERE \"username\" = ? AND \"password\" = ? AND \"phone_number\" = ?";
                PreparedStatement pst = con1.prepareStatement(sqlQuery);
                pst.setString(1, TFUsername.getText());
                pst.setString(2, new String(PFPassword.getPassword()));
                pst.setString(3, TFPhone_Number.getText());
                ResultSet rs = pst.executeQuery();
                boolean perfect = true;
                String password = PFPassword.getText();
                if (password.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 4 characters long.", "Password too short.", JOptionPane.WARNING_MESSAGE);
                    return; //Prevent form submission
                }
                perfect = true;
                if (rs.next()) {
                    Login_Window lw = new Login_Window();
                    //If username field if not filled
                    if (TFUsername.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in the username.");
                        lw.setVisible(true);
                    } //If password field is not filled
                    else if (PFPassword.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in the password.");
                        lw.setVisible(true);
                    } //If phone number field is not filled
                    else if (TFPhone_Number.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill in the phone number.");
                        lw.setVisible(true);
                    } else if (perfect) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        TFUsername.setEditable(false);
                        PFPassword.setEditable(false);
                        TFPhone_Number.setEditable(false);
                    }

                    btnMain.setEnabled(false);
                    Mavericks ms = new Mavericks();
                    ms.setVisible(true);
                    ms.setSender(TFUsername.getText());
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Login failed.");
                    TFUsername.setText("");
                    PFPassword.setText("");
                    TFPhone_Number.setText("+");
                }
            } catch (ClassNotFoundException | HeadlessException | SQLException classNotFoundException) {
                JOptionPane.showMessageDialog(null, classNotFoundException);
            }
        });

        btnTo_Registration.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTo_Registration.setForeground(new java.awt.Color(0, 102, 255));
        btnTo_Registration.setText("Register");
        btnTo_Registration.addActionListener((java.awt.event.ActionEvent evt) -> {
            Registration_Window registrationWindow = new Registration_Window();
            registrationWindow.setVisible(true);
            this.dispose();
        });

        Label_Login.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        Label_Login.setText("Login");
        Label_Login.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Label_Login.setAlignmentY(0.0F);

        Label_To_Registration.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        Label_To_Registration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_To_Registration.setText("Don't have an account?");

        Label_Phone_Number.setText("Phone number");

        JCShow_password.setText("Show Password");
        JCShow_password.addActionListener((java.awt.event.ActionEvent evt) -> {
            if (JCShow_password.isSelected()) {
                PFPassword.setEchoChar((char) 0);
            } else {
                PFPassword.setEchoChar('*');
            }
        });

        PFPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                {
                    //Allowing usage of backspace
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        return;
                    }

                    //Restrict the length to 12 digits (or any length you prefer)
                    if (PFPassword.getText().length() >= 12) {
                        e.consume(); //Discard any further input once the length limit is reached
                        JOptionPane.showMessageDialog(null, "Please enter a 12-digit password", "Password too long", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        TFPhone_Number.setText("+");
        TFPhone_Number.addKeyListener(
                new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Restrict the length to 12 digits
                if (TFPhone_Number.getText().length() > 12) {
                    e.consume(); //Discard any further input once the length limit is reached
                    JOptionPane.showMessageDialog(null, "Please enter a real phone number", "Wrong phone number", JOptionPane.WARNING_MESSAGE);
                }
                //Restricting to digits only
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); //Discard the non-numeric character
                    JOptionPane.showMessageDialog(null, "Enter numbers only", "Invalid input", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        );

        javax.swing.GroupLayout jPanel_LoginLayout = new javax.swing.GroupLayout(Main_Panel);
        Main_Panel.setLayout(jPanel_LoginLayout);
        jPanel_LoginLayout.setHorizontalGroup(
                jPanel_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_LoginLayout.createSequentialGroup()
                                .addGroup(jPanel_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel_LoginLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(JCShow_password))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                .addGroup(jPanel_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(108, 108, 108)
                                                                .addComponent(Label_Login))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(Label_Username, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(105, 105, 105)
                                                                .addComponent(btnMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(79, 79, 79)
                                                                .addComponent(Label_To_Registration, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(104, 104, 104)
                                                                .addComponent(btnTo_Registration))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(Label_Password))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(Label_Phone_Number)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel_LoginLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(PFPassword, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel_LoginLayout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(TFUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(TFPhone_Number, javax.swing.GroupLayout.Alignment.LEADING))))
                                .addGap(38, 38, 38))
        );
        jPanel_LoginLayout.setVerticalGroup(
                jPanel_LoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_LoginLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(Label_Login)
                                .addGap(12, 12, 12)
                                .addComponent(Label_Username)
                                .addGap(6, 6, 6)
                                .addComponent(TFUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_Phone_Number)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFPhone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_Password)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(JCShow_password)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnMain, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(Label_To_Registration)
                                .addGap(6, 6, 6)
                                .addComponent(btnTo_Registration, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane()
                .setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(215, Short.MAX_VALUE)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(205, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(113, Short.MAX_VALUE)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(194, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    public String getTFUsername() {
        return TFUsername.getText();
    }
}
