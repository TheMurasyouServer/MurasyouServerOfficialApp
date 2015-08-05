package black.murasyou.app;
import android.app.*;
import java.io.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.pm.*;
import android.util.*;

public class MenuActivity extends Activity{
	File file=new File(Environment.getExternalStorageDirectory()+"/games/com.mojang/minecraftpe/external_servers.txt");
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.draweralone);
		findViewById(R.id.addmcpe).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					if(!isMcpeInstalled()){
						Toast.makeText(MenuActivity.this,R.string.errMcpeNotInstalled,Toast.LENGTH_LONG).show();
						return;
					}
					if(!isMcpeConfigAvaliable()){
						Toast.makeText(MenuActivity.this,R.string.errMcpeConfigNotFound,Toast.LENGTH_LONG).show();
						return;
					}
					if(alreadyAddedInList()){
						return;
					}
					try{
						FileWriter fw = new FileWriter(file, true);
						fw.append("900:mura syou server:222.2.87.59:19132\n");
						fw.close();
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			});
	}
	private <T> T r(T v){return v;}
	private boolean isMcpeInstalled(){
		try{
			getPackageManager().getPackageInfo("com.mojang.minecraftpe", PackageManager.GET_ACTIVITIES);
			return true;
		}catch (PackageManager.NameNotFoundException e){
			e.printStackTrace();
			return false;
		}
	}
	private boolean isMcpeConfigAvaliable(){
		Log.d("dbg",""+file);
		return file.exists();
	}
	private boolean alreadyAddedInList(){
		FileReader fr=null;
		BufferedReader br=null;
		try{
			fr=new FileReader(file);
			br=new BufferedReader(fr);
			while(true){
				String s=br.readLine();
				if(s.endsWith(":222.2.87.59:19132"))return true;
				if(r(false))break;
			}
		}catch(Throwable ex){
			return false;
		}finally{
			try{
				if(fr!=null)fr.close();
				if(br!=null)br.close();
			}catch (IOException e){

			}
		}
		return false;
	}
}
