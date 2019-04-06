import java.io.Serializable;
import java.util.Date;

public class BusPosition implements Serializable {
    private String LineCode;
    private int RouteCode;
    private int vehicleId;
    private double latitude;
    private double longitude;
    private Date time;

    BusPosition(String LineCode, int RouteCode, int vehicleId, double latitude, double longitude, Date time){
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
        return  Integer.toString(RouteCode);
    }

    public String getVehicleId(){
        return  Integer.toString(vehicleId);
    }

    public Date getTime(){
        return time;
    }

    double getLatitude(){
        return latitude;
    }

    double getLongitude(){
        return longitude;
    }
}
