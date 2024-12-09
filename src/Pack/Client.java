package Pack;

import java.io.*;
import java.net.*;

public class Client {
    private final int PORT = 54321;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private OutputStream fout;
    private FileInputStream fis;
    private MessageListener listener;

    public Client(MessageListener listener) {
        this.listener = listener;

        try {
            socket = new Socket("localhost", PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to listen for incoming messages
            new Thread(this::receiveMessages).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("TEXT: "))
                    if (listener != null) {
                        listener.onMessageReceived(message);
                    } // Pass received message to the listener
                //else receiveFile(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void receiveFile(String filename) {
//        try{
//            String[] parts = filename.split(":",3);
//            String fileName = parts[0].trim();
//            long fileSize = Long.parseLong(parts[1].trim());
//
//            File file = new File("C:\\Users\\User\\OneDrive\\Desktop\\Project final verse\\src\\Pack\\" + fileName);
//            FileOutputStream fos = new FileOutputStream(file);
//
//            InputStream input = socket.getInputStream();
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            long totalRead = 0;
//
//            while (totalRead < fileSize && (bytesRead = input.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//                totalRead += bytesRead;
//            }
//            fos.close();
//
//            if (listener != null) {
//                listener.onMessageReceived("Received file: " + file.getAbsolutePath());
//            }
//        }
//        catch(Exception e){e.printStackTrace();}
//    }

    public void sendMessage(String message) {
        out.println(message); // Send message to the server
    }

    public void sendFile(File file) {
        try{
            System.out.println("Received file: " + file.getAbsolutePath());
            out.println(file.getName());
            out.println(file.length());

            fis = new FileInputStream(file);
            fout = socket.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = fis.read(buffer)) != -1) {
                fout.write(buffer, 0, bytesRead);
            }
            fis.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public interface MessageListener {
        void onMessageReceived(String message);
    }
}
