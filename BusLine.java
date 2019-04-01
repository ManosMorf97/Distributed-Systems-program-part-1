import java.io.Serializable;

class BusLine implements Serializable {

    private int LineCode;
    private String LineId;
    private String description;

    BusLine(int LineCode,String LineId,String description){
        this.LineId = LineId;
        this.LineCode = LineCode;
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    String getLineId() {
        return LineId;
    }

    String getLineCode() {
        return Integer.toString(LineCode);
    }
}

