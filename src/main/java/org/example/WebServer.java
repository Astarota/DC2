package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Simple web server.
 */
public class WebServer {
    public static void main(String[] args) {
        // Port number for http request
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        // The maximum queue length for incoming connection
        int queueLength = args.length > 2 ? Integer.parseInt(args[2]) : 50;;

        ThreadSafeQueue<Struct> queue = new ThreadSafeQueue<>();

        int maxNum = 6;
        int currNum = 0;

        while(currNum<maxNum){
            ThreadRunning threadRunning= new ThreadRunning(queue);
            threadRunning.start();
            currNum++;
        }


        try (ServerSocket serverSocket = new ServerSocket(port, queueLength)) {
            System.out.println("Web Server is starting up, listening at port " + port + ".");
            System.out.println("You can access http://localhost:" + port + " now.");

            while (true) {
                // Make the server socket wait for the next client request
                Socket socket = serverSocket.accept();

                System.out.println("Got connection!");

                // To read input from the client
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                try {
                    // Get request
                    HttpRequest request = HttpRequest.parse(input);

                    // Process request
//                    Processor proc = new Processor(socket,request);
//                    proc.start();
                    Struct struct = new Struct(socket, request);

                    queue.add(struct);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            for (int i = 0; i < currNum; i++) {
                queue.add(null);
            }
            System.out.println("Server has been shutdown!");
        }
    }
}