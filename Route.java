import java.io.Serializable;

public class Route implements Serializable {
    private String routedescription;
    private String LineCode;
    private String RouteCode;
    private String RouteType;

    public Route(String  RouteCode ,String LineCode,String RouteType,String routedescription){
        this.routedescription = routedescription;
       this.RouteCode = RouteCode;
       this.LineCode = LineCode;
       this.RouteType = RouteType;
    }
    public String getRouteDescription(){
        return routedescription;
    }
    public String getRouteCode(){
        return RouteCode;
    }
    public String getRouteType(){
        return RouteType;
    }
    public String getLineCode(){
        return LineCode;
    }

}
