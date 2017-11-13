package com.connect6.ClientCommunication;
import com.connect6.Request;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestThread extends Thread {

    private Socket clientSocket;
    private ObjectOutputStream outputStream;

    public RequestThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            while (!clientSocket.isClosed()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.print("Failed to create request thread");
        }
    }

    public void sendRequest(Request request) {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                outputStream.writeObject(request);
                outputStream.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}