package battleship;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicShip<T> implements Ship<T> {

    /**
     * The coordinates that made up the ship together
     */
    protected HashMap<Coordinate, Boolean> myPieces;

    /**
     * The display information of my ships
     */
    protected ShipDisplayInfo<T> myDisplayInfo;

    /**
     * The display information of the enemy's ships
     */
    protected ShipDisplayInfo<T> enemyDisplayInfo;

    /**
     * This make a basic ship from an Iterable
     * @param where is an interable data structure
     * @param myDisplayInfo is the display information of the ship
     */
    public BasicShip(Iterable<Coordinate> where,  ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        this.myPieces = new HashMap<Coordinate, Boolean>();
        for (Coordinate c: where) {
            this.myPieces.put(c, false);
        }
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
    }

    /**
     * This check whether there is a ship at the coordinate
     */
    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        return myPieces.containsKey(where);
    }

    /**
     * This check whether the coordinate is part of the ship
     */
    protected void checkCoordinateInThisShip(Coordinate c) {
        if (myPieces.get(c)==null){
            throw new IllegalArgumentException("Coordinate "+ c.toString() +" is not part of this ship");
        }
    }

    /**
     * This check whether all the coordinates of the ship are hit,
     * if so, the ship is regarded as sunk
     */
    @Override
    public boolean isSunk() {
        for(Map.Entry<Coordinate, Boolean> entry: myPieces.entrySet()) { // the ship is sunk if all of its coordinates are hit
            if(entry.getValue() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * This record that the coordinate of the ship is hit
     * @param where is a coordinate that is part of the ship
     */
    @Override
    public void recordHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        myPieces.put(where, true);
    }

    /**
     * This check where the coordinate of the ship has been hit or not
     * @param where is a coordinate that is part of the ship
     */
    @Override
    public boolean wasHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        return myPieces.get(where);
    }

    /**
     * This get the display information of a ship on a coordinate
     */
    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip) {
        checkCoordinateInThisShip(where);
        if (myShip){
            return myDisplayInfo.getInfo(where, myPieces.get(where));
        }
        else {
            return enemyDisplayInfo.getInfo(where, myPieces.get(where));
        }
    }

    /**
     * Get all of the Coordinates that this Ship occupies.
     * @return An Iterable with the coordinates that this Ship occupies
     */
    @Override
    public Iterable<Coordinate> getCoordinates(){
        return myPieces.keySet();
    }

}
