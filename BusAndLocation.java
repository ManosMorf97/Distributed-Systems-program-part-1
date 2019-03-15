public class BusAndLocation{
	private String busLine;
	private String linenumber;
	private String routeCode;
	private String vehicleId;
	private String buslineId;
	private double latitude;
	private double longitude;
	
	
	
	public void SetBusLine(String busLine){
		this.busLine=busLine;
	}
	public String GetBusLine(){
		return busLine;
	}
	public void SetLineNumber(String linenumber){
		this.linenumber= linenumber;
	}
	public String GetLineNumber(){
		return linenumber;
	}
	public void SetRouteCode(String routeCode){
		this.routeCode=routeCode;
	}
	public String GetRouteCode(){
		return routeCode;
	}
	public void SetVehicleId(String vehicleId){
		this.vehicleId=vehicleId;
	}
	public String GetVehicleId(){
		return vehicleId;
	}
	public void SetBusLineId(String buslineId){
		this.buslineId=buslineId;
	}
	public String GetBusLineId(){
		return buslineId;
	}
	public void SetLatitude(double latitude){
		this.latitude=latitude;
	}
	public double GetLatitude(){
		return latitude;
	}
	public void SetLongitude(double longitude){
		this.longitude=longitude;
	}
	public double GetLongitude(){
		return longitude;
	}
}