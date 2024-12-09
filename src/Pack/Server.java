package Pack;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static Server server;
    private FileOutputStream fos;
    private final int PORT = 54321;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private Server() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket socket = server.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                ClientHandler cl = new ClientHandler(socket);
                clients.add(cl);
                new Thread(cl).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            }
            catch (Exception e) {e.printStackTrace();}
        }

        @Override
        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    if (inputLine.startsWith("TEXT: "))
                        broadcastText(inputLine, this);
                    //else broadcastFile(inputLine,in);
                }
            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }

        public void broadcastText(String message, ClientHandler sender) {
            for(ClientHandler cl: clients) {
                if(cl != sender)
                    cl.sendMessage(message);
            }
        }

//        private void broadcastFile(File file) {
//            for (ClientHandler cl : clients) {
//                try (FileInputStream fis = new FileInputStream(file);
//                     OutputStream out = cl.socket.getOutputStream()) {
//
//                    byte[] buffer = new byte[4096];
//                    int bytesRead;
//                    while ((bytesRead = fis.read(buffer)) != -1) {
//                        out.write(buffer, 0, bytesRead);
//                    }
//                    System.out.println("File broadcasted: " + file.getName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

    public static synchronized Server getInstance() {
        if (server == null) {
            Server server = new Server();
        }
        return server;
    }

    public static void main(String[] args) {
        Server.getInstance();
    }
}
