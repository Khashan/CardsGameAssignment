package ca.anderson.game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.anderson.game.common.DealCard;
import ca.anderson.game.common.Deck;
import ca.anderson.game.common.Game;
import ca.anderson.game.server.GamesServlet.GamePutBody;

@WebServlet("/test")
public class Test extends HttpServlet implements IRequestUtils
{
	private static final long serialVersionUID = 1L;

	private final String ULR_START = "http://localhost:8080/CardsGame/";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		// DECK
		sendMessage(response, "Creating Deck");
		String deckJson = creatingDeck();

		//sendMessage(response, deckJson);
		Deck deck = gson.fromJson(deckJson, Deck.class);
		
		// Game
		sendMessage(response, "Creating Game");
		String gameJson = createGame();
		Game game = gson.fromJson(gameJson, Game.class);
		sendMessage(response, gameJson);

		if (game == null)
		{
			sendMessage(response, "Error while creating a game..");
			return;
		}

		// Add 3 Players
		sendMessage(response, "Adding 3 players");
		GamePutBody putBody = new GamePutBody();
		putBody.add_player = true;

		putRequestOnGame(game, putBody);
		putRequestOnGame(game, putBody);
		game = gson.fromJson(putRequestOnGame(game, putBody), Game.class);

		sendMessage(response, gson.toJson(game));

		// Removing one player
		sendMessage(response, "Removing one player");

		putBody.add_player = false;
		putBody.remove_player = 1;

		game = gson.fromJson(putRequestOnGame(game, putBody), Game.class);

		sendMessage(response, gson.toJson(game));

		// Add Deck
		sendMessage(response, "Adding deck to the game (overriding the default)");
		putBody.remove_player = -1;
		putBody.deck = deck;

		game = gson.fromJson(putRequestOnGame(game, putBody), Game.class);

		sendMessage(response, gson.toJson(game));

		// Shuffle
		sendMessage(response, "Shuffling game deck");
		putBody.deck = null;
		putBody.shuffle = true;

		game = gson.fromJson(postRequestOnGame(game, putBody), Game.class);
		sendMessage(response, gson.toJson(game));

		// Give 25 cards to player id: 0
		sendMessage(response, "Giving cards to player 1 - 25 cards");
		putBody.shuffle = false;
		putBody.dealCard = new DealCard(0, 25);

		game = gson.fromJson(postRequestOnGame(game, putBody), Game.class);
		sendMessage(response, gson.toJson(game));

		//Game Info
		sendMessage(response, "Cards left in deck by SUIT: " + game.getCardsLeftBySuitString());
		sendMessage(response, "Cards left in deck by SUIT + VALUE: " + game.getCardsLeftByValueSortedString());
		sendMessage(response, "Players: \n" + game.getPlayersString());

		// Give 40 (27) cards to player id 2
		sendMessage(response, "Giving cards to player id: 2 - 40 cards (Will receive 27 cards in reality)");
		putBody.dealCard = new DealCard(2, 40);

		game = gson.fromJson(postRequestOnGame(game, putBody), Game.class);
		sendMessage(response, gson.toJson(game));

		//Game Info
		sendMessage(response, "Players: \n" + game.getPlayersString());

		sendMessage(response, "Delete game");

		putBody.dealCard = null;
		putBody.delete = true;
		postRequestOnGame(game, putBody);

		sendMessage(response, "Trying to get the game (Validating existance)");

		URL url = new URL(ULR_START + "games/" + game.getGameId());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		sendMessage(response, getResponseMessage(con));
		
		response.getOutputStream().println("Test - Succeeded");
	}

	private void sendMessage(HttpServletResponse response, String msg) throws IOException
	{
		System.out.println(msg);
		//response.getOutputStream().println(msg + "\n");
	}

	private String creatingDeck() throws IOException
	{
		URL url = new URL(ULR_START + "deck");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		return getResponseMessage(con);
	}

	private String createGame() throws IOException
	{
		URL url = new URL(ULR_START + "games");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setFixedLengthStreamingMode(0);
		con.setDoOutput(true);

		return getResponseMessage(con);
	}

	private String postRequestOnGame(Game game, GamePutBody putBody) throws IOException
	{
		String bodyJson = gson.toJson(putBody);
		URL url = new URL(ULR_START + "games/" + game.getGameId().toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		con.connect();

		byte[] out = bodyJson.getBytes(StandardCharsets.UTF_8);
		try (OutputStream os = con.getOutputStream())
		{
			os.write(out);
		}

		return getResponseMessage(con);
	}

	private String putRequestOnGame(Game game, GamePutBody putBody) throws IOException
	{
		String bodyJson = gson.toJson(putBody);

		URL url = new URL(ULR_START + "games/" + game.getGameId().toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		con.connect();

		byte[] out = bodyJson.getBytes(StandardCharsets.UTF_8);
		try (OutputStream os = con.getOutputStream())
		{
			os.write(out);
		}

		return getResponseMessage(con);
	}

	private String getResponseMessage(HttpURLConnection con) throws IOException
	{
		
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
		{ 
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            
            return sb.toString();
		} else
		{
			return ("GET request not worked");
		}
	}
}
