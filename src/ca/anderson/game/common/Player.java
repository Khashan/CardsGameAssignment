package ca.anderson.game.common;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private int m_Id;
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
	}
	
	public int getPlayerId()
	{
		return m_Id;
	}
	
	public int handValue()
	{	
		return m_HandValue;
	}
}
