package battleship;

import java.util.ArrayList;

public interface Board<T> {
    /**
     * This get the width of the newly constructed board
     */
    public int getWidth();

    /**
     * This get the height of the newly constructed board
     */
    public int getHeight();

    /**
     * This get the list of ships on the board
     */
    public ArrayList<Ship<T>> getShips();

    /**
     * This add ships to the arraylist of ships to our BattleShipBoard
     */
    public String tryAddShip(Ship<T> toAdd);

    /**
     * This check which (if any) Ship occupies the coordinate in our own board
     */
    public T whatIsAtForSelf(Coordinate where);

    /**
     * This check which (if any) Ship occupies the coordinate in the enemy's board
     */
    public T whatIsAtForEnemy(Coordinate where);

    /**
     * This check whether a ship is hit at the given coordinate or not
     */
    public Ship<T> fireAt(Coordinate c);

    /**
     * This takes a Coordinate, and get the ship at there
     */
    public Ship<T> getShipAt(Coordinate c);

    /**
     * This move ship on the board to a new location
     */
    public void moveShip(Ship<T> toMove, Ship<T> newShip, Placement p);

}
