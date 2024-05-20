package battleship;

public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

    /**
     * The next placement rule checker
     */
    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * This check the ship is placed in bound of the board
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        Iterable<Coordinate> shipSet = theShip.getCoordinates();
        for (Coordinate c: shipSet) {
            if (c.getRow() < 0) {
                return "That placement is invalid: the ship goes off the top of the board.";
            }
            if (c.getRow() >= theBoard.getHeight()) {
                return "That placement is invalid: the ship goes off the bottom of the board.";
            }
            if (c.getColumn() < 0) {
                return "That placement is invalid: the ship goes off the left of the board.";
            }
            if (c.getColumn() >= theBoard.getWidth()) {
                return "That placement is invalid: the ship goes off the right of the board.";
            }
        }
        return null;
    }
}