//package battleship;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import static battleship.App.*;
//
//public class ServerBattle {
//    public static void main(String[] args) {
//        final int PORT = 12349;
//
//        try {
//            ServerSocket serverSocket = new ServerSocket(PORT);
//            System.out.println("Server is running and waiting for client connection...");
//
//            while (true) {
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostName());
//                ClientHandler clientHandler = new ClientHandler(clientSocket);
//                clientHandler.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//class ClientHandler extends Thread {
//    private final Socket clientSocket;
//    private BufferedReader in;
//
//    TextPlayer tp;
//
//    public ClientHandler(Socket socket) {
//        this.clientSocket = socket;
//    }
//
//    public void run() {
//        try {
//            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                PrintStream out = System.out;
//                V2ShipFactory factory = new V2ShipFactory();
//                printWelcomeMessage(out);
//                printCueWord(out);
//                TextPlayer player1 = playerIdentification("A", in, out, factory);
//                TextPlayer player2 = playerIdentification("B", in, out, factory);
//                App app = new App(player1, player2);
//                app.doPlacementPhase();
//                app.doAttackingPhase();
//            }
//
//            in.close();
//            clientSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
