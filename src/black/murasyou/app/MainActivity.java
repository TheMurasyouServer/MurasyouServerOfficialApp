package black.murasyou.app;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.*;
import android.util.*;
import android.graphics.drawable.*;
import android.content.pm.*;
import java.io.*;
import android.content.pm.PackageManager.*;
import query.*;
import android.content.res.Resources.*;
import java.lang.ref.*;

public class MainActivity extends Activity
{
	static int res_str,res_col;
	static QueryResponseUniverse resp;
	static boolean shutout=false,allowed=true,workerworking=false;
	static WeakReference<TextView> stat;
	static WeakReference<MainActivity> currentInstance=new WeakReference(null);
    File file=new File(Environment.getExternalStorageDirectory()+"/games/com.mojang/minecraftpe/external_servers.txt");
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		if(currentInstance.get()!=null){
			currentInstance.get().finish();
		}
		currentInstance=new WeakReference(this);
		findViewById(R.id.community).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					share("https://plus.google.com/communities/107155705374970174018");
				}
			});
		findViewById(R.id.wiki).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					share("http://wiki.murasyou.black/");
				}
			});
		stat=new WeakReference(findViewById(R.id.serverstat));
		if(!workerworking){
			new Thread(){
				public void run(){
					workerworking=true;
					while(true){
						shutout=ServerStat.isShutout();
						if(shutout){
							res_str=R.string.shutout;
							res_col=R.color.shutout;
						}else{
							try {
								resp = ServerStat.ping(getResources().getString(R.string.str_ip), (short)getResources().getInteger(R.integer.int_port));
								if("on".equals(resp.getData().get("whitelist"))){
									res_str=R.string.whitelist;
									res_col=R.color.whitelist;
								}else{
									res_str=R.string.online;
									res_col=R.color.online;
								}
							} catch (Throwable e) {
								res_str=R.string.offline;
								res_col=R.color.offline;
							}
						}
						if(stat.get()!=null&currentInstance.get()!=null){
							currentInstance.get().runOnUiThread(new Runnable(){
								public void run(){
									stat.get().setText(res_str);
									stat.get().setTextColor(currentInstance.get().getResources().getColor(res_col));
								}
							});
						}
						try {
							sleep(1000 * 30);
						} catch (InterruptedException e) {}
					}
				}
			}.start();
		}
		stat.get().setText(res_str);
		stat.get().setTextColor(getResources().getColor(res_col));
	}
	private void share(String url){
		startActivity(Intent.createChooser(new Intent().setData(Uri.parse(url)).setAction(Intent.ACTION_VIEW),getResources().getString(R.string.share)));
	}
	@Override
	protected void attachBaseContext(Context newBase){
		// TODO: Implement this method
		if(CustomApplication.currentApplication.get()!=null)
			super.attachBaseContext(CustomApplication.currentApplication.get());
		else 
			super.attachBaseContext(newBase);
	}

}
