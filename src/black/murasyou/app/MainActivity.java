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
	static int res_str=R.string.loading,res_col=android.R.color.black;
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
		currentInstance=new WeakReference(this);
		findViewById(R.id.community).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					share("https://plus.google.com/communities/107155705374970174018");
				}
			});
		stat=new WeakReference(findViewById(R.id.serverstat));
		stat.get().setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					if(isWisecraftInstalled()&((res_str==R.string.online)|(res_str==R.string.whitelist))){
						startActivity(new Intent()
									  .setClassName("com.nao20010128nao.Wisecraft",".api.RequestedServerInfoActivity")
									  .setAction("com.nao20010128nao.Wisecraft.SERVER_INFO")
									  .addCategory(Intent.CATEGORY_LAUNCHER)
									  .putExtra("com.nao20010128nao.Wisecraft.SERVER_IP",getResources().getString(R.string.str_ip))
									  .putExtra("com.nao20010128nao.Wisecraft.SERVER_PORT",getResources().getIntArray(R.integer.int_port)));
					}
				}
			});
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
	private boolean isWisecraftInstalled(){
		try{
			getPackageManager().getPackageInfo("com.nao20010128nao.Wisecraft", PackageManager.GET_ACTIVITIES);
			return true;
		}catch (PackageManager.NameNotFoundException e){
			e.printStackTrace();
			return false;
		}
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
