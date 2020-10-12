package ca.anderson.game.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.anderson.game.common.Deck;

/**
 * Servlet implementation class DeckServlet
 */
@WebServlet("/deck")
public class DeckServlet extends HttpServlet implements IRequestUtils{
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Deck deck = new Deck();
		deck.initDeck();
		response.getOutputStream().println(gson.toJson(deck));
	}
}
