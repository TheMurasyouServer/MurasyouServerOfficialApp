package black.murasyou.app;
import android.app.*;
import android.os.*;
import java.io.*;
import android.widget.*;
import javax.crypto.*;

public class LawViewerActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.law);
		new AsyncTask<Void,Void,String>(){
			public String doInBackground(Void[] a){
				char[]buffer=new char[1024];
				InputStreamReader isr=null;
				try{
					StringBuilder sw=new StringBuilder();
					isr=new InputStreamReader(getAssets().open("law"));
					while (true){
						int data=isr.read(buffer);
						if(data<=0)break;
						sw.append(buffer,0,data);
					}
					return sw.toString();
				}catch (IOException e){
					return null;
				}finally{
					try {
						if (isr != null)
							isr.close();
					} catch (IOException e) {
						
					}
				}
			}
			public void onPostExecute(String result){
				if(result==null){
					result=getResources().getString(R.string.errLoad);
				}
				((TextView)findViewById(R.id.lawtext)).setText(result);
			}
		}.execute();
	}
}
