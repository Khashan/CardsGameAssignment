package ca.anderson.game.common;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private int m_Id;
	
	@SuppressWarnings("unused")
	private int m_DebugTotalCardsInHand = 0;
	
	private List<Card> m_CardsInHand = new ArrayList<Card>();
	private int m_HandValue = 0;
	
	public Player(int id)
	{
		m_Id = id;
	}
	
	public void addCard(Card card)
	{
		m_CardsInHand.add(card);
		m_HandValue += card.getValue();
		
		m_DebugTotalCardsInHand++;
	}
	
	public int getPlayerId()
	{
		return m_Id;
	}
	
	public int handValue()
	{	
		return m_HandValue;
	}
	
	public List<Card> getCards()
	{
		return m_CardsInHand;
	}
}
