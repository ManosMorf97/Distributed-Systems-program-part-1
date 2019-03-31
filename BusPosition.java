public class BusPosition {
    private BusLine bus;
    private String vehicleId;
    private double latitude;
    private double longitude;
    private String time;
    BusPosition(BusLine bus, String vehicleId, double latitude, double longitude, String time){
        this.bus=bus;
        this.latitude=latitude;
        this.longitude=longitude;
        this.time=time;
        this.vehicleId=vehicleId;
    }
    BusLine getBus(){
        return bus;
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
