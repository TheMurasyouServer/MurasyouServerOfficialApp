package black.murasyou.app;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.graphics.drawable.*;
import android.util.*;
import android.content.pm.*;
import android.content.*;
import android.support.v4.widget.*;

public class FlameActivity extends ActivityGroup{
	LocalActivityManager lam=getLocalActivityManager();
	ViewGroup prevDecor;Window localWindow;
	DrawerLayout rootDrawer;
	File file=new File(Environment.getExternalStorageDirectory()+"/games/com.mojang/minecraftpe/external_servers.txt");
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.root);
		findViewById(R.id.addmcpe).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					if(!isMcpeInstalled()){
						Toast.makeText(FlameActivity.this,R.string.errMcpeNotInstalled,Toast.LENGTH_LONG).show();
						return;
					}
					if(!isMcpeConfigAvaliable()){
						Toast.makeText(FlameActivity.this,R.string.errMcpeConfigNotFound,Toast.LENGTH_LONG).show();
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
		findViewById(R.id.funcMain).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					change(MainActivity.class);
				}
			});
		findViewById(R.id.funcLaw).setOnClickListener(new View.OnClickListener(){
				public void onClick(View p){
					change(LawViewerActivity.class);
				}
			});
		findViewById(R.id.drawerBG).setBackground(resolveBackground());
		rootDrawer=(DrawerLayout)findViewById(R.id.rootDrawer);
		rootDrawer.setWillNotCacheDrawing(true);
		rootDrawer.setDrawingCacheEnabled(false);
		change(MainActivity.class);
    }
	private Drawable resolveBackground(){
		if(r(false))return getWindow().getDecorView().getBackground();
		Drawable tmp=null;ViewGroup tmp2=(ViewGroup)findViewById(android.R.id.content);
		while(tmp==null){
			Log.d("dbg","tmp:"+tmp+";tmp2:"+tmp2+";tmp2.getId():"+tmp2.getId());
			tmp=tmp2.getBackground();
			tmp2=(ViewGroup)tmp2.getParent();
		}
		Log.d("dbg","tmp:"+tmp+";tmp2:"+tmp2);
		return tmp;
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
	@Override
	protected void attachBaseContext(Context newBase){
		// TODO: Implement this method
		if(CustomApplication.currentApplication.get()!=null)
			super.attachBaseContext(CustomApplication.currentApplication.get());
		else 
			super.attachBaseContext(newBase);
	}

	@Override
	public void change(Class<? extends Activity> clazz){
		// TODO: Implement this method
		try{
			lam.destroyActivity("main",true);
		}catch(Throwable e){
			
		}
		localWindow=lam.startActivity("main",new Intent(this,clazz));
		ViewGroup vg=(ViewGroup)findViewById(R.id.activityRoot);
		removeFromParent(prevDecor);
		vg.addView(prevDecor=(ViewGroup)localWindow.getDecorView());
		rootDrawer.invalidate();
	}
	public void removeFromParent(View v){
		if(v!=null)
			((ViewGroup)v.getParent()).removeView(v);
	}
}
