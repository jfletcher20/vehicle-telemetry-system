//package slusac;
//
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class ObjaviteljDogadaja {
//    private static final List<PrintWriter> clients = new CopyOnWriteArrayList<>();
//
//    public static void addClient(PrintWriter client) {
//        clients.add(client);
//    }
//
//    public static void removeClient(PrintWriter client) {
//        clients.remove(client);
//    }
//
//    public static void broadcast(String message) {
//        for (PrintWriter client : clients) {
//            client.println("data: " + message + "\n");
//            client.flush();
//        }
//    }
//}
