package edu.kit.informatik;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum Commands {
    /**
     * the place command to place two stones
     *
     */
    PLACE("place (\\d+);(\\d+);(\\d+);(\\d+)") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            int row1 = Integer.parseInt(matcher.group(1));
            int row2 = Integer.parseInt(matcher.group(3));
            int col1 = Integer.parseInt(matcher.group(2));
            int col2 = Integer.parseInt(matcher.group(4));

            if (mode.equals("standard")) {

                boolean won = game.playTurnStandard(row1, col1, row2, col2, player);
                game.checkDraw(game.getTurn(), won);
                if (won) {
                    Terminal.printLine(player.getName() + " " + "wins");
                    game.setGameOver(true);
                } else {
                    Terminal.printLine("OK");
                }
            } else if (mode.equals("torus")) {
                boolean won = game.playTurnTorus(row1, col1, row2, col2, player);
                game.checkDraw(game.getTurn(), won);
                if (won) {
                    Terminal.printLine(player.getName() + " " + "wins");
                    game.setGameOver(true);
                } else {
                    Terminal.printLine("OK");
                }
            }
        }
    },

    /**
     * the print command to print the game board
     *
     */
    PRINT("print") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            game.printGrid();
        }
    },

    /**
     * the rowprint command to print a specific row from the board
     *
     */
    PRINT_ROW("rowprint (\\d+)") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            int rowNumber = Integer.parseInt(matcher.group(1));
            game.printRow(rowNumber);
        }
    },

    /**
     * the colprint command to print a specific column from the game board
     *
     */
    PRINT_COLUMN("colprint (\\d+)") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            int colNumber = Integer.parseInt(matcher.group(1));
            game.printColumn(colNumber);
        }
    },

    /**
     * the state command to print a slot out of the game board
     *
     */
    STATE("state (\\d+);(\\d+)") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            int rowIndex = Integer.parseInt(matcher.group(1));
            int colIndex = Integer.parseInt(matcher.group(2));
            game.printSlot(rowIndex, colIndex);
        }
    },
    /**
     * the reset command to reset the game
     *
     */
    RESET("reset") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            int gameSize = game.getBoard().length;
            game = new Game(gameSize);
            game.setGameOver(false);
            Terminal.printLine("OK");
        }
    },

    /**
     * The quit command to exit the program.
     */
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException {
            isRunning = false;
        }
    };

    private static boolean isRunning = true;
    private Pattern pattern;

    /**
     * Constructs a new command.
     *
     * @param pattern The regex pattern to use for command validation and processing.
     *            
     */
    Commands(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Checks an input against all available commands and calls the command if one
     * is found.
     *
     * @param input The user input.
     *            
     * @param delTracker The instance of the tracker to run the command on.
     *            
     * @return The command that got executed.
     * @throws InputException if no matching command is found. Contains an error message.
     *             
     */

    /**
     * Executes a command.
     *
     * @param matcher The regex matcher that contains the groups of input of the
     *            command.
     *            
     * @param delTracker The instance of the DEL tracker.
     *            
     * @throws InputException if the command contains syntactical or symantical errors.
     *             
     */
    public abstract void execute(MatchResult matcher, Game game, Player player, String mode) throws InputException;

    /**
     * Checks if the program is still running or was exited.
     *
     * @return true if the program is still running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    public static Commands executeMatching(String input, Game game, Player player, String mode) throws InputException {
        for (Commands command : Commands.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, game, player, mode);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }
}
