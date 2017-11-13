package com.connect6.ClientCommunication;

import com.connect6.Client;
import com.connect6.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ResponseThread extends Thread {

    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ResponseProcessor responseProcessor;

    public ResponseThread(Socket clientSocket, Client client) {
        this.clientSocket = clientSocket;
        this.responseProcessor = new ResponseProcessor(client);
    }

    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            while (!clientSocket.isClosed()) {
                final Response response = receiveResponse();
                if (response != null) {
                    responseProcessor.process(response);
                }
            }
        } catch (IOException e) {
            System.out.print("Failed to create response thread");
        }
    }

    private Response receiveResponse() {
        Response response = null;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                response = (Response) inputStream.readObject();
            }
        } catch (Exception e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return response;
    }
}