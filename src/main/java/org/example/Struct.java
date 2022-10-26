package org.example;

import java.net.Socket;

public class Struct {
    public Socket socket;
    public HttpRequest request;

    public Struct(Socket socket, HttpRequest request){
        this.socket = socket;
        this.request = request;
    }
}