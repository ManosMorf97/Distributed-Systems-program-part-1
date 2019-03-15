import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class Broker implements Runnable{
	private int hashnumber;
	private String hashstring;
	private boolean recieveorsend=false;
	//we want the data from publisher
	private static ArrayList<BusAndLocation> DataFromPublisher;
	private static ArrayList<BusAndLocation> DataResponsible;
	private ArrayList<Consumer> consumers;
	private ArrayList<Publisher> registeredPublishers;
	Iterator registeredPublishersIterator=registeredPublishers.iterator();
	private static ArrayList<String> Keys;
	private static int PublisherID=-1;//UNDONE
	private static int ConsumerID=-1;//UNDONE
	private static int port=-1;//UNDONE
	
	public ArrayList<String> getKeys(){
		return Keys;
		}
	
	public void sethashnumer(int hashnumber){
		this.hashnumber=hashnumber;
	}
	public int gethashnumber(){
		return hashnumber;
	}
	public void sethashstring(String hashstring){
		this.hashstring=hashstring;
	}
     public void WakeUp(){//UNDONE
    	 int ammountofPubsThreads=100;//UNDONE
    	 int ammountofCons=consumers.size();//UNDONE
    	 Thread threadsPub[]=new Thread[ammountofPubsThreads];
    	 for(int i=0; i<ammountofPubsThreads; i++){
    		threadsPub[i]= new Thread(new Broker());
    		threadsPub[i].start();
         }
    	 for(int i=0; i<ammountofPubsThreads; i++)
			try {
				threadsPub[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 Thread threadsCon[]=new Thread[ammountofCons];
    	 for(int i=0; i<ammountofCons; i++){
    		threadsCon[i]= new Thread(new Broker());
    		threadsCon[i].start();
         }
    	 recieveorsend=!recieveorsend;
    	 for(int i=0; i<ammountofCons; i++)
 			try {
 				threadsCon[i].join();
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
     }
     public void ChangePublisher(){//UNDONE
    	 registeredPublishersIterator.next();
     }
     public void ChangeConsumer(){//UNDONE
    	 
     }
	@Override
	public void run() {
		//We will get the data and we 'll decide if we want
		if(!recieveorsend)
		try {
			while(true){
			ChangePublisher();
			if(!registeredPublishersIterator.hasNext()) break;
			ServerSocket pubServerSocket=new ServerSocket(port);//UNDONE
			Socket pubSocket=pubServerSocket.accept();
			ObjectInputStream pubOIS=new ObjectInputStream(pubSocket.getInputStream());
			try {
				
				String message = (String) pubOIS.readObject();
				while(message!=null){
					BusAndLocation TransformedMessage=(Suitmessage(message));
					DataFromPublisher.add(TransformedMessage);//Suitmessage UNDONE
					PutData( TransformedMessage);
					message = (String) pubOIS.readObject();
					if(!Keys.contains(TransformedMessage.GetBusLine())) Keys.add(TransformedMessage.GetBusLine());
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pubServerSocket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 
		}else{
			try {
				ChangeConsumer();
				ServerSocket conServerSocket=new ServerSocket(port);//UNDONE
				Socket conSocket=conServerSocket.accept();
				ObjectOutputStream conOOS=new ObjectOutputStream(conSocket.getOutputStream());
				conOOS.writeObject("I am responsible for these keys:\n");
				for(String keys :Keys)
			   conOOS.writeObject(keys+"\n");
				//continue
				//THE OTHER BROKERS RESPONSIBLE FOR?
				int i=0;
				for(Broker br : Node.getBrokers()){
					if(gethashnumber()!=br.gethashnumber()){
					 i++;
					 conOOS.writeObject("Keys responsible  broker: "+i+"\n");
					 for(String keys:br.getKeys()){
						conOOS.writeObject(keys+"\n");
					 }
					}
				}
				ObjectInputStream conOIS=new ObjectInputStream(conSocket.getInputStream());
				String message;
				try {
					message = (String) conOIS.readObject();
				
				while(message!=null){
					conOOS.writeObject("Bus from line "+message+"\n");
					for(BusAndLocation dr:DataResponsible){
						if(dr.GetBusLine()==message)
						conOOS.writeObject("Bus: "+dr.GetBusLine()+" location"+dr.GetLongitude()+" "+dr.GetLatitude()+"\n");
					}
				}
				conOOS.writeObject("That's all for now\n");
				conServerSocket.close();
				conOOS.close();
				conOIS.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
		}
	}   
	
	
	
     public static ArrayList<BusAndLocation> GetDataFromPublisher(){
    	 return  DataFromPublisher;
     }
	public String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
		}
	public void PutData(BusAndLocation Data){//UNDONE
		String MD=MD5(Data.GetBusLine());
		int mod=hashnumber;
		if(mod%100<=hashnumber&&MD.equals(hashstring)){
			DataResponsible.add(Data);
		}
	}
	public BusAndLocation Suitmessage(String message){//UNDONE
		BusAndLocation BAL= new BusAndLocation();
		BAL.SetTopic(message.charAt(index));//SEE
		BAL.SetValue(Integer.parseInt(message.charAt(index)));
		return BAL;
	}
}