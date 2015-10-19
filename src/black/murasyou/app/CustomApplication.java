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
	}
}
