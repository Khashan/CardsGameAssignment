package ca.anderson.game.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface IRequestUtils
{
	static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	static final String ERROR_INVALID_GAME = "Invalid game";
	static final String ERROR_UNSUPPORTED_MEDIA = "Unsupported Media Type";
	static final String ERROR_INVALID_BODY = "Invalid Vody";
	
	static final String SUCCESS_DELETE_GAME = "Game has been deleted";
	
	static final int ERROR_CODE_UNSUPPORTED_MEDIA = 415;
	static final int ERROR_CODE_BAD_REQUEST = 400;
}
