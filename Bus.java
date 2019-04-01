import java.io.Serializable;

class Bus implements Serializable {

    private BusLine busLine;
    private BusPosition busPosition;
    private Route route;

    Bus(BusLine busLine,BusPosition busPosition,Route route){
        this.busLine = busLine;
        this.busPosition = busPosition;
        this.route = route;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public BusPosition getBusPosition() {
        return busPosition;
    }

    public Route getRoute() {
        return route;
    }
}
