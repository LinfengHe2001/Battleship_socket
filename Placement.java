package battleship;

public class Placement {

    /**
     * The coordinate of the ship
     */
    public final Coordinate where;

    /**
     * The orientation of the ship
     */
    public final char orientation;

    /**
     * This get the coordinate of the ship
     */
    public Coordinate getWhere() {
        return where;
    }

    /**
     * This get the orientation of the ship
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * Constructs a Placenemt with the specified coordinate
     * and orientation
     *
     * @param w is the coordinate of the newly constructed placement.
     * @param o is the orientation of the newly constructed placement.
     * @throws IllegalArgumentException if the coordinate or orientation is invaild.
     */
    public Placement(Coordinate w, char o) {
        this.where = w;
        char oUp = Character.toUpperCase(o);
        this.orientation = oUp;
    }


    /**
     * Constructs a Placement with the described string
     *
     * @param descr is the string that describes the coordinate and orientation of the Placement.
     * @throws IllegalArgumentException if the coordinate or orientation is invaild.
     */
    public Placement(String descr) {
        if (descr.length() != 3) { // descr should be a string of length 3
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }

        // check the row coordinate in this placement
        char row = descr.charAt(0);
        char rowUp = Character.toUpperCase(row);
        if (rowUp < 'A' || rowUp > 'Z') {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }

        // check the column coordinate in this placement
        char column = descr.charAt(1);
        if (column < '0' || column> '9') {
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }

        String coDescr = descr.substring(0, 2); // coordinate letters at index 0 and 1
        where = new Coordinate(coDescr);
        char orletter = descr.charAt(2); // orientation letter at index 2

        // check the orientation in this placement
        char oUp = Character.toUpperCase(orletter); // turn orientation letter to upper case
        if (oUp != 'V' && oUp != 'H' && oUp!= 'U' && oUp!= 'D' && oUp!= 'L' && oUp!= 'R'){
            throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
        }
        orientation = oUp;
    }

    /**
     * This compares whether two placements are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Placement p = (Placement) o;
            return where.equals(p.where) && orientation == p.orientation;
        }
        return false;
    }

    /**
     * This transforms the placement string to hashcode
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * This transforms the placement to string
     */
    @Override
    public String toString() {
        String oStr = Character.toString(orientation);
        return "Coordinate: " + where.toString() + ", Orientation: " + oStr;
    }
}
