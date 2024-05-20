package battleship;

public class Coordinate {
    /**
     * The row of the coordinate
     */
    private final int row;

    /**
     * The column of the coordinate
     */
    private final int column;

    /**
     * This get the row of the coordinate
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * This get the column of the coordinate
     *
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * Constructs a Coordinate with the specified row
     * and column
     *
     * @param r is the row of the newly constructed coordinate.
     * @param c is the column of the newly constructed coordinate.
     * @throws IllegalArgumentException if the row or column are less than zero.
     */
    public Coordinate(int r, int c) {
        this.row = r;
        this.column = c;
    }

    /**
     * Constructs a Coordinate with the described string
     *
     * @param descr is the string that describes the row and column of the Coordinate.
     * @throws IllegalArgumentException if the row or column are out of valid range.
     */
    public Coordinate(String descr) {
        if (descr.length() != 2) { // descr should be a string of length 2
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        char rowletter = descr.charAt(0); // row letter at index 0
        char rowletterUp = Character.toUpperCase(rowletter);  // convert letters from lower case to upper case
        if (rowletterUp < 'A' || rowletterUp > 'Z') { // the range of row letter should be from 'A' to 'Z'
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        int row = rowletterUp - 'A';
        char columnletter = descr.charAt(1); // column letter at index 1
        String c = descr.substring(1);
        if (columnletter < '0' || columnletter > '9') {  // the column should be a single digit
            throw new IllegalArgumentException("That coordinate is invalid: it does not have the correct format.");
        }
        int column = Integer.parseInt(c);
        this.row = row;
        this.column = column;
    }

    /**
     * This compares whether two coordinates are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.row && column == c.column;
        }
        return false;
    }

    /**
     * This transforms the coordinate string to hashcode
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * This transforms the coordinate to string
     */
    @Override
    public String toString() {
        return "("+row+", " + column+")";
    }
}
