package Pack;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Add_Contacts extends Window {

    private javax.swing.JLabel Label_Contacts;
    private javax.swing.JLabel Label_Phone_Number;
    private javax.swing.JLabel Label_Username;
    private javax.swing.JButton btnMain;
    private javax.swing.JPanel Main_Panel;
    private javax.swing.JButton btnBack;
    private Mavericks maverics;
    private Add_Contacts ac;

    public Add_Contacts(Mavericks maverics) {
        super();
        this.maverics = maverics;
        initComponents();
        setData();
    }

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

    @Override
    public void keyTyped(KeyEvent e) {
        //Restrict the length to 12 digits (or any length you prefer)
        if (TFPhone_Number.getText().length() >= 12) {
            e.consume(); //Discard any further input once the length limit is reached
            JOptionPane.showMessageDialog(null, "Please enter a real number.", "Wrong number.", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {

        Main_Panel = new javax.swing.JPanel();
        Label_Contacts = new javax.swing.JLabel();
        Label_Phone_Number = new javax.swing.JLabel();
        Label_Username = new javax.swing.JLabel();
        TFPhone_Number = new javax.swing.JTextField();
        TFUsername = new javax.swing.JTextField();
        btnMain = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_Panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        Main_Panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Main_Panel.setInheritsPopupMenu(true);
        Main_Panel.setMaximumSize(new java.awt.Dimension(380, 294));

        Label_Contacts.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        Label_Contacts.setText("New contact");

        Label_Phone_Number.setText("Phone number:");

        Label_Username.setText("Username:");

        TFPhone_Number.setText("+");
        TFPhone_Number.addKeyListener(new KeyAdapter() {
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

        btnMain.setText("Create");
        btnMain.addActionListener((java.awt.event.ActionEvent evt) -> {
            try {
                String username = TFUsername.getText().trim();
                String phoneNumber = TFPhone_Number.getText().trim();

                String sqlQuery = "INSERT INTO public.\"new_contact\"(\"contact\") VALUES (?);";
                PreparedStatement pst = con.prepareStatement(sqlQuery);
                pst.setString(1, username);
                pst.executeUpdate();
                // Check for empty fields 
                if (username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, enter the username.");
                    return;
                }
                if (phoneNumber.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, enter the phone number.");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Contact has been added successfully!");

                if (TFUsername.getText().equals("") || TFPhone_Number.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, enter your details.");
                    ac.setVisible(true);
                }
                maverics.addContact(username);
                this.dispose();

            } catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(null, sQLException);
            }
        });

        btnBack.setText("Back");

        btnBack.addActionListener((java.awt.event.ActionEvent evt) -> {
            this.dispose();
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(Main_Panel);
        Main_Panel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(Label_Phone_Number)
                                                        .addComponent(Label_Username)
                                                        .addComponent(TFUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                                                        .addComponent(TFPhone_Number)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(122, 122, 122)
                                                .addComponent(Label_Contacts))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(151, 151, 151)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnMain))))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(14, Short.MAX_VALUE)
                                .addComponent(Label_Contacts)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(Label_Username)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Label_Phone_Number)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TFPhone_Number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnMain)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(btnBack)
                                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(600, 600, 600)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(947, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(229, 229, 229)
                                .addComponent(Main_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(563, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }
}
