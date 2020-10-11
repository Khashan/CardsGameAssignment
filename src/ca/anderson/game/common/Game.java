package ca.anderson.game.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class Game {
	private UUID m_GameId;
	private Map<Integer, Player> m_Players = new HashMap<Integer, Player>();
	private Deck m_GameDeck = new Deck();
	
	private transient boolean m_HasDefaultDeck = true;
	
	public Game()
	{
		m_GameId = UUID.randomUUID();
	}
	
	public boolean AddDeck(Deck deck)
	{
		boolean added = false;
		
		if(m_HasDefaultDeck)
		{
			m_HasDefaultDeck = false;
			m_GameDeck = deck;
			added = true;
		}
		
		return added;
	}
	
	public boolean AddPlayer()
	{	
		boolean added = false;
		
		Player newPlayer = new Player(GetNewPlayerId());
		
		if(!m_Players.containsKey(newPlayer.getPlayerId()))
		{	
			added = true;
			m_Players.put(newPlayer.getPlayerId(), newPlayer);
		}
		
		return added;
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
	
	public Player getPlayer(int playerId)
	{
		return m_Players.getOrDefault(playerId, null);
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
	
	public SortedMap<Player, Integer> getPlayers()
	{
		SortedMap<Player, Integer> sorted = new TreeMap<Player, Integer>(Collections.reverseOrder());
		
		for(Player player: m_Players.values())
		{
			sorted.put(player, player.handValue());
		}
		
		return sorted;
	}

	public Map<Card.Suit, Integer> getCardsLeftBySuit()
	{
		return m_GameDeck.getCardsLeftBySuit();
	}
	
	public String getCardsLeftBySuitString()
	{
		Map<Card.Suit, Integer> left = getCardsLeftBySuit();
		
		StringBuilder message = new StringBuilder();
		
		for(Map.Entry<Card.Suit, Integer> entry :  left.entrySet())
		{
			message.append(entry.getValue() + " " + entry.getKey());
			message.append("");
		}
		
		return message.toString();
	}
	
	public String getCardsLeftByValueSortedString()
	{
		Map<Card.Suit, SortedMap<Integer, Integer>> sorted = m_GameDeck.GetCardsByValueLeftSorted();
		
		StringBuilder message = new StringBuilder();
		
		for(Map.Entry<Card.Suit, SortedMap<Integer, Integer>> entry : sorted.entrySet())
		{
			for(Map.Entry<Integer, Integer> sortedEntry : entry.getValue().entrySet())
			{
				String cardValueName = Card.getCardNameByValue(sortedEntry.getKey());
				
				message.append(cardValueName + " of " + entry.getKey().toString() + " x" + sortedEntry.getValue());
				message.append("");
			}			
		}
		
		return message.toString();
	}
	
	public Map<Card.Suit, SortedMap<Integer, Integer>> getCardsLeftByValueSorted()
	{
		return m_GameDeck.GetCardsByValueLeftSorted();
	}
	
}
