package ca.anderson.game.common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.UUID;

public class Game {
	private UUID m_GameId;
	private Map<Integer, Player> m_Players = new HashMap<Integer, Player>();
	private Deck m_GameDeck;
	
	private transient boolean m_HasDefaultDeck = true;
	
	
	public Game()
	{
		this(false);
	}
	
	public Game(boolean newGame)
	{
		if(newGame)
		{
			m_GameId = UUID.randomUUID();
			m_GameDeck = new Deck();
		}
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
	
	public boolean dealCards(int playerId)
	{
		boolean dealt = false;
		Player player = m_Players.get(playerId);

		if(m_GameDeck.size() > 0 && player != null)
		{
			ShuffleDeck();
			
			player.addCard(m_GameDeck.get(0));
			m_GameDeck.remove(0);
			dealt = true;
		}		
		
		return dealt;
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
	
	public Map<Player, Integer> getPlayers()
	{
		Map<Player, Integer> playersList = new HashMap<>();
		LinkedHashMap<Player, Integer> sortedPlayersList = new LinkedHashMap<>();
		
		for(Player player: m_Players.values())
		{
			playersList.put(player, player.handValue());
		}
		
		// Source: https://howtodoinjava.com/java/sort/java-sort-map-by-values/
		playersList.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
	    .forEachOrdered(x -> sortedPlayersList.put(x.getKey(), x.getValue()));
		

		return sortedPlayersList;
	}
	
	public String getPlayersString()
	{
		Map<Player, Integer> playersList = getPlayers();
		
		StringBuilder builder = new StringBuilder();
		
		for(Entry<Player, Integer> entry : playersList.entrySet())
		{
			builder.append(entry.getKey().getPlayerId() + " > " + entry.getValue() + "\n");
		}
		
		return builder.toString();
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
			message.append(entry.getValue() + " " + entry.getKey() + " | ");
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
				
				message.append(cardValueName + " of " + entry.getKey().toString() + " x" + sortedEntry.getValue() + " | ");
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
