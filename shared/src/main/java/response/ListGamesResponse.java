package response;

import model.ListGameData;

import java.util.ArrayList;

//Record class used to create ListGamesResponse Object
public record ListGamesResponse(ArrayList<ListGameData> games) {}