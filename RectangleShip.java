package battleship;

import java.util.ArrayList;
import java.util.HashSet;

public class RectangleShip<T> extends BasicShip<T> {

    /**
     * This is the name of the ship
     */
    private final String name;

    /**
     * This is the top-left corner of the ship
     */
    private final Coordinate upperLeft;

    /**
     * This get the name of this Ship, such as "submarine".
     *
     * @return the name of this ship
     */
    public String getName() {
        return name;
    }

    /**
     * This make the coordinate set,
     * and pass them up to the BasicShip constructor
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> shipDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(upperLeft, width, height), shipDisplayInfo, enemyDisplayInfo);
        this.name = name;
        this.upperLeft = upperLeft;
    }

    /**
     * This refactor the BasicShip and RectangleShip
     *
     z*/
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
        this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     * This refactor the BasicShip and RectangleShip
     *
     */
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit);
    }

    /**
     * This generate the set of coordinates for a rectangle ship
     *
     * @param upperLeft is the upper left coordinate of the rectangle ship
     */
    public static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height) {
        HashSet<Coordinate> coords = new HashSet<Coordinate>();
        int upperLeftRow = upperLeft.getRow();
        int upperLeftColumn = upperLeft.getColumn();
        for (int i = 0; i < width; i++) { // go through each row
            for(int j = 0; j < height; j++) { // go through each cloumn
                int row = upperLeftRow + j;
                int column = upperLeftColumn + i;
                Coordinate coord = new Coordinate(row, column); // construct the current coordinate
                coords.add(coord); // set as part of the rectangle
            }
        }
        return coords;
    }

    /**
     * Move all hit coordinates on this ship to a new same-typed ship at the given placement
     *
     * @param p is the placement of the new ship
     * @return a list of coordinates on the new ship that are corresponding to be hit
     */
    @Override
    public ArrayList<Coordinate> moveHitCoordinate(Placement p) {
        ArrayList<Coordinate> hits = new ArrayList<Coordinate>(); // a list to store corresponding hit coordinates on the new ship
        Iterable<Coordinate> hitCoordinates = this.getCoordinates(); // coordinates of this ship
        int oldRow = upperLeft.getRow();
        int oldColumn = upperLeft.getColumn();
        Coordinate newCoordinate = p.getWhere();
        int newRow = newCoordinate.getRow();
        int newColumn = newCoordinate.getColumn();
        char newOrientation = p.getOrientation();
        for (Coordinate c : hitCoordinates) { // check every coordinate on this ship
            if (this.wasHitAt(c)) { // find hit coordinate
                int row = c.getRow();
                int column = c.getColumn();
                int distance = (row - oldRow) + (column - oldColumn);
                if (newOrientation == 'V') { // get the corresponding hit coordinate when the orientation of new ship is 'V'
                    hits.add(new Coordinate(newRow + distance, newColumn)); // add hit coordinate
                }
                if (newOrientation == 'H') { // get the corresponding hit coordinate when the orientation of new ship is 'H'
                    hits.add(new Coordinate(newRow, newColumn + distance)); // add hit coordinate
                }
            }
        }
        return hits; // return a list of hit coordinates on the new ship
    }
}
