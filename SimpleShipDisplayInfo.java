package battleship;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {

    private T myData;
    private T onHit;

    /**
     * This initializes the value of myData and onHit
     */
    public  SimpleShipDisplayInfo(T myData, T onHit){
        this.myData = myData;
        this.onHit = onHit;
    }

    /**
     * This specifies which display information to return
     */
    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if (hit) {
            return onHit;
        }
        else {
            return myData;
        }
    }
}
