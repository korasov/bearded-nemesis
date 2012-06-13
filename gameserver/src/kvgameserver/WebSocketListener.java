package kvgameserver;

import javax.servlet.http.HttpServletRequest;

import kvgameserver.players.WebSocketPlayer;
import kvgameserver.service.Configuration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

public class WebSocketListener implements Runnable {

	@Override
	public void run() {
		String sPort = Configuration.getInstance().get("websocketport");
		int port = Integer.parseInt(sPort);
		System.out.println("Starting websocket listener at port " + port + ".");
		try {
			Server server = new Server(port);
			ConnMan coman = new ConnMan();
			coman.setHandler(new DefaultHandler());
			server.setHandler(coman);
			server.start();
			System.out.println("Websocket listener started.");
		} catch (Exception e) {}
	}

	private class ConnMan extends WebSocketHandler {

		@Override
		public WebSocket doWebSocketConnect(HttpServletRequest request,
				String protocol) {
			return new WebSocketPlayer();
		}
		
	}
}
