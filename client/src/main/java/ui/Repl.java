package ui;

import java.util.Objects;
import java.util.Scanner;
public class Repl {

    private final PreLoginClient prelogin;

    private PostLoginClient postLogin;

    private GameplayClient gameplay;


    private State state = State.PRELOGIN;

    private final String serverUrl;

    public Repl(String serverUrl) {

        this.serverUrl = serverUrl;

        prelogin = new PreLoginClient(serverUrl);

    }

    public void run() {

        System.out.println(EscapeSequences.WHITE_KNIGHT + "Welcome to Chess. Sign in to start.");

        System.out.print(prelogin.help());

        Scanner scanner = new Scanner(System.in);

        var result = "";

        while (!result.equals("quit")) {

            printPrompt();

            String line = scanner.nextLine();

            try {

                if (state == State.PRELOGIN) {

                    prelogin.setAuthTokenNull();

                    result = prelogin.eval(line);

                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);

                    if (prelogin.getAuthToken() != null){

                        setState(State.POSTLOGIN);

                        postLogin = new PostLoginClient(serverUrl, prelogin.getAuthToken());
                    }

                } else if (state == State.POSTLOGIN) {

                    result = postLogin.eval(line);

                    System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA + result);

                    if (Objects.equals(result, "Successfully Logged out!")) {

                        setState(State.PRELOGIN);
                    }

                    if (Objects.equals(result, "You Successfully Joined the Game" + "\n")
                            || Objects.equals(result, ("You Are Observing the Game!" + "\n"))){

                        setState(State.GAMEPLAY);

                        gameplay = new GameplayClient(serverUrl, postLogin.getAuthToken(),
                                postLogin.getGameID(), postLogin.getPlayerColor());

                        String board = gameplay.drawBoard();

                        System.out.println(board);

                        continue;
                    }
                }

            } catch (Throwable e)  {

                var msg = e.toString();

                System.out.println(msg);

            }

            System.out.println();
        }
    }

    public void setState(State newState) {

        this.state = newState;
    }

    private void printPrompt() {

        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR+ ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
