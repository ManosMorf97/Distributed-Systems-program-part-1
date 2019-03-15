class BusAndLocation{
	private String busLine;
	private String linenumber;
	private String routeCode;
	private String vehicleId;
	private String buslineId;
	private double latitude;
	private double longitude;
	
	
	
	void SetBusLine(String busLine){
		this.busLine=busLine;
	}
	String GetBusLine(){
		return busLine;
	}
	void SetLineNumber(String linenumber){
		this.linenumber= linenumber;
	}
	String GetLineNumber(){
		return linenumber;
	}
	void SetRouteCode(String routeCode){
		this.routeCode=routeCode;
	}
	String GetRouteCode(){
		return routeCode;
	}
	void SetVehicleId(String vehicleId){
		this.vehicleId=vehicleId;
	}
	String GetVehicleId(){
		return vehicleId;
	}
	void SetBusLineId(String buslineId){
		this.buslineId=buslineId;
	}
	String GetBusLineId(){
		return buslineId;
	}
	void SetLatitude(double latitude){
		this.latitude=latitude;
	}
	double GetLatitude(){
		return latitude;
	}
	void SetLongitude(double longitude){
		this.longitude=longitude;
	}
	double GetLongitude(){
		return longitude;
	}
}