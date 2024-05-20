package battleship;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BattleShipBoard<T> implements Board<T> {

    /**
     * The width of the newly constructed board
     */
    private final int width;

    /**
     * The height of the newly constructed board
     */
    private final int height;

    /**
     * The arraylist of ships to our BattleShipBoard
     */
    private final ArrayList<Ship<T>> myShips;


    private final PlacementRuleChecker<T> placementChecker;

    private HashSet<Coordinate> enemyMisses;

    /**
     * The information represents a miss
     */
    private final T missInfo;

    /**
     * The coordinates of ships that have been hit on the board
     */
    private HashMap<Coordinate,Ship<T>> boardHits;

    /**
     * This get the width of the newly constructed board
     *
     * @return the Board width
     */
    public int getWidth() {
        return width;
    }

    /**
     * This get the height of the newly constructed board
     *
     * @return the Board height
     */
    public int getHeight() {
        return height;
    }

    /**
     * This get the list of ships on the board
     *
     * @return the list of ships on the Board
     */
    public ArrayList<Ship<T>> getShips() {
        return myShips;
    }

    /**
     * Constructs a BattleShipBoard with the specified width,
     * height and palcement checker
     * @param w is the width of the newly constructed board.
     * @param h is the height of the newly constructed board.
     * @param placementChecker is the placement validity checker
     * @throws IllegalArgumentException if the width or height are less than or equal to zero.
     */
    public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo) {
        if (w <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
        }
        if (h <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
        }
        this.width = w;
        this.height = h;
        this.myShips = new  ArrayList<Ship<T>>();
        this.placementChecker = placementChecker;
        this.enemyMisses =  new HashSet<Coordinate>();
        this.missInfo = missInfo;
        this.boardHits = new HashMap<Coordinate, Ship<T>>();
    }

    /**
     * Constructs a BattleShipBoard with the specified width
     * and height
     * @param w is the width of the newly constructed board.
     * @param h is the height of the newly constructed board.
     */
    public BattleShipBoard(int w, int h, T missInfo) {
        this(w, h, new InBoundsRuleChecker<T>(new NoCollisionRuleChecker<T>(null)), missInfo);
    }

    /**
     * This add ships to the arraylist of ships to our BattleShipBoard
     *
     * @return true
     */
    public String tryAddShip(Ship<T> toAdd) {
        String s = placementChecker.checkPlacement(toAdd, this);
        if (s != null) {
            return s;
        }
        myShips.add(toAdd);
        return null;
    }

    /**
     * This takes a Coordinate, and sees which (if any)
     * Ship occupies that coordinate in our own board
     *
     * @return displayInfo if a ship is at the Coordinate, null if not
     */
    public T whatIsAtForSelf(Coordinate where) {
        return whatIsAt(where, true);
    }

    /**
     * This takes a Coordinate, and sees which (if any)
     * Ship occupies that coordinate in the enemy's board
     *
     * @return displayInfo if a ship is at the Coordinate, null if not
     */
    public T whatIsAtForEnemy(Coordinate where) {
        return whatIsAt(where, false);
    }

    /**
     * This takes a Coordinate, and sees which (if any)
     * Ship occupies that coordinate
     *
     * @return displayInfo if a ship is at the Coordinate, null if not
     */
    protected T whatIsAt(Coordinate where, boolean isSelf) {
        if (!isSelf && enemyMisses.contains(where)) { // if missed record exists, return it
            return missInfo;
        }
        if (!isSelf && boardHits.containsKey(where)) { // if hit record exists. return it
            Ship<T> ship = boardHits.get(where);
            return ship.getDisplayInfoAt(where, false);
        }
        for (Ship<T> s: myShips) {
            if (s.occupiesCoordinates(where)) { // coordinate on the ship
                if (!isSelf && s.wasHitAt(where)) {
                    return null;
                }
                return s.getDisplayInfoAt(where, isSelf);
            }
        }
        return null; // no ship at the coordinate
    }

    /**
     * This takes a Coordinate, and check whether a ship
     * would be hit at that coordinate or not
     *
     * @return ship if it is hit at the coordinate, null if not
     */
    public Ship<T> fireAt(Coordinate c) {
        for (Ship<T> ship : myShips) { // check all ships on the board
            if (ship.occupiesCoordinates(c)) { // a ship is hit
                boardHits.put(c, ship);
                enemyMisses.remove(c); // remove past miss if exists
                ship.recordHitAt(c); // record the hit
                return ship;
            }
        }
        enemyMisses.add(c); // no hit, record the miss
        boardHits.remove(c); // remove past hit if exists
        return null;
    }

    /**
     * This move ship on the board to a new location
     */
    public void moveShip(Ship<T> toMove, Ship<T> newShip, Placement p) {
        myShips.remove(toMove);
        String s = placementChecker.checkPlacement(newShip, this);
        myShips.add(toMove);
        if (s != null) { // invalid placement
            throw new IllegalArgumentException(s);
        }
        ArrayList<Coordinate> hits = toMove.moveHitCoordinate(p); // get all the hit coordinates on the ship to move
        for (Coordinate hit : hits) { // record all the hit
            newShip.recordHitAt(hit);
        }
        myShips.add(newShip); // add the new ship to the board
        myShips.remove(toMove); // remove the ship to move from the board
    }

    /**
     * This takes a Coordinate, and get the ship at there
     *
     * @return ship at the coordinate, null if no ship is found there
     */
    public Ship<T> getShipAt(Coordinate where) {
        for (Ship<T> s: myShips) {
            if (s.occupiesCoordinates(where)) { // get the ship at the coordinate
                return s;
            }
        }
        return null; // no ship found
    }

}
