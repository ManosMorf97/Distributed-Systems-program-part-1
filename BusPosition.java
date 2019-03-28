public class BusPosition {
    private BusLine bus;
    private String vehicleId;
    private double latitude;
    private double longitude;
    private String time;
    public BusPosition(BusLine bus,String vehicleId,double latitude,double longitude,String time){
        this.bus=bus;
        this.latitude=latitude;
        this.longitude=longitude;
        this.time=time;
        this.vehicleId=vehicleId;
    }
    public BusLine getBus(){
        return bus;
    }
    public String getVehicleId(){
        return vehicleId;
    }
    public String getTime(){
        return time;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}
