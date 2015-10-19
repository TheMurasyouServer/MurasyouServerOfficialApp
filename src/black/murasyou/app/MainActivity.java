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

public class MainActivity extends Activity
{
	File file=new File(Environment.getExternalStorageDirectory()+"/games/com.mojang/minecraftpe/external_servers.txt");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
	}
	private void share(String url){
		startActivity(new Intent().setData(Uri.parse(url)).setAction(Intent.ACTION_VIEW));
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
