package ui;

import server.ServerFacade;

public class PreLoginClient {

    private final ServerFacade server;

    private final String serverUrl;

    public PreLoginClient(String serverUrl) {

        server = new ServerFacade(serverUrl);

        this.serverUrl = serverUrl;
    }


}
