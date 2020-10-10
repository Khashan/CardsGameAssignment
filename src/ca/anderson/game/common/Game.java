package ca.anderson.game.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {
	private UUID m_GameId;
	private Map<Integer, Player> m_Players = new HashMap<Integer, Player>();
	private Deck m_GameDeck = null;
	
	
	public Game() 
	{
		this(null);
	}
	
	public Game(Deck deck)
	{
		m_GameId = UUID.randomUUID();
		m_GameDeck = deck;
	}
	
	public boolean AddDeck(Deck deck)
	{
		boolean added = false;
		
		if(m_GameDeck == null)
		{
			m_GameDeck = deck;
			added = true;
		}
		
		return added;
	}
	
	public void AddPlayer()
	{	
		Player newPlayer = new Player(GetNewPlayerId());
		m_Players.put(newPlayer.getPlayerId(), newPlayer);
	}
	
	private int GetNewPlayerId()
	{
		int id = -1;
		
		for(int playerId : m_Players.keySet())
		{
			id = playerId;
		}
		
		return id + 1;
	}
	
	public void RemovePlayer(Player player)
	{
		RemovePlayer(player.getPlayerId());
	}
	
	public void RemovePlayer(int playerId)
	{
		m_Players.remove(playerId);
	}
	
	public void dealCards(int playerId)
	{
		Player player = m_Players.get(playerId);

		if(m_GameDeck.size() > 0 && player != null)
		{
			ShuffleDeck();
			
			player.addCard(m_GameDeck.get(0));
			m_GameDeck.remove(0);
		}		
	}
	
	public void ShuffleDeck()
	{
		if(m_GameDeck != null)
		{
			m_GameDeck.Shuffle();
		}
	}
	
	public UUID getGameId()
	{
		return m_GameId;
	}
}