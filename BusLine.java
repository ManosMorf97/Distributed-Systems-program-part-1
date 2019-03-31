import java.io.Serializable;

class BusLine implements Serializable {

    private String LineCode;
    private String LineId;
    private String routedescription;

    BusLine(String LineCode,String LineId,String routedescription){
        this.LineId=LineId;
        this.LineCode=LineCode;
        this.routedescription=routedescription;
    }
    String getLineId(){
      return  LineId;
    }
    String getRouteDescription(){
        return  routedescription;
    }
    String getLineCode(){
        return LineCode;
    }
}

