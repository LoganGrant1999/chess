package model;

    /*Makes model record class of Game Data excluding ChessGame.
    This is used to make it possible to only return relevant game info when
    ListGames endpoint is used
     */

public record ListGameData (int gameID, String whiteUsername, String blackUsername, String gameName){}
