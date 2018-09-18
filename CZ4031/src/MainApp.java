import java.util.ArrayList;

public class MainApp {
	public static void main(String[] args) {
		System.out.println("Start reading XML");
		readXML ReadXML = new readXML();
		ReadXML.getXml();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				System.out.println("Running Shutdown Hook");
			}
		});
		System.out.println("Done");
	}
	
}
