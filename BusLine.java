import java.util.ArrayList;

public class BusLine {

    private String LineCode;
    private String LineId;
    private String routedescription;
    public BusLine(String LineCode,String LineId,String routedescription){
        this.LineId=LineId;
        this.LineCode=LineCode;
        this.routedescription=routedescription;
    }
    public String getLineId(){
      return  LineId;
   }
   public String getRouteDescription(){
        return  routedescription;
   }
   public String getLineCode(){
        return LineCode;
   }


}

