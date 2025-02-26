package model;

import chess.ChessGame;

//Makes model record class for Game Data

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){}
