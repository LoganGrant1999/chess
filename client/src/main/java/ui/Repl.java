package ui;

import java.util.Scanner;
public class Repl {

    private final PreLoginClient prelogin;

    public Repl(String serverUrl) {

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

                result = prelogin.eval(line);

                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + result);

            } catch (Throwable e)  {

                var msg = e.toString();

                System.out.println(msg);

            }

            System.out.println();
        }
    }


    private void printPrompt() {

        System.out.print("\n" + EscapeSequences.RESET_TEXT_COLOR+ ">>> " + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
    }
}
