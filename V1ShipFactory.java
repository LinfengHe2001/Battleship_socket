package battleship;

/**
 * This class represents an Abstract Factory pattern for Ship creation in Version1
 */
public class V1ShipFactory implements AbstractShipFactory<Character> {

    /**
     * Make a submarine.
     *
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the submarine.
     */
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    /**
     * Make a battleship.
     *
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the battleship.
     */
    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createShip(where, 1, 4, 'b', "Battleship");
    }

    /**
     * Make a carrier.
     *
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the carrier.
     */
    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createShip(where, 1, 6, 'c', "Carrier");
    }

    /**
     * Make a destroyer.
     *
     * @param where specifies the location and orientation of the ship to make
     * @return the Ship created for the destroyer.
     */
    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }

    /**
     * This make a rectangle ship, which assumed in vertical orientation and
     * need to reverse for horizontal orientation
     *
     * @param where specifies the location and orientation of the ship to make
     * @param name specifies the type of the ship
     * @return the Ship created.
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
        char orientation = where.getOrientation();
        Coordinate c = where.getWhere();
        if (orientation == 'V'){
            RectangleShip<Character> myShip = new RectangleShip<>(name, c, w, h, letter, '*');
            return myShip;
        }
        else if (orientation == 'H') {
            RectangleShip<Character> myShip = new RectangleShip<>(name, c, h, w, letter, '*');
            return myShip;
        }
        else {
            throw new IllegalArgumentException("Invaild orientation: " + orientation);
        }
    }


}
