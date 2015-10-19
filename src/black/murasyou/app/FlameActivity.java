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
	static Class prevClass;
	LocalActivityManager lam=getLocalActivityManager();
	ViewGroup prevDecor;Window localWindow;
	DrawerLayout rootDrawer;
	File file=new File(Environment.getExternalStorageDirectory()+"/games/com.mojang/minecraftpe/external_servers.txt");
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.root);
		rootDrawer=(DrawerLayout)findViewById(R.id.rootDrawer);
		if(prevClass==null)
			change(MainActivity.class);
		else
			change(prevClass);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,0,0,R.string.main);
		menu.add(0,1,0,R.string.murasyoulaw);
		menu.add(0,2,0,R.string.registInMinecraft);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Implement this method
		switch(item.getItemId()){
			case 0:
				change(MainActivity.class);
				break;
			case 1:
				change(LawViewerActivity.class);
				break;
			case 2:
				if(!isMcpeInstalled()){
					Toast.makeText(FlameActivity.this,R.string.errMcpeNotInstalled,Toast.LENGTH_LONG).show();
					break;
				}
				if(!isMcpeConfigAvaliable()){
					Toast.makeText(FlameActivity.this,R.string.errMcpeConfigNotFound,Toast.LENGTH_LONG).show();
					break;
				}
				if(alreadyAddedInList()){
					break;
				}
				try{
					FileWriter fw = new FileWriter(file, true);
					fw.append("900:mura syou server:222.2.87.59:19132\n");
					fw.close();
				}catch (IOException e){
					e.printStackTrace();
				}
				break;
		}
		return true;
	}
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
				if(br==null)break;
				if(s.endsWith(":222.2.87.59:19132"))return true;
			}
			return false;
		}catch(Throwable ex){
			return false;
		}finally{
			try{
				if(fr!=null)fr.close();
				if(br!=null)br.close();
			}catch (IOException e){

			}
		}
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
		prevClass=clazz;
	}
	public void removeFromParent(View v){
		if(v!=null)
			((ViewGroup)v.getParent()).removeView(v);
	}
}
