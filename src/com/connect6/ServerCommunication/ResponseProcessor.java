package com.connect6.ServerCommunication;

import com.connect6.*;

public class ResponseProcessor {
    Server server;

    ResponseProcessor(Server server) {
        this.server = server;
    }

    void process(Request request) {
        String type = (String) request.getParameter("type");

        switch (type) {
            case "stone":
                ReceivedStone stone = (ReceivedStone) request.getParameter("stone");
                server.setStone(stone.x, stone.y, stone.color);
        }

    }
}
