public class Route {
    private String route;
    private String LineCode;
    private String RouteCode;
    private String RouteType;

    public Route(String route,String  RouteCode ,String LineCode,String RouteType){
        this.route=route;
       this.RouteCode=RouteCode;
       this.LineCode=LineCode;
       this.RouteType=RouteType;
    }
    public String getRoute(){
        return route;
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
