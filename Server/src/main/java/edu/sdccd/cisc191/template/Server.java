//package edu.sdccd.cisc191.template;
//
//import java.net.*;
//import java.io.*;
//
///**
// * This program is a server that takes connection requests on
// * the port specified by the constant LISTENING_PORT.  When a
// * connection is opened, the program sends the current time to
// * the connected socket.  The program will continue to receive
// * and process connections until it is killed (by a CONTROL-C,
// * for example).  Note that this server processes each connection
// * as it is received, rather than creating a separate thread
// * to process the connection.
// */
//public class Server {
//    private ServerSocket serverSocket;
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void start(int port) throws Exception {
//        serverSocket = new ServerSocket(port);
//        clientSocket = serverSocket.accept();
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//        String jsonRequest;
//
//        while ((jsonRequest = in.readLine()) != null) {
//            HydrationRequest request =  HydrationRequest.toRequest(jsonRequest);
//            HydrationResponse response = new  HydrationResponse(request, 5000,25000,5,4,new String[]{"AC", "LEATHER"});
//            out.println( HydrationResponse.toJSON(response));
//        }
//    }
//
//    public void stop() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//        serverSocket.close();
//    }
//
//    public static void main(String[] args) {
//        Server server = new Server();
//        try {
//            server.start(4444);
//            server.stop();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//} //end class Server
