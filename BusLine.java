import java.util.ArrayList;

public class BusLine {

    private Route route;
    private String LineId;
    public BusLine(Route route,String LineId){
        this.route=route;
        this.LineId=LineId;
    }
    public Route getRoute(){
        return route;
    }
    public String getLineId(){
      return  LineId;

   }


}

