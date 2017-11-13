package com.connect6.ServerCommunication;

import com.connect6.Client;
import com.connect6.Request;
import com.connect6.Response;
import com.connect6.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseThread extends Thread {

    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ResponseProcessor responseProcessor;

    public ResponseThread(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.responseProcessor = new ResponseProcessor(server);
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            while (!clientSocket.isClosed()) {
                final Request request = receiveResponse();
                if (request != null) {
                    responseProcessor.process(request);
                }
            }
        } catch (IOException e) {
            System.out.print("Failed to create response thread");
        }
    }

    private Request receiveResponse() {
        Request request = null;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                request = (Request) inputStream.readObject();
            }
        } catch (Exception e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return request;
    }
}