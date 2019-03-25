import java.util.ArrayList;

public class BusLine {

    private Route route;
    private String LineCode;
    private String LineId;
    public BusLine(Route route,String LineCode,String LineId){
        this.route=route;
        this.LineCode=LineCode;
        this.LineId=LineId;
    }
    public Route getRoute(){
        return route;
    }
    public String getLineId(){
      return  LineId;

   }
   public String getLineCode(){
        return LineCode;
   }
}

