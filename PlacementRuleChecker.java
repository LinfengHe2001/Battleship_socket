package battleship;


public abstract class PlacementRuleChecker<T> {

    /**
     * The next placement rule
     */
    private final PlacementRuleChecker<T> next;

    /**
     * This takes the next placement rule
     */
    public PlacementRuleChecker(PlacementRuleChecker<T> next) {
        this.next = next;
    }

    /**
     * This specify how to check my own rule
     */
    protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

    /**
     * This handle chaining placement rules together
     */
    public String checkPlacement (Ship<T> theShip, Board<T> theBoard) {
        //if we fail our own rule: stop the placement is not legal
        if (checkMyRule(theShip, theBoard) != null) {
            return checkMyRule(theShip, theBoard);
        }
        //other wise, ask the rest of the chain.
        if (next != null) {
            return next.checkPlacement(theShip, theBoard);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }

}

