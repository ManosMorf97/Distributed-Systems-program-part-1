public class BusPosition {
    private String LineCode;
    private String RouteCode;
    private String vehicleId;
    private double latitude;
    private double longitude;
    private String time;

    BusPosition(String LineCode, String RouteCode, String vehicleId, double latitude, double longitude, String time){
        this.LineCode = LineCode;
        this.RouteCode = RouteCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.vehicleId = vehicleId;
    }

    String getLineCode(){
        return  LineCode;
    }

    public String getRouteCode(){
        return  RouteCode;
    }

    public String getVehicleId(){
        return vehicleId;
    }

    public String getTime(){
        return time;
    }

    double getLatitude(){
        return latitude;
    }

    double getLongitude(){
        return longitude;
    }
}
