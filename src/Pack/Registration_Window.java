package Pack;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Registration_Window extends Window {

    private javax.swing.JCheckBox JCShowPassword;
    private javax.swing.JLabel Label_ConfirmPassword;
    private javax.swing.JLabel Label_Have_An_Account;
    private javax.swing.JLabel Label_Password;
    private javax.swing.JLabel Label_Phone_Number;
    private javax.swing.JLabel Label_Registration;
    private javax.swing.JLabel Label_Username;
    private javax.swing.JButton btnMain;
    private javax.swing.JButton btnTo_Login;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JButton btnSubmit;

    public Registration_Window() {
        super();
        initComponents();
        setData();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {

        Main_Panel = new javax.swing.JPanel();
        Label_Username = new javax.swing.JLabel();
        Label_Password = new javax.swing.JLabel();
        Label_ConfirmPassword = new javax.swing.JLabel();
        btnMain = new javax.swing.JButton();
        TFPhone_Number = new javax.swing.JTextField();
        PFPassword = new javax.swing.JPasswordField();
        PFConfirm_Password = new javax.swing.JPasswordField();
        TFUsername = new javax.swing.JTextField();
        Label_Phone_Number = new javax.swing.JLabel();
        Label_Registration = new javax.swing.JLabel();
        btnTo_Login = new javax.swing.JButton();
        Label_Have_An_Account = new javax.swing.JLabel();
        JCShowPassword = new javax.swing.JCheckBox();
        btnSubmit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(100, 100));
        setSize(new java.awt.Dimension(720, 680));

        Main_Panel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        Label_Username.setText("Enter your username");

        Label_Password.setText("Enter your password");

        Label_ConfirmPassword.setText("Confirm your password");

        btnMain.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnMain.setText("Register");
        btnMain.addActionListener((java.awt.event.ActionEvent evt) -> {
            try {
                String sqlQuery = "INSERT INTO public.\"contact_data\"(\"username\", \"password\", \"phone_number\")	VALUES (?, ?, ?);";
                PreparedStatement pst = con.prepareStatement(sqlQuery);
                pst.setString(1, TFUsername.getText());
                pst.setString(2, PFPassword.getText());
                pst.setString(3, TFPhone_Number.getText());
                String password = PFPassword.getText();
                pst.executeUpdate();

                String phoneNumber = TFPhone_Number.getText();
                boolean perfect = true;
                if (password.length() < 4) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 4 characters long", "Password too short", JOptionPane.WARNING_MESSAGE);
                    return; //Prevent form submission
                }
                if (phoneNumber.length() < 13) {
                    JOptionPane.showMessageDialog(null, "Entered a wrong number!", "Wrong number.", JOptionPane.WARNING_MESSAGE);
                    return; //Prevent form submission
                }
                Registration_Window rw = new Registration_Window();
                //If username field if not filled
                if (TFUsername.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill in the username.");
                    rw.setVisible(true);
                } //If password field is not filled
                else if (PFPassword.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill in the password.");
                    rw.setVisible(true);
                } //If phone number field is not filled
                else if (TFPhone_Number.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill in the phone number.");
                    rw.setVisible(true);
                } //If something is filled wrongly
                //else if (PFPassword.getText() != PFConfirm_Password.getText()) {
                //  JOptionPane.showMessageDialog(null, "Passwords don't match.");
                // rw.setVisible(true);
                else if (perfect) {
                    JOptionPane.showMessageDialog(null, "You've registered successfully!");
                    Login_Window login = new Login_Window();
                    login.setVisible(true);
                }
            } catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(null, sQLException);
            }
            this.dispose();
        });

        TFPhone_Number.setText("+");
        TFPhone_Number.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                //Restrict the length to 12 digits
                if (TFPhone_Number.getText().length() > 12) {
                    e.consume(); //Discard any further input once the length limit is reached
                    JOptionPane.showMessageDialog(null, "Please enter a 12-digit password.", "Password too long.", JOptionPane.WARNING_MESSAGE);
                }
                // Restrict to digits only
                if (!Character.isDigit(c) && e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Discard the non-numeric character
                    JOptionPane.showMessageDialog(null, "Enter numbers only.", "Invalid input.", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        PFPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //Restrict the length to 12 digits
                if (PFPassword.getText().length() >= 12) {
                    e.consume(); //Discard any further input once the length limit is reached
                    JOptionPane.showMessageDialog(null, "Please enter a 12-digit password.", "Password too long.", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        PFConfirm_Password.addKeyListener(
                new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e
            ) {
                super.keyTyped(e);
            }
        }
        );
        Label_Phone_Number.setText(
                "Enter your phone number");

        Label_Registration.setFont(
                new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Label_Registration.setText(
                "REGISTRATION");
        Label_Registration.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnTo_Login.setFont(
                new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTo_Login.setForeground(
                new java.awt.Color(0, 102, 255));
        btnTo_Login.setText(
                "Login ");
        btnTo_Login.addActionListener(
                (java.awt.event.ActionEvent evt) -> {
                    Login_Window login = new Login_Window();
                    login.setVisible(true);
                    this.dispose();
                }
        );

        Label_Have_An_Account.setFont(
                new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        Label_Have_An_Account.setText(
                "Have an account?");

        JCShowPassword.setText(
                "Show password");
        JCShowPassword.addActionListener(
                (java.awt.event.ActionEvent evt) -> {
                    if (JCShowPassword.isSelected()) {
                        PFConfirm_Password.setEchoChar((char) 0);
                        PFPassword.setEchoChar((char) 0);
                    } else {
                        PFPassword.setEchoChar('*');
                        PFConfirm_Password.setEchoChar('*');
                    }
                }
        );

        javax.swing.GroupLayout jPanel_RegistrationLayout = new javax.swing.GroupLayout(Main_Panel);

        Main_Panel.setLayout(jPanel_RegistrationLayout);

        jPanel_RegistrationLayout.setHorizontalGroup(
                jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_RegistrationLayout.createSequentialGroup()
                                .addGroup(jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel_RegistrationLayout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(Label_Registration)
                                                                .addComponent(Label_Username, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(Label_ConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(Label_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(Label_Phone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(TFUsername)
                                                                .addComponent(TFPhone_Number)
                                                                .addComponent(PFPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                                                .addComponent(PFConfirm_Password))
                                                        .addComponent(JCShowPassword, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(jPanel_RegistrationLayout.createSequentialGroup()
                                                .addGap(104, 104, 104)
                                                .addGroup(jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                        .addComponent(btnTo_Login)
                                                        .addComponent(Label_Have_An_Account)
                                                        .addComponent(btnMain))))
                                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel_RegistrationLayout.setVerticalGroup(
                jPanel_RegistrationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_RegistrationLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(Label_Registration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Label_Username)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_Phone_Number)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFPhone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_Password)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_ConfirmPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PFConfirm_Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JCShowPassword)
                                .addGap(12, 12, 12)
                                .addComponent(btnMain)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(Label_Have_An_Account)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTo_Login)
                                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane()
                .setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(202, Short.MAX_VALUE)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(207, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(104, Short.MAX_VALUE)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(161, Short.MAX_VALUE))
        );

        pack();

        setLocationRelativeTo(
                null);
    }
}
