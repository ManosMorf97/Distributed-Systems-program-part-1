import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Consumer;

//lol
public class Broker implements Runnable{
	private int hashnumber;
	private String hashstring;
	private boolean recieveorsend = false;
	//we want the data from publisher
	private static ArrayList<BusAndLocation> DataFromPublisher;
	private static ArrayList<BusAndLocation> DataResponsible;
	private ArrayList<Consumer> consumers;
	private ArrayList<Publisher> registeredPublishers;
	private Iterator registeredPublishersIterator = null;
	private static HashMap<String, BusAndLocation> Keys;
	private static int PublisherID = -1;//UNDONE
	private static int ConsumerID = -1;//UNDONE

	private Broker(ArrayList<Consumer> consumers, ArrayList<Publisher> registeredPublishers) {
		this.consumers = consumers;
		this.registeredPublishers = registeredPublishers;
	}

	private HashMap<String,BusAndLocation> getKeys(){
		return Keys;
	}
	
	public void sethashnumer(int hashnumber){
		this.hashnumber=hashnumber;
	}
	private int gethashnumber(){
		return hashnumber;
	}
	public void sethashstring(String hashstring){
		this.hashstring=hashstring;
	}
	public void WakeUp() throws InterruptedException {//UNDONE
    	 int ammountofPubsThreads = 100;//UNDONE
    	 int ammountofCons = consumers.size();//UNDONE
         Thread[] threadsPub = new Thread[ammountofPubsThreads];
    	 for(int i=0; i<ammountofPubsThreads; i++){
    		threadsPub[i]= new Thread(new Broker(consumers, registeredPublishers));
    		threadsPub[i].start();
         }
    	 for(int i=0; i<ammountofPubsThreads; i++) threadsPub[i].join();
         recieveorsend =! recieveorsend;
         Thread[] threadsCon = new Thread[ammountofCons];
    	 for(int i=0; i<ammountofCons; i++){
    		threadsCon[i] = new Thread(new Broker(consumers, registeredPublishers));
    		threadsCon[i].start();
         }
    	 for(int i=0; i<ammountofCons; i++) threadsCon[i].join();
	}
	private void ChangePublisher(){//UNDONE
    	if(registeredPublishersIterator == null) registeredPublishersIterator = registeredPublishers.iterator();
		registeredPublishersIterator.next();
	}
    private void ChangeConsumer(){//UNDONE
    	 
	}

	@Override
	public void run() {
		//We will get the data and we 'll decide if we want
		//UNDONE
		int port = -1;
		if(!recieveorsend)
		try {
			while(true){
			ChangePublisher();
			if(!registeredPublishersIterator.hasNext()) break;
			ServerSocket pubServerSocket=new ServerSocket(port);//UNDONE
			Socket pubSocket=pubServerSocket.accept();
			ObjectInputStream pubOIS=new ObjectInputStream(pubSocket.getInputStream());


			String message = (String) pubOIS.readObject();
			while(message!=null){
				BusAndLocation TransformedMessage=(Suitmessage(message));
				DataFromPublisher.add(TransformedMessage);//Suitmessage UNDONE
				PutData( TransformedMessage);
				message = (String) pubOIS.readObject();
				if(!Keys.containsKey(TransformedMessage.GetBusLine())) Keys.put(TransformedMessage.GetBusLine(),TransformedMessage);
			}
			pubServerSocket.close();
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		else{
			try {
				ChangeConsumer();
				ServerSocket conServerSocket=new ServerSocket(port);//UNDONE
				Socket conSocket=conServerSocket.accept();
				ObjectOutputStream conOOS=new ObjectOutputStream(conSocket.getOutputStream());
				conOOS.writeObject("I am responsible for these keys:\n");
				for(String keys :Keys.keySet())
			    conOOS.writeObject(keys+"\n");
				//continue
				//THE OTHER BROKERS RESPONSIBLE FOR?
				int i=0;
				for(Broker br : Node.getBrokers()){
					if(gethashnumber()!=br.gethashnumber()){
						i++;
						conOOS.writeObject("Keys responsible  broker: " + i + "\n");
						for(String keys: br.getKeys().keySet()) conOOS.writeObject(keys + "\n");
					}
				}
				ObjectInputStream conOIS=new ObjectInputStream(conSocket.getInputStream());
				String message;
				message = (String) conOIS.readObject();
				
				while(message != null){
					conOOS.writeObject("Bus from line " + message+"\n");
					for(BusAndLocation dr:DataResponsible){
						if(dr.GetBusLine().equals(message))
						conOOS.writeObject("Bus: " + dr.GetBusLine() + " location"+dr.GetLongitude() + " "+dr.GetLatitude() + "\n");
					}
					message = (String) conOIS.readObject();
				}
				conOOS.writeObject("That's all for now\n");
				conServerSocket.close();
				conOOS.close();
				conOIS.close();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
		}
	}   

	public static ArrayList<BusAndLocation> GetDataFromPublisher(){
    	 return  DataFromPublisher;
	}

	private String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException ignored) {}
		return null;
	}

	private void PutData(BusAndLocation Data){//UNDONE
		String MD = MD5(Data.GetBusLine());
		int mod=hashnumber;
		if (MD != null && mod % 100 <= hashnumber && MD.equals(hashstring)) DataResponsible.add(Data);
	}

	private BusAndLocation Suitmessage(String message){//UNDONE
		BusAndLocation BAL = new BusAndLocation();
		BAL.SetTopic(message.charAt(index));//SEE
		BAL.SetValue(Integer.parseInt(message.charAt(index)));
		return BAL;
	}
}