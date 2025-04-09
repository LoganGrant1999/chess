package ui;

import chess.ChessPiece;
import exceptions.NetworkException;

import java.util.Objects;
import java.util.Scanner;
public class Repl {

    private final PreLoginClient preLogin;

    private PostLoginClient postLogin;

    private GameplayClient gamePlay;

    private State state = State.PRELOGIN;

    private boolean waiting = false;

    private final String serverUrl;

    public Repl(String serverUrl) {

        this.serverUrl = serverUrl;

        preLogin = new PreLoginClient(serverUrl);

    }

    public void run() throws NetworkException {

        System.out.println(EscapeSequences.WHITE_KNIGHT + "Welcome to Chess. Sign in to start.");

        System.out.print(preLogin.help());

        Scanner scanner = new Scanner(System.in);

        var result = "";

        while (!result.equals("quit")) {

            printPrompt();

            String line = scanner.nextLine();

            try {

                if (state == State.PRELOGIN) {

                    preLogin.setAuthTokenNull();

                    result = preLogin.eval(line);

                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                    if (preLogin.getAuthToken() != null) {

                        setState(State.POSTLOGIN);

                        postLogin = new PostLoginClient(serverUrl, preLogin.getAuthToken());
                    }

                } else if (state == State.POSTLOGIN) {

                    postLogin.setPlayerColorNull();

                    result = postLogin.eval(line);

                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                    if (Objects.equals(result, "Successfully Logged out!")) {

                        setState(State.PRELOGIN);
                    }

                    if (Objects.equals(result, "You Successfully Joined the Game!" + "\n")
                            || Objects.equals(result, ("You Are Observing the Game!" + "\n"))) {


                        gamePlay = new GameplayClient(serverUrl, postLogin.getAuthToken(),

                                postLogin.getGameID(), postLogin.getPlayerColor());

                        setState(State.GAMEPLAY);

                        printPrompt();
                    }

                } else if (state == State.GAMEPLAY) {

                    if (waiting) {

                        if (line.equalsIgnoreCase("y")) {

                            result = gamePlay.resignFinal();

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                            waiting = false;

                        } else if (line.equalsIgnoreCase("n")) {

                            result = "Cancelled Resignation!";

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                            waiting = false;

                        } else {

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "Please Enter Y or N");

                            result = gamePlay.resign();

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                        }

                    } else {

                        result = gamePlay.eval(line);

                        if (Objects.equals(result, "Are you sure you would like to resign? [Y/N]")) {

                            waiting = true;

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);

                        } else {

                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
                        }

                        if (Objects.equals(result, "You successfully left the game!")) {

                            setState(State.POSTLOGIN);
                        }
                    }
                }


            } catch (Exception e) {

                throw new NetworkException(500, e.getMessage());
            }
        }
    }

    public void setState(State newState) {

        this.state = newState;
    }

    private void printPrompt() {

        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
