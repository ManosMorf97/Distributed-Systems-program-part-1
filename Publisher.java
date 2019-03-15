import java.util.ArrayList;
import java.util.HashMap;

class Publisher{

    private static HashMap<String, BusAndLocation> Keys;
    private ArrayList<Publisher> registeredPublishers;


    private Publisher(ArrayList<Broker> brokers) {

    }

    private HashMap<String,BusAndLocation> getKeys(){
        return Keys;
    }

}