package battleship;

public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

    /**
     * The next placement rule checker
     */
    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * This check the placement of ship does not collide with other ships
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        Iterable<Coordinate> shipSet = theShip.getCoordinates();
        for (Coordinate c: shipSet) {
            if (theBoard.whatIsAtForSelf(c) != null) {
                return "That placement is invalid: the ship overlaps another ship.";
            }
        }
        return null;
    }

}
