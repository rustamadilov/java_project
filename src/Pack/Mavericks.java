package Pack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Mavericks extends Window {

    private Client client;
    private String sender;
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField inputField;
    private JButton sendButton;
    private JButton uploadMediaButton;
    private JTextField newContactField;
    private DefaultListModel<String> contactListModel;
    private Map<String, JPanel> messagesArea;
    private Map<String, ImageIcon> avatars;
    private String currentContact;

    public Mavericks() {
        avatars = initializeAvatars();
        frame = setupFrame();
        contactListModel = new DefaultListModel<>();
        messagesArea = new HashMap<>();
        setData();
        loadContactsFromDatabase();
        setupContacts();
        setupUI();
        client = new Client(this::onMessageReceived);
        frame.setVisible(true);
    }

    public void setSender(String userName) {
        this.sender = userName;
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (message.startsWith("Received file: ")) {
                    String filePath = message.substring(15).trim();
                    File file = new File(filePath);

                    // Display file based on type
                    if (filePath.endsWith(".txt")) {
                        displayTextFile(file);
                    } else if (filePath.endsWith(".jpg") || filePath.endsWith(".png")) {
                        displayImageFile(file);
                    } else {
                        JOptionPane.showMessageDialog(frame, "File received: " + filePath);
                    }
                } else {
                    // Handle text message
                    String[] parts = message.split(": ", 4);
                    if (parts.length == 4) {
                        String recipient = parts[1].trim();
                        String sender = parts[2].trim();
                        String content = parts[3].trim();

                        // Add the received message to the appropriate chat
                        if (!messagesArea.containsKey(sender)) {
                            messagesArea.put(sender, createNewMessagePanel());
                        }

                        JPanel senderPanel = messagesArea.get(sender);
                        senderPanel.add(new JLabel(sender + ": " + content));

                        // Update UI if the message is for the current chat
                        if (currentContact != null && currentContact.equals(sender)) {
                            updateChatArea();
                        }
                    } else {
                        showErrorDialog("Invalid message format received: " + message);
                    }
                }
            } catch (Exception e) {
                showErrorDialog("Error processing received message: " + e.getMessage());
            }
        });
    }

    private void displayTextFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JTextArea textArea = new JTextArea();
            textArea.read(reader, null);
            reader.close();

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(frame, scrollPane, "Text File Content", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showErrorDialog("Error reading file: " + e.getMessage());
        }
    }

    private void displayImageFile(File file) {
        try {
            ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
            JLabel imageLabel = new JLabel(imageIcon);

            JScrollPane scrollPane = new JScrollPane(imageLabel);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(frame, scrollPane, "Image File", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showErrorDialog("Error displaying image: " + e.getMessage());
        }
    }

    private Map<String, ImageIcon> initializeAvatars() {
        Map<String, ImageIcon> avatars = new HashMap<>();
        String defaultAvatarPath = "src/avatar.png";
        for (String name : new String[]{"Влад", "Солех", "Насир", "Диана"}) {
            avatars.put(name, new ImageIcon(new ImageIcon(defaultAvatarPath)
                    .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        }
        return avatars;
    }

    private JFrame setupFrame() {
        JFrame frame = new JFrame("Dragunov예요");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        return frame;
    }

    private void setupContacts() {
        avatars.keySet().forEach(contact -> {
            contactListModel.addElement(contact);
            messagesArea.put(contact, createNewMessagePanel());
        });
    }

    private void setupUI() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createChatPanel());
        splitPane.setDividerLocation(300);

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(createInputPanel(), BorderLayout.SOUTH);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        JList<String> contactList = new JList<>(contactListModel);
        contactList.setCellRenderer(new ContactListCellRenderer());
        contactList.addListSelectionListener(this::onContactSelected);
        leftPanel.add(new JScrollPane(contactList), BorderLayout.CENTER);
        leftPanel.add(createAddContactPanel(), BorderLayout.SOUTH);
        return leftPanel;
    }

    private JScrollPane createChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return new JScrollPane(chatPanel);
    }

    private JPanel createAddContactPanel() {
        JPanel addContactPanel = new JPanel(new BorderLayout(5, 5));
        newContactField = new JTextField();
        JButton addContactButton = new JButton("➕");
        addContactButton.addActionListener(e -> addContact());
        addContactPanel.add(newContactField, BorderLayout.CENTER);
        addContactPanel.add(addContactButton, BorderLayout.EAST);
        return addContactPanel;
    }

    private void loadContactsFromDatabase() {
        try {
            String sqlQuery = "SELECT \"contact\" FROM public.\"new_contact\"";
            PreparedStatement pst = con.prepareStatement(sqlQuery);
            ResultSet rs = pst.executeQuery();
            contactListModel.clear();

            while (rs.next()) {
                String username = rs.getString("Contact");
                if (!contactListModel.contains(username)) {
                    contactListModel.addElement(username);  // Add username to the JList model
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading contacts from database: " + e.getMessage());
        }
    }
//

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        uploadMediaButton = new JButton("📎");
        uploadMediaButton.addActionListener(e -> uploadMedia());

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(500, 40));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        sendButton = new JButton("\uD83D\uDCAC");
        sendButton.addActionListener(e -> sendMessage());

        inputPanel.add(uploadMediaButton, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        return inputPanel;
    }

    private JPanel createNewMessagePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        return panel;
    }

    private void onContactSelected(ListSelectionEvent e) {
        JList<?> list = (JList<?>) e.getSource();
        currentContact = (String) list.getSelectedValue();

        // Ensure messages area is initialized for the selected contact
        if (currentContact != null && !messagesArea.containsKey(currentContact)) {
            messagesArea.put(currentContact, createNewMessagePanel());
        }

        updateChatArea();
    }

    public void addContact(String contact) {

        if (contact != null && !contact.isEmpty() && !contactListModel.contains(contact)) {
            contactListModel.addElement(contact);  // Add to the list model
            avatars.put(contact, new ImageIcon("avatar.png"));  // Optionally set a default avatar

            // Initialize the message area for the new contact
            messagesArea.put(contact, createNewMessagePanel());  // Create a new message area for this contact
        }
    }

    private void addContact() {
        String newContact = newContactField.getText().trim();
        if (newContact.isEmpty() || contactListModel.contains(newContact)) {
            Add_Contacts contact = new Add_Contacts(this);  // Pass Mavericks reference
            contact.setVisible(true);
        } else {
            contactListModel.addElement(newContact);
            avatars.put(newContact, new ImageIcon("avatar.png"));
            messagesArea.put(newContact, createNewMessagePanel());
            newContactField.setText("");
        }
    }

    private void updateChatArea() {
        chatPanel.removeAll();
        if (currentContact != null && messagesArea.containsKey(currentContact)) {
            chatPanel.add(messagesArea.get(currentContact));
        }
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (currentContact == null) {
            showErrorDialog("Select a chat to start messaging.");
        } else if (!message.isEmpty()) {
            // Shows to which contact the message was sent
            String recipient = currentContact;
            client.sendMessage("TEXT" + ": " + recipient + ": " + sender + ": " + message);

            // Ensure the messages area for the current contact exists
            JPanel currentContactMessages = messagesArea.get(currentContact);
            if (currentContactMessages != null) {
                currentContactMessages.add(new JLabel("\uD83D\uDC64: " + message));
                inputField.setText("");
                updateChatArea();
            } else {
                showErrorDialog("Error: No message panel for this contact.");
            }
        }
    }

    private void uploadMedia() {
        if (currentContact == null) {
            showErrorDialog("Select a chat.");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            handleMediaFile(fileChooser.getSelectedFile());
        }
    }

    private void handleMediaFile(File file) {
        if (file.exists()) {
            client.sendFile(file);
        }

        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            addMediaToChat(new JLabel(new ImageIcon(new ImageIcon(file.getAbsolutePath())
                    .getImage().getScaledInstance(360, 360, Image.SCALE_SMOOTH))));
        } else {
            addMediaToChat(new JLabel(fileName));
        }
        updateChatArea();
    }

    private void addMediaToChat(JLabel label) {
        messagesArea.get(currentContact).add(label);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private class ContactListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon(avatars.get(value));
            label.setBorder(new EmptyBorder(5, 5, 5, 5));
            return label;
        }
    }
}