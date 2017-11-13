package com.connect6.ClientCommunication;

import com.connect6.Client;
import com.connect6.ReceivedStone;
import com.connect6.Response;
import com.connect6.Stone;

public class ResponseProcessor {
    private Client client;

    ResponseProcessor(Client client) {
        this.client = client;
    }

    void process(Response response) {
        String type = (String) response.getParameter("type");
        switch (type) {
            case "color" :
                client.setColor((int) response.getParameter("color"));
                break;
            case "stones":
                client.setStones((ReceivedStone)response.getParameter("stones"));
                break;
            case "turn":
                client.setIsMyTurn((boolean)response.getParameter("turn"));
        }
    }
}
