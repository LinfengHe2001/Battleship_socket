package battleship;

import java.util.ArrayList;

public class EasyCompletionRules<T> implements CompletionRules<T> {

    /**
     * The player's board, from which we could justify
     * whether the palyer has lost or not
     */
    private final Board<T> myBoard;

    /**
     * This initializes the palyer's board
     */
    public EasyCompletionRules(Board<T> myBoard) {
        this.myBoard = myBoard;
    }

    /**
     * This check whether the player has lost the game
     */
    public boolean checkCompletion() {
        ArrayList<Ship<T>> myShips = myBoard.getShips();
        for (Ship<T> ship : myShips) {
            if(!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }
}
