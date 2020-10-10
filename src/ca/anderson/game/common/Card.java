package ca.anderson.game.common;

public class Card {
	public enum Suit
	{
		Hearts,
		Spades,
		Clubs,
		Diamonds
	}
	
	private Suit m_Suit;
	private int m_Value;
	
	public Card(Suit suit, int value)
	{
		m_Suit = suit;
		setValue(value);
	}
	
	private void setValue(int number)
	{
		if(number > 10 || number <= 0)
		{
			throw new IllegalArgumentException("Card value has to be between 1 and 10");
		}
			
		m_Value = number;
	}
	
	public int getValue()
	{
		return m_Value;
	}
	
	public Suit getSuit()
	{
		return m_Suit;
	}
	
	@Override
	public String toString()
	{	
		return m_Suit.name() + " " + m_Value;
	}
}

