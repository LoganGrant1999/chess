package response;

import model.ListGameData;

import java.util.ArrayList;

public record ListGamesResponse(ArrayList<ListGameData> games) {}