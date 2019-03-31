class BusLine {

    private Route route;
    private String LineId;
    BusLine(Route route,String LineId){
        this.route=route;
        this.LineId=LineId;
    }
    Route getRoute(){
        return route;
    }
    String getLineId(){
      return  LineId;

   }
}

