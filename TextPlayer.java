package battleship;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class TextPlayer {
    /**
     * The name of the player
     */
    public final String name;

    /**
     * The reamaining count of move actions
     */
    public Integer moveCounts;

    /**
     * The remaining count of the scan actions
     */
    public Integer scanCounts;

    /**
     * The battleship board
     */
    public final Board<Character> theBoard;

    /**
     * The textual dispaly information of the board
     */
    public final BoardTextView view;

    /**
     * The input from player
     */
    public final BufferedReader inputReader;

    /**
     * The output to player
     */
    public final PrintStream out;

    /**
     * The factory of the ship
     */
    public final AbstractShipFactory<Character> shipFactory;

    /**
     * The list of ship names that we want to work from
     */
    public final ArrayList<String> shipsToPlace;

    /**
     * The map from ship name to the lambda to create it
     */
    public final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

    /**
     * The status to show whether the player has lost the game
     */
    public EasyCompletionRules<Character> myStatus;

    /**
     * This get the name of the player
     *
     * @return the name
     */
    protected String getName() {
        return this.name;
    }

    /**
     * This get the board of the coordinate
     *
     * @return the board
     */
    protected Board<Character> getBoard() {
        return this.theBoard;
    }

    /**
     * Constructs a textplayer of the battleship game
     */
    public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, V2ShipFactory factory) {
        this.name = name;
        this.moveCounts = 3;
        this.scanCounts = 3;
        this.theBoard = theBoard;
        this.view = new BoardTextView(theBoard);
        this.inputReader = inputSource;
        this.out = out;
        this.shipFactory = factory;
        this.shipsToPlace = new ArrayList<String>();
        this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
        this.myStatus = new EasyCompletionRules<>(theBoard);
        setupShipCreationList();
        setupShipCreationMap();
    }

    /**
     * This set up the map of different kinds of ships for creation
     */
    protected void setupShipCreationMap() {
        shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
//        shipCreationFns.put("BattleShip", (p) -> shipFactory.makeBattleship(p));
//        shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
//        shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    }

    /**
     * This set up the list of different kinds of ships for creation
     */
    protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
//        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
//        shipsToPlace.addAll(Collections.nCopies(3, "BattleShip"));
//        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    /**
     * This read input from player and get the placement information
     */
    public Placement readPlacement(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
        return new Placement(s);
    }

    /**
     * This add one ship to the borad and then display the borad
     */
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement p = null;
        try { p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?"); // read a Placement
        }
        catch (IllegalArgumentException e) { // invalid placement with incorrect input information
            out.println(e.getMessage()); // print out the message to user
            doOnePlacement(shipName, createFn); // read input again
            return;
        }
        Ship<Character> s = null;
        try {
            s = createFn.apply(p); // create a ship based on the location in that Placement
        }
        catch(IllegalArgumentException e) {
            out.println(e.getMessage()); // print out the message to user
            doOnePlacement(shipName, createFn); // read input again
            return;
        }
        String res = theBoard.tryAddShip(s); // add that ship to the board
        if (res != null) { // invalid placement that violates placement rules. fail to add ship
            out.println(res); // print out the message to user
            doOnePlacement(shipName, createFn); // read input again
            return;
        }
        out.print(view.displayMyOwnBoard()); // print out the board
    }

    /**
     * This generate the make placement phase for players during the battleship game
     */
    public void doPlacementPhase() throws IOException {
        out.print(view.displayMyOwnBoard()); // the initial empty board
        // the instruction
        out.print("Player " + name + ":"
                + " you are going to place the following ships (which are all rectangular)."
                + " For each ship, type the coordinate of the upper left side of the ship,"
                + " followed by either H (for horizontal) or V (for vertical)."
                + "  For example M4H would place a ship horizontally starting at M4 and going to the right."
                + "  You have\n\n"
                + "2 \"Submarines\" ships that are 1x2\n"
                + "3 \"Destroyers\" that are 1x3\n"
                + "3 \"Battleships\" that are 1x4\n"
                + "2 \"Carriers\" that are 1x6\n");
        for (String s : shipsToPlace) { // call doOnePlacement iteratively from the list of ships for creation
            doOnePlacement(s, shipCreationFns.get(s));
        }
    }

    /**
     * This check whether the player has lost the game
     *
     * @return true if the player has lost the game, false if not
     */
    public boolean checkLose() {
        return myStatus.checkCompletion();
    }

    private Coordinate coordinateCheck(String s, Board<Character> board) {
        int width = board.getWidth();
        int height = board.getHeight();

        validateInputFormat(s);
        char rowLetter = extractRowLetter(s);
        int row = validateAndConvertRow(rowLetter, height);
        int column = validateAndConvertColumn(s.substring(1), width);

        return new Coordinate(row, column);
    }

    private void validateInputFormat(String s) {
        if (s.length() != 2) {
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
    }

    private char extractRowLetter(String s) {
        char rowLetter = Character.toUpperCase(s.charAt(0));
        if (rowLetter < 'A' || rowLetter > 'Z') {
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        return rowLetter;
    }

    private int validateAndConvertRow(char rowLetter, int height) {
        int row = rowLetter - 'A';
        if (row < 0 || row > height - 1) {
            throw new IllegalArgumentException("That coordinate is invalid: it is out of the board.");
        }
        return row;
    }

    private int validateAndConvertColumn(String columnString, int width) {
        char columnLetter = columnString.charAt(0);
        if (columnLetter < '0' || columnLetter > '9') {
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }

        int column = Integer.parseInt(columnString);
        if (column > width - 1 || column < 0) {
            throw new IllegalArgumentException("That coordinate is invalid: it is out of the board.");
        }
        return column;
    }


    private Coordinate getFireCoordinate(Board<Character> enemyBoard) throws IOException {
        String firePrompt = "Please choose a coordinate to fire at:";
        Coordinate c = null;
        // try until the input is a vaild coordinate to fire at
        boolean status = false;
        while(!status) {
            try {
                out.println(firePrompt);
                String s = inputReader.readLine();
                c = coordinateCheck(s, enemyBoard); // check the validity of coordinate
                status = true; // valid input
            }
            catch (IllegalArgumentException e) {
                out.println(e.getMessage()); // print the exception message out
                continue; // try again
            }
        }
        return c;
    }

    /**
     * This perform a one-turn fire action of a player
     */
    public void doFire(Board<Character> enemyBoard) throws IOException {
        Coordinate c = getFireCoordinate(enemyBoard); // get a valid coordinate to fire at

        Ship<Character> target = enemyBoard.fireAt(c); // find what is located att the coordinate
        if (target == null) { // a miss
            String missResult = "You missed!";
            out.println(missResult);
        }
        else { // a hit
            String hitResult = "You hit a " + target.getName() + "!";
            out.println(hitResult);
        }
    }

    /**
     * This perform a one-turn action of a player
     */
    public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
        String myTurn = "Player " + name + "'s turn:";
        out.println(myTurn); // print my turn prompt
        String myHeader = "Your ocean";
        String enemyHeader = "Player " + enemyName + "'s ocean";
        out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, myHeader, enemyHeader)); // print the display information of two boards
        doChooseAction(enemyBoard);
    }

    private Coordinate getScanCoordinate(Board<Character> enemyBoard) throws IOException {
        Coordinate c = null;
        String prompt = "Please choose the center coordinate for sonar scan:";
        // try until the input is a vaild coordinate for sonar scan
        boolean flag = true;
        while(flag) {
            try {
                out.println(prompt);
                String s = inputReader.readLine();
                c = coordinateCheck(s, enemyBoard); // check the validity of coordinate
                flag = false; // valid input
            }
            catch (IllegalArgumentException e) {
                out.println(e.getMessage()); // print the exception message out
                continue; // try again
            }
        }
        return c;
    }

    public void doScan(Board<Character> enemyBoard) throws IOException {
        Coordinate c = getScanCoordinate(enemyBoard);
        ArrayList<Coordinate> scanCoordinates = generateScanCoordinates(c, enemyBoard);
        int[] shipCounts = countShipsInScanArea(enemyBoard, scanCoordinates);
        String res = generateScanResult(shipCounts);
        out.print(res);
    }

    private ArrayList<Coordinate> generateScanCoordinates(Coordinate center, Board<Character> enemyBoard) {
        int height = enemyBoard.getHeight();
        int width = enemyBoard.getWidth();
        int column = center.getColumn();
        int row = center.getRow();

        ArrayList<Coordinate> scanCoordinates = new ArrayList<>();
        for (int j = column - 3; j <= column + 3; j++) {
            int i = row;
            if (0 <= i && i <= height - 1 && 0 <= j && j <= width - 1) {
                scanCoordinates.add(new Coordinate(i, j));
            }
        }
        if (0 <= row + 3 && row + 3 <= height - 1 && 0 <= column && column <= width - 1) {
            scanCoordinates.add(new Coordinate(row + 3, column));
        }
        if (0 <= row - 3 && row - 3 <= height - 1 && 0 <= column && column <= width - 1) {
            scanCoordinates.add(new Coordinate(row - 3, column));
        }
        for (int j = column - 1; j <= column + 1; j++) {
            int i = row + 2;
            if (0 <= i && i <= height - 1 && 0 <= j && j <= width - 1) {
                scanCoordinates.add(new Coordinate(i, j));
            }
        }
        for (int j = column - 1; j <= column + 1; j++) {
            int i = row - 2;
            if (0 <= i && i <= height - 1 && 0 <= j && j <= width - 1) {
                scanCoordinates.add(new Coordinate(i, j));
            }
        }
        for (int j = column - 2; j <= column + 2; j++) {
            int i = row + 1;
            if (0 <= i && i <= height - 1 && 0 <= j && j <= width - 1) {
                scanCoordinates.add(new Coordinate(i, j));
            }
        }
        for (int j = column - 2; j <= column + 2; j++) {
            int i = row - 1;
            if (0 <= i && i <= height - 1 && 0 <= j && j <= width - 1) {
                scanCoordinates.add(new Coordinate(i, j));
            }
        }

        return scanCoordinates;
    }

    private int[] countShipsInScanArea(Board<Character> enemyBoard, ArrayList<Coordinate> scanCoordinates) {
        int submairneNum = 0;
        int destroyerNum = 0;
        int battleshipNum = 0;
        int carrierNum = 0;

        for (Coordinate scanCoordinate : scanCoordinates) {
            Ship<Character> ship = enemyBoard.getShipAt(scanCoordinate);
            if (ship != null) {
                switch (ship.getName()) {
                    case "Carrier":
                        carrierNum += 1;
                        break;
                    case "Destroyer":
                        destroyerNum += 1;
                        break;
                    case "Submarine":
                        submairneNum += 1;
                        break;
                    case "Battleship":
                        battleshipNum += 1;
                        break;
                }
            }
        }

        return new int[]{submairneNum, destroyerNum, battleshipNum, carrierNum};
    }

    private String generateScanResult(int[] shipCounts) {
        // Generate the scan result message
        return "Submarines occupy " + shipCounts[0] + " squares\n" +
                "Destroyers occupy " + shipCounts[1] + " squares\n" +
                "Battleships occupy " + shipCounts[2] + " squares\n" +
                "Carriers occupy " + shipCounts[3] + " squares\n";
    }

    void getShipByName(Ship<Character>[] newShip, String name, Placement p){
        if (name.equals("Carrier")) {
            newShip[0] = shipFactory.makeCarrier(p);
        }
        if (name.equals("Submarine")) {
            newShip[0] = shipFactory.makeSubmarine(p);
        }
        if (name.equals("Battleship")) {
            newShip[0] = shipFactory.makeBattleship(p);
        }
        if (name.equals("Destroyer")) {
            newShip[0] = shipFactory.makeDestroyer(p);
        }
    }

    public void doMove() throws IOException {
        Ship<Character> toMove = getShipToMove(); // get the ship to move
        String name = toMove.getName();
        Placement p = getMovePlacement(name); // get the placement to move to
        Ship<Character>[] newShip = new Ship[1]; // 使用数组包装

        getShipByName(newShip, name, p);

        theBoard.moveShip(toMove, newShip[0], p); // move the ship
        out.println("Move Successfully!");
    }

    private Ship<Character> getShipToMove() throws IOException {
        String prompt = "Please choose a ship to move:";
        out.println(prompt);
        Coordinate c = null;
        String s = inputReader.readLine();
        c = coordinateCheck(s, theBoard); // check the validity of coordinate
        Ship<Character> ship = theBoard.getShipAt(c);
        if (ship == null) {
            throw new IllegalArgumentException("That coordinate is invalid: no ship is found.");
        }
        return ship;
    }

    private Placement getMovePlacement(String name) throws IOException {
        String prompt = "Please enter the location to move your ship to:";
        out.println(prompt);
        Placement p = null;
        String s = inputReader.readLine();
        p = new Placement(s);
        return p;
    }

    public Character choiceCheck(String s, Board<Character> enemyBoard) throws IOException {
        // check the input string format
        if (s.length() != 1) {
            throw new IllegalArgumentException("That choice is invalid: it does not have the correct format.");
        }
        String sUp = s.toUpperCase();
        Character choice = sUp.charAt(0);

        if (choice != 'F' && choice != 'M' && choice != 'S') { // invalid choice letter
            throw new IllegalArgumentException("That choice is invalid: " + choice);
        }
        if (choice == 'M' && moveCounts <= 0) { // no more move action available
            throw new IllegalArgumentException("Invalid Choice: No More Move Actions.");
        }
        if (choice == 'S' && scanCounts <= 0) { // no more scan action available
            throw new IllegalArgumentException("Invalid Choice: No More Scan Actions.");
        }

        return choice;
    }

    /**
     * This perform a choose action process of a player
     */
    public void doChooseAction(Board<Character> enemyBoard) throws IOException {
        String prompt = "Possible actions for Player " + name + ":\n\n\n" +
                "F Fire at a square\n" +
                "M Move a ship to another square (" + moveCounts +" remaining)\n" +
                "S Sonar scan (" + scanCounts + " remaining)\n\n\n" +
                "Player " + name + ", what would you like to do?";
        Character choice = null;
        boolean flag = true;
        while(flag) {
            try {
                out.println(prompt);
                String s = inputReader.readLine();
                choice = choiceCheck(s, enemyBoard);
                flag = false;
            }
            catch (IllegalArgumentException e) {
                out.println(e.getMessage());
                continue;
            }
        }
        chooseOneAction(enemyBoard, choice);
    }

    public void chooseOneAction(Board<Character> enemyBoard, Character choice) throws IOException {
        if (choice == 'M') {
            try {
                doMove();
                moveCounts -= 1;
            }
            catch (IllegalArgumentException e) {
                out.println(e.getMessage());
                doChooseAction(enemyBoard);
                return;
            }
        }
        if (choice == 'F') {
            doFire(enemyBoard);
        }
        if (choice == 'S') {
            scanCounts -= 1;
            doScan(enemyBoard);
        }
    }

}

