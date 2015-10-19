package black.murasyou.app;
import java.net.*;
import java.io.*;

public class ServerStat
{
	public static boolean isShutout(){
		URL url;
		try {
			url=new URL("http://nao20010128nao.github.io/murasyou/status.txt");
		} catch (MalformedURLException e) {
			return false;
		}
		InputStreamReader isr=null;
		try {
			isr = new InputStreamReader(url.openConnection().getInputStream());
			
		} catch (IOException e) {
			
		}finally{
			
		}
		return false;
	}
}
