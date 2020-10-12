package ca.anderson.game.server;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.anderson.game.common.DealCard;
import ca.anderson.game.common.Deck;
import ca.anderson.game.common.Game;

/**
 * Servlet implementation class Games
 */


@WebServlet("/games/*")
public class GamesServlet extends HttpServlet implements IRequestUtils
{
	public static class GamePutBody
	{
		public Deck deck;
		public boolean add_player;
		public int remove_player = -1;
		public boolean shuffle;
		public boolean delete;
		public DealCard dealCard;
	}

	private static final long serialVersionUID = 1L;
	private static final String REST_API_URL = "/games/";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		Game game = this.getGame(request, response);

		if (game == null)
		{
			return;
		}

		response.getOutputStream().println(gson.toJson(game));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		Game game = null;
		GamePutBody body = null;

		if (request.getContentLength() == 0)
		{
			game = createGame();
			
			if(game == null)
			{
				response.sendError(ERROR_CODE_BAD_REQUEST, "Couldn't create the game");
				return;
			}
		}
		else if(!IsValidMedia(request, response))
		{
			return;
		}
		else
		{
			game = getGame(request, response);
			body = getBody(request, response);
			
			if(game == null || body == null)
			{
				return;
			}
			
			if(body.shuffle)
			{
				game.ShuffleDeck();
			}
			
			if(body.delete)
			{
				ServerDataStore.getInstance().removeGame(game);
				
				response.getOutputStream().println(SUCCESS_DELETE_GAME);
				return;
			}
			
			if(body.dealCard != null)
			{
				for(int i = 0; i < body.dealCard.quantity; i ++)
				{
					if(!game.dealCards(body.dealCard.toPlayer))
					{
						break;
					}
				}
			}
		}
		
		ServerDataStore.getInstance().updateGame(game);
		response.getOutputStream().println(gson.toJson(game));
	}

	private Game createGame()
	{
		Game newGame = new Game(true);
		ServerDataStore.getInstance().addGame(newGame);
		
		return newGame;
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if(!IsValidMedia(request, response))
		{
			return;
		}
		
		Game selectedGame = getGame(request, response);
		GamePutBody body = getBody(request, response);;
		
		if(selectedGame == null || body == null)
		{
			return;
		}
	
		
		if(body.deck != null)
		{
			selectedGame.AddDeck(body.deck);
		}

		if(body.add_player)
		{			
			selectedGame.AddPlayer();
		}
		
		if(body.remove_player != -1)
		{
			selectedGame.RemovePlayer(body.remove_player);
		}
		

		ServerDataStore.getInstance().updateGame(selectedGame);
		response.getOutputStream().println(gson.toJson(selectedGame));
	}
	
	private boolean IsValidMedia(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		boolean success = true;
		if(!request.getContentType().equalsIgnoreCase("application/json"))
		{
			success = false;
			response.sendError(ERROR_CODE_UNSUPPORTED_MEDIA, ERROR_UNSUPPORTED_MEDIA);
		}
		
		return success;
	}
	
	private GamePutBody getBody(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		GamePutBody body = null;
		
		try
		{
			body = gson.fromJson(request.getReader().lines().collect(Collectors.joining()), GamePutBody.class);
		} catch (Exception e)
		{
			response.sendError(ERROR_CODE_BAD_REQUEST, ERROR_INVALID_BODY);
		}
		
		return body;
	}
	
	private Game getGame(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		Game game = null;
		UUID uuid = getGameUUID(request.getRequestURI(), response);
		
		if(uuid != null)
		{
			game = getGame(uuid, response);
		}
		
		return game;
	}
	
	private Game getGame(UUID uuid, HttpServletResponse response) throws IOException 
	{
		Game game =  ServerDataStore.getInstance().getGame(uuid);
		
		if(game == null)
		{
			response.sendError(ERROR_CODE_BAD_REQUEST, ERROR_INVALID_GAME);
		}
		
		return game;
	}

	private UUID getGameUUID(String requestUrl, HttpServletResponse response) throws IOException 
	{
		int indexGames = requestUrl.indexOf(REST_API_URL);

		if (indexGames != -1)
		{
			String requestedUUID = requestUrl.substring(indexGames + REST_API_URL.length());
			
			if(requestedUUID.endsWith("/"))
			{
				requestedUUID = requestedUUID.substring(0, requestedUUID.length() -1);
			}
			
			try
			{
				return UUID.fromString(requestedUUID);
			} catch (IllegalArgumentException e)
			{
				response.sendError(ERROR_CODE_BAD_REQUEST, ERROR_INVALID_GAME);
			}
		}

		return null;
	}

}
