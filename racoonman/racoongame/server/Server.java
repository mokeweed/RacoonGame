package racoonman.racoongame.server;

import racoonman.nettyimpl2.connection.Connection;
import racoonman.nettyimpl2.connection.ConnectionBuilder;
import racoonman.nettyimpl2.connection.Side;
import racoonman.nettyimpl2.packet.PacketRegistry;
import racoonman.nettyimpl2.packet.PacketSource;

public class Server implements Runnable {
	private static final PacketSource PACKET_SOURCE = new PacketRegistry();
	private Connection server;

	public Server(String ip, int port) throws InterruptedException {
		this.server = new ConnectionBuilder()
			.side(Side.SERVER)
			.address(ip, port)
			.handlerFactory(null)
			.packetSource(PACKET_SOURCE)
			.build();
	}
	
	@Override
	public void run() {
		this.server.open();
		
		while(true) {
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Server(args[0], Integer.parseInt(args[1]));
	}
}
