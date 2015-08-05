package black.murasyou.app;
import android.app.*;
import android.os.*;
import java.lang.ref.*;
import android.util.*;
import java.util.zip.*;
import java.io.*;
import android.preference.*;

public class CustomApplication extends Application{
	public static WeakReference<CustomApplication> currentApplication=new WeakReference<CustomApplication>(null);
	volatile transient byte[] buffer=new byte[2048*8*16];
	@Override
	public void onCreate(){
		// TODO: Implement this method
		super.onCreate();
		Log.d("dbg","called");
		currentApplication=new WeakReference<CustomApplication>(this);
		new AsyncTask<Void,Void,Boolean>(){
			public Boolean doInBackground(Void[] params){
				if(PreferenceManager.getDefaultSharedPreferences(CustomApplication.this).getBoolean("extracted",false))return true;
				try{
					ZipInputStream zis=new ZipInputStream(openAsset("RakLib-master.zip"));
					while(true){
						ZipEntry ze=zis.getNextEntry();
						if(ze==null)break;
						String tmpname=ze.getName();
						if(tmpname.startsWith("/"))tmpname=tmpname.substring(2);
						Log.d("dbg",tmpname);
						OutputStream saveto=openFileOutput(tmpname,MODE_WORLD_WRITEABLE);
						while(true){
							int t=zis.read(buffer);
							if(t<=0)break;
							saveto.write(buffer,0,t);
						}
						saveto.close();
					}
					zis.close();
				}catch (IOException e){
					e.printStackTrace();
					return false;
				}
				try{
					ZipInputStream zis=new ZipInputStream(openAsset("data.zip"));
					while(true){
						ZipEntry ze=zis.getNextEntry();
						if(ze==null)break;
						String tmpname=ze.getName();
						if(!tmpname.startsWith("/"))tmpname="/"+tmpname;
						tmpname="php"+tmpname;
						Log.d("dbg",tmpname);
						OutputStream saveto=openFileOutput(tmpname,MODE_WORLD_WRITEABLE);
						while(true){
							int t=zis.read(buffer);
							if(t<=0)break;
							saveto.write(buffer,0,t);
						}
						saveto.close();
					}
					zis.close();
				}catch (IOException e){
					e.printStackTrace();
					return false;
				}
				return true;
			}
			public InputStream openAsset(String name){
				//if(Build.VERSION.SDK_INT<=8){
					try{
						InputStream is=new BufferedInputStream(new FileInputStream(getPackageResourcePath()));
						ZipInputStream zis=new ZipInputStream(is);
						while(zis.getNextEntry().getName().toLowerCase().endsWith(name.toLowerCase())&seekEnd(zis))o();
						return new BufferedInputStream(zis);
					}catch (Throwable e){
						e.printStackTrace();
					}
				/*}else{
					
				}*/
				return null;
			}
			private boolean seekEnd(InputStream is)throws IOException{
				while(is.read(buffer)!=-1)o();
				return true;
			}
			private void o(){}
			public void onPostExecute(Boolean result){
				if(result)
					PreferenceManager.getDefaultSharedPreferences(CustomApplication.this).edit().putBoolean("extracted",true);
			}
		};//.execute();
	}
}
