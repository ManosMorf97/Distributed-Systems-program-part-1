import java.io.Serializable;

class BusLine implements Serializable {

    private int LineCode;
    private int LineId;
    private String description;

    BusLine(int LineCode,int LineId,String description){
        this.LineId = LineId;
        this.LineCode = LineCode;
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    String getLineId() {
        return Integer.toString(LineId);
    }

    String getLineCode() {
        return Integer.toString(LineCode);
    }
}

