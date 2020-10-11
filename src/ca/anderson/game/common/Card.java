package ca.anderson.game.common;

import java.util.AbstractMap;
import java.util.Map;

public class Card {
	public enum Suit
	{
		Hearts,
		Spades,
		Clubs,
		Diamonds
	}
	
	public enum NamedCard
	{
		Number,
		Ace,
		Jack,
		Queen,
		King,
	}
	
	private transient final Map.Entry<Integer, Integer> MIN_MAX_VALUE = new AbstractMap.SimpleEntry<>(1,13);
	
	private Suit m_Suit;
	private int m_Value;
	private String m_FullName;
	private String m_ValueName;
	
	
	public Card(Suit suit, int value)
	{
		m_Suit = suit;
		setValue(value);
		GetCardName();
	}
	
	private void setValue(int number)
	{
		if(number < MIN_MAX_VALUE.getKey() || number > MIN_MAX_VALUE.getValue())
		{
			String errorMessage = String.format(
					"Card value has to be between %d and %d, value entered %d",
					MIN_MAX_VALUE.getKey(),
					MIN_MAX_VALUE.getValue(),
					number);
					
			throw new IllegalArgumentException(errorMessage);
		}
			
		m_Value = number;
	}
	
	private void GetCardName()
	{
		String endName = " of " + m_Suit.name();
		m_ValueName = getCardNameByValue(m_Value);
		m_FullName = m_ValueName + endName;
	}
	
	public int getValue()
	{
		return m_Value;
	}
	
	public Suit getSuit()
	{
		return m_Suit;
	}
	
	public String getName()
	{
		return m_FullName;
	}
	
	public static String getCardNameByValue(int value)
	{
		NamedCard namedCard;
		switch(value)
		{
			case 1:
				namedCard = NamedCard.Ace;
				break;
			case 11:
				namedCard = NamedCard.Jack;
				break;
			case 12:
				namedCard = NamedCard.Queen;
				break;
			case 13:
				namedCard = NamedCard.King;
				break;
				
			default:
				namedCard = NamedCard.Number;
		}
		
		String valueName = "";
		
		if(namedCard == NamedCard.Number)
		{
			valueName = value + "";	
		}
		else
		{
			valueName = namedCard.name();
		}
		
		return valueName;
	}
	
	public String getValueName()
	{
		return m_ValueName;
	}
	
	@Override
	public String toString()
	{	
		return m_Suit.name() + " " + m_Value;
	}
}

