package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.function.Function;

public class ComputerPlayer extends TextPlayer {
    /**
     * The row of the coordinate to fire at
     */
    private int row;

    /**
     * The column of the coordinate to fire at
     */
    private int column;

    /**
     * The number of ships on the board
     */
    private int shipNum;

    /**
     * The list of positions of ships on the board
     */
    private ArrayList<Placement> shipPositions;

    /**
     * Constructs a computer of the battleship game
     */
    public ComputerPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, PrintStream out, V2ShipFactory factory) {
        super(name, theBoard, inputSource, out, factory);
        this.row = 0;
        this.column = 0;
        this.shipNum = 0;
        this.shipPositions = new ArrayList<Placement>();
    }

    private void fillShipPositions() {
        shipPositions.add(new Placement("a0v"));
        shipPositions.add(new Placement("o2v"));
        shipPositions.add(new Placement("a1h"));
        shipPositions.add(new Placement("a6h"));
        shipPositions.add(new Placement("d7v"));
        shipPositions.add(new Placement("b1u"));
        shipPositions.add(new Placement("J5U"));
        shipPositions.add(new Placement("M5U"));
        shipPositions.add(new Placement("c0u"));
        shipPositions.add(new Placement("O7U"));
    }
    @Override
    public void doPlacementPhase() throws IOException {
        this.fillShipPositions();
        for (String s : this.shipsToPlace) { // call doOnePlacement iteratively from the list of ships for creation
            doOnePlacement(s, shipCreationFns.get(s));
        }
        out.println("Player " + name + " has placed all ships on the board!");
    }

    /**
     * This add one ship to the borad
     */
    @Override
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        Placement p = shipPositions.get(shipNum);
        shipNum += 1;
        Ship<Character> s = createFn.apply(p);
        theBoard.tryAddShip(s);
    }

    /**
     * This perform a one-turn fire action of a player
     */
    @Override
    public void doFire(Board<Character> enemyBoard) throws IOException {
        int width = enemyBoard.getWidth();
        int height = enemyBoard.getHeight();
        Coordinate c = new Coordinate(row, column);
        Ship<Character> target = enemyBoard.fireAt(c); // find what is located att the coordinate

        // fire at coordinates on the board in order (from top-left to bottom-right)
        if (column == width - 1) {
            column = 0;
            if (row == height - 1) {
                row = 0;
            }
            else {
                row += 1;
            }
        }
        else {
            column += 1;
        }

        if (target == null) { // a miss
            String missResult = "Player " + name + " missed at " + c + "!";
            out.println(missResult);
        }
        else { // a hit
            String hitResult = "Player " + name + " hit your " + target.getName() + " at " + c + "!";
            out.println(hitResult);
        }
    }

    /**
     * This perform a one-turn action of a player
     */
    public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
        doFire(enemyBoard);
    }

    /**
     * This perform a one-turn scan action of a player
     */
    @Override
    public void doScan(Board<Character> enemyBoard) throws IOException {
        if (scanCounts > 0) {
            int width = enemyBoard.getWidth();
            int height = enemyBoard.getHeight();
            Integer submairneNum = 0; // number of submarines in range
            Integer destroyerNum = 0; // number of destroyers in range
            Integer battleshipNum = 0; // number of battleships in range
            Integer carrierNum = 0; // number of carriers in rage
            // use a list to store all the vaild coordinates to take sonar scan
            ArrayList<Coordinate> scanCoordinates= new ArrayList<Coordinate>();
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

            // count ships in range
            for (Coordinate scanCoordinate : scanCoordinates) {
                Ship<Character> ship = enemyBoard.getShipAt(scanCoordinate);
                if (ship != null) {
                    if (ship.getName().equals("Carrier")) {
                        carrierNum += 1;
                    }
                    if (ship.getName().equals("Destroyer")) {
                        destroyerNum += 1;
                    }
                    if (ship.getName().equals("Submarine")) {
                        submairneNum += 1;
                    }
                    if (ship.getName().equals("Battleship")) {
                        battleshipNum += 1;
                    }
                }
            }
            scanCounts -= 1;
            String prompt = "Player " + name + " used a special action!";
            out.println(prompt);
        }
        return;
    }

    /**
     * This perform a one-turn move action of a player
     */
    @Override
    public void doMove() throws IOException {
        Ship<Character> toMove = null;
        Placement p = null;
        // choose the ship and placement in each turn
        if (moveCounts == 3) {
            toMove = theBoard.getShipAt(new Coordinate("a0"));
            p = new Placement("I1V");
        }
        if (moveCounts == 2) {
            toMove = theBoard.getShipAt(new Coordinate("a1"));
            p = new Placement("i2V");
        }
        if (moveCounts == 1) {
            toMove = theBoard.getShipAt(new Coordinate("a6"));
            p = new Placement("o8v");
        }
        moveCounts -= 1 ;
        Ship<Character> newShip = shipFactory.makeSubmarine(p);
        theBoard.moveShip(toMove, newShip, p); // move the ship
        out.println("Move Successfully!");
        String prompt = "Player " + name + " used a special action!";
        out.println(prompt);
    }

}
