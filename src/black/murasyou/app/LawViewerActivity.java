package black.murasyou.app;
import android.app.*;
import android.os.*;
import java.io.*;
import android.widget.*;

public class LawViewerActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.law);
		new AsyncTask<Void,Void,String>(){
			public String doInBackground(Void[] a){
				try{
					char[]buffer=new char[8000000];
					StringWriter sw=new StringWriter();
					InputStreamReader isr=new InputStreamReader(getAssets().open("law"));
					while (true){
						int data=isr.read(buffer);
						if(data<=0)break;
						sw.append(new String(buffer,0,data));
					}
					isr.close();
					return sw.toString();
				}catch (IOException e){
					return null;
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
