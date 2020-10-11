package ca.anderson.game.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ca.anderson.game.common.Game;

public class ServerDataStore {

	private static ServerDataStore instance = new ServerDataStore();
	private Map<UUID, Game> m_ActiveGames = new HashMap<>();
	
	public static ServerDataStore getInstance()
	{
		return instance;
	}
	
	
	public Game getGame(UUID gameId)
	{
		return m_ActiveGames.getOrDefault(gameId, null);
	}
	
	public void removeGame(Game game)
	{
		removeGame(game.getGameId());
	}
	
	public void removeGame(UUID gameId)
	{
		m_ActiveGames.remove(gameId);	}
	
	public void addGame(Game game)
	{
		if(!m_ActiveGames.containsKey(game.getGameId()))
		{		
			m_ActiveGames.put(game.getGameId(), game);
		}
	}
	
}
