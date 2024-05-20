package battleship;

import java.util.function.Function;

/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the
 * enemy's board.
 */
public class BoardTextView {
    /**
     * The Board to display
     */
    private final Board<Character> toDisplay;

    /**
     * Constructs a BoardView, given the board it will display.
     *
     * @param toDisplay is the Board to display
     * @throws IllegalArgumentException if the board is larger than 10x26.
     */
    public BoardTextView(Board<Character> toDisplay) {
        this.toDisplay = toDisplay;
        if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
            throw new IllegalArgumentException(
                    "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
        }
    }

    /**
     * Display the board
     */
    protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
        StringBuilder board = new StringBuilder("");
        String header = this.makeHeader();
        String body = this.makeBody(getSquareFn);
        board.append(header); // add the header line
        board.append(body); // add the body part
        board.append(header); // add the header line
        return board.toString();
    }

    /**
     * Display my own board
     */
    public String displayMyOwnBoard() {
        return displayAnyBoard((c)->toDisplay.whatIsAtForSelf(c));
    }

    /**
     * Display the enemy's board
     */
    public String displayEnemyBoard() {
        return displayAnyBoard((c)->toDisplay.whatIsAtForEnemy(c));
    }

    /**
     * This makes the content and Coordinate(row, column)
     *
     * @return the display informantion if there is a ship at the coordinate, ' ' otherwise
     */
    public Character makeDot(int row, int column, Function<Coordinate, Character> getSquareFn) {
        // get the display information at the coordinate
        Character cur = getSquareFn.apply(new Coordinate(row, column));
        if (cur != null) {
            return cur;
        }
        return ' ';
    }

    /**
     * This makes the body part, e.g. A  |  A\n
     *
     * @return the String that is the body part for the given board
     */
    public String makeBody(Function<Coordinate, Character> getSquareFn) {
        int rows = toDisplay.getHeight(); // the rows
        int columns = toDisplay.getWidth(); // the columns
        StringBuilder ans = new StringBuilder("");
        String sep = "|"; // the seperation symbol
        char letter = 'A'; // letter start from 'A'
        for (int row = 0; row < rows; row++) { // go throuth each letter in the range of rows
            StringBuilder curRow = new StringBuilder("");
            curRow.append(letter); // add the letter of this line
            curRow.append(" ");
            for(int column = 0; column < columns - 1; column++) { // go through the current line
                Character cur = makeDot(row, column, getSquareFn); // get the display information at the coordinate
                curRow.append(cur); // add the display information at the coordinate
                curRow.append(sep); // add seperation symbol
            }
            Character cur = makeDot(row, columns - 1, getSquareFn);
            curRow.append(cur); // add the display information at the last coordinate of current row
            curRow.append(" ");
            curRow.append(letter); // add the letter of this line
            curRow.append("\n");
            ans.append(curRow); // add the current line to the body part
            letter += 1; // move to the next letter
        }
        return ans.toString();
    }

    /**
     * This makes the header line, e.g. 0|1|2|3|4\n
     *
     * @return the String that is the header line for the given board
     */
    public String makeHeader() {
        StringBuilder ans = new StringBuilder("  "); // README shows two spaces at
        String sep=""; //start with nothing to separate, then switch to | to separate
        for (int column = 0; column < toDisplay.getWidth(); column++) {
            ans.append(sep);
            ans.append(column);
            sep = "|";
        }
        ans.append("\n");
        return ans.toString();
    }

    /**
     * This makes a string of continuous space
     *
     * @return the String of continuous
     */
    public String makeSpace(int num) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < num; i++) {
            space.append(" ");
        }
        return space.toString();
    }

    /**
     * This makes the display view of two boards together
     *
     * @return the String that is the header line for the given board
     */
    public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {

        StringBuilder view = new StringBuilder(); // string to represent the  whole view of two boards

        int width = toDisplay.getWidth(); // width of the board
        int height = toDisplay.getHeight(); // height of the board
        int headerStart = 5; // the first header start at column 5
        int myHeaderLength = myHeader.length(); // length of the first header
        int headerDistance = 2 * width + 22; // start position of the second header
        int headerSpace = headerDistance - myHeaderLength - headerStart; // number of space between two headers
        String spaceBeforeHeader = makeSpace(headerStart); // space before the first header
        String spaceBetweenHeader = makeSpace(headerSpace); // space between two headers

        // add the header part
        view.append(spaceBeforeHeader);
        view.append(myHeader);
        view.append(spaceBetweenHeader);
        view.append(enemyHeader);
        view.append("\n");

        String[] myBoard = this.displayMyOwnBoard().split("\n"); // split my board by "\n"
        String[] enemyBoard = enemyView.displayEnemyBoard().split("\n"); // split the enemy's board by "\n"

        int bodyDistance = 2 * width + 19; // start position of the second board
        int bodyOccupied = 2 * width + 3; // width of the first board
        int bodySpace = bodyDistance - bodyOccupied; // number of space between two boards
        String spaceBetweenBoard = makeSpace(bodySpace); // space between two boards
        String spaceBetweenFirstLine = makeSpace(bodySpace + 2); // space between the number line of two boards

        // add the number line in the body part
        view.append(myBoard[0]);
        view.append(spaceBetweenFirstLine);
        view.append(enemyBoard[0]);
        view.append("\n");

        // add the board in the body part
        for(int i = 1; i < height + 1; i++) {
            view.append(myBoard[i]);
            view.append(spaceBetweenBoard);
            view.append(enemyBoard[i]);
            view.append("\n");
        }

        // add the number line in the body part
        view.append(myBoard[0]);
        view.append(spaceBetweenFirstLine);
        view.append(enemyBoard[0]);
        view.append("\n");

        return view.toString();
    }

}
