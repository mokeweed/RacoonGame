package racoonman.racoongame.player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import racoonman.racoongame.core.tick.EmptyTickContext;
import racoonman.racoongame.core.tick.TickingObject;

public class PlayerManager implements TickingObject<EmptyTickContext> {
	private Map<String, ClientPlayer> players;
	private Queue<PlayerManager.PlayerEntry> toAdd;
	private Queue<String> toRemove;
	
	public PlayerManager() {
		this.players = new HashMap<>();
		this.toAdd = new LinkedList<>();
		this.toRemove = new LinkedList<>();
	}
	
	@Override
	public void tick(EmptyTickContext tickContext) {
		this.tickAddQueue();
		this.tickRemoveQueue();
	}
	
	private void tickAddQueue() {
		PlayerManager.PlayerEntry next = this.toAdd.poll();
		if(next != null)
			this.players.put(next.getId(), next.getPlayer());
	}
	
	private void tickRemoveQueue() {
		String next = this.toRemove.poll();
		if(next != null)
			this.players.remove(next);
	}

	public void addPlayer(String id, ClientPlayer player) {
		this.toAdd.add(new PlayerManager.PlayerEntry(id, player));
	}
	
	public void removePlayer(String id) {
		this.toRemove.add(id);
	}
	
	public static class PlayerEntry {
		private String id;
		private ClientPlayer player;
		
		public PlayerEntry(String id, ClientPlayer player) {
			this.id = id;
			this.player = player;
		}
		
		public String getId() {
			return this.id;
		}
		
		public ClientPlayer getPlayer() {
			return this.player;
		}
	}
}
