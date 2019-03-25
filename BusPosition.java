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
    }
    public BusLine getBus(){
        return bus;
    }
    public String getVehicleId(){
        return vehicleId;
    }
    private String getTime(){
        return time;
    }
    private double getLatitude(){
        return latitude;
    }
    private double getLongitude(){
        return longitude;
    }
}
