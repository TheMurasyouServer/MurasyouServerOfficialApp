package black.murasyou.app;
import java.net.*;
import java.io.*;
import query.*;

public class ServerStat
{
	public static boolean isShutout(){
		URL url;
		try {
			url=new URL("http://nao20010128nao.github.io/murasyou/status.txt");
		} catch (MalformedURLException e) {
			return false;
		}
		BufferedReader isr=null;
		try {
			isr = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			String s=isr.readLine();
			if("shutout".equalsIgnoreCase(s)){
				return true;
			}
		} catch (IOException e) {
			
		}finally{
			try {
				isr.close();
			} catch (Throwable e) {}
		}
		return false;
	}
	public static QueryResponseUniverse ping(String serv,short port){
		return new MCQuery(serv,port).fullStatUni();
	}
}
