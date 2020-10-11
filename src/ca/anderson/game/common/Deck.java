package ca.anderson.game.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class Deck extends ArrayList<Card>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2031175609230873243L;

	public Deck()
	{
		Card.Suit[] suits = Card.Suit.values();
		
		for(int value = 1; value <= 13; value++)
		{
			for(Card.Suit suit : suits)
			{
				this.add(new Card(suit, value));
			}
		}
		
		Shuffle();
	}
	
	public void Shuffle()
	{
		Card[] shuffleCards = shuffleCards();
		this.clear();
		this.addAll(Arrays.asList(shuffleCards));
	}
	
	private Card[] shuffleCards()
	{
		Card[] unShuffleCards = this.toArray(new Card[this.size()]);
        int length = unShuffleCards.length;
        
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < length; i++)
        {
            int change = i + random.nextInt(length - i);
            swap(unShuffleCards, i, change);
        }
        
		return unShuffleCards;
	}
	
    private void swap(Card[] unShuffleCards, int aCurrentIndex, int aChangeIndex)
    {
        Card holderData = unShuffleCards[aCurrentIndex];
        unShuffleCards[aCurrentIndex] = unShuffleCards[aChangeIndex];
        unShuffleCards[aChangeIndex] = holderData;
    }
    
	
	public Map<Card.Suit, Integer> getCardsLeftBySuit()
	{
		Map<Card.Suit, Integer> cardsLeft = new HashMap<Card.Suit, Integer>();
		
		for(Card card : this)
		{
			Integer value = cardsLeft.get(card.getSuit());
			value = value == null ? 1 : value + 1;
			
			cardsLeft.put(card.getSuit(), value);
		}
		
		return cardsLeft;
	}
	
	/// Returns < <Suit> <CardValue, Quantity> >
	public Map<Card.Suit, SortedMap<Integer, Integer>> GetCardsByValueLeftSorted()
	{
		Map<Card.Suit, SortedMap<Integer, Integer>> sortedCardsLeft = new HashMap<Card.Suit, SortedMap<Integer, Integer>>();
		
		for(Card.Suit suit : Card.Suit.values())
		{
			SortedMap<Integer, Integer> suitCards = new TreeMap<Integer, Integer>(Collections.reverseOrder());
			
			for(Card card : this)
			{
				Integer value = suitCards.get(card.getValue());
				value = value == null ? 1 : value + 1;
			}
			
			sortedCardsLeft.put(suit, suitCards);
		}
		
		return sortedCardsLeft;
		
	}
}
