package black.murasyou.app;
import android.app.*;
import android.view.*;
import android.support.v4.widget.*;
import java.io.*;
import android.os.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.util.*;
import android.content.pm.*;
import android.content.*;
import android.support.v4.app.*;
import java.lang.ref.*;
import java.util.*;
import android.support.v4.view.*;
/*ActivityGroupとFragmentActivityを両方使いたいのよ*/
public class PagerFlameActivity extends ActivityGroup{
	static int sdkint=Build.VERSION.SDK_INT;
	public static WeakReference<PagerFlameActivity> instance=new WeakReference<PagerFlameActivity>(null);
	//Map<Integer,Class<? extends Activity>> dic=new ArrayMap<>();
	List<Map.Entry<Integer,Class<? extends Activity>>> helper=new ArrayList<>();
	Map<Class<? extends Activity>,View> decorCache=new ArrayMap<>();
	List<View> helper2=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		/*LIST PAGES*/
		addTab(R.string.main,MainActivity.class);
		//addTab(R.string.menu,MenuActivity.class);
		addTab(R.string.menu,PagerCore.MenuActivity.class);
		addTab(R.string.murasyoulaw,LawViewerActivity.class);

		//for(Map.Entry<Integer,Class<? extends Activity>> i:dic.entrySet())helper.add(i);
		for(Map.Entry<Integer,Class<? extends Activity>> i:helper)decorCache.put(dbg(i.getValue()),openActivity(i.getValue()));
		for(Map.Entry<Class<? extends Activity>,View> i:decorCache.entrySet())helper2.add(i.getValue());
		
		if(sdkint>=21)getActionBar().setElevation(0);
		instance=new WeakReference<PagerFlameActivity>(this);
		setContentView(getLocalActivityManager().startActivity("core",new Intent(this,PagerCore.class)).getDecorView());
	}
	public void addTab(int res,Class<? extends Activity> clz){
		helper.add(new ME<Integer,Class<? extends Activity>>(res,clz));
	}
	private <T> T dbg(T obj){
		Log.d("dbg",obj+"");
		return obj;
	}
	/*private View openActivity(Class<? extends Activity> clazz){
		return pfa.getLocalActivityManager().startActivity(clazz.toString(),new Intent(pfa,clazz)).getDecorView();
	}*/
	public static View removeFromParent(View v){
		if(v!=null)
			if(v.getParent()!=null)
				((ViewGroup)v.getParent()).removeView(v);
		return v;
	}
	public class ME<K,V> implements Map.Entry<K,V>{
		K key;
		V value;
		public ME(K k,V v){
			key=k;
			value=v;
		}
		@Override
		public V setValue(V p1){
			// TODO: Implement this method
			return p1;
		}
		@Override
		public V getValue(){
			// TODO: Implement this method
			return value;
		}
		@Override
		public K getKey(){
			// TODO: Implement this method
			return key;
		}
	}
	public static class PagerCore extends FragmentActivity{
		PagerFlameActivity pfa=PagerFlameActivity.instance.get();
		@Override
		protected void onCreate(Bundle savedInstanceState){
			// TODO: Implement this method
			super.onCreate(savedInstanceState);
			setContentView(R.layout.pagerroot);
			((ViewPager)findViewById(R.id.rootDrawer)).setAdapter(new PagerAdapter());
		}
		public class PagerAdapter extends FragmentStatePagerAdapter{
			public PagerAdapter(){
				super(getSupportFragmentManager());
			}

			@Override
			public CharSequence getPageTitle(int position){
				// TODO: Implement this method
				return pfa.getResources().getString(pfa.helper.get(position).getKey());
			}
			
			@Override
			public android.support.v4.app.Fragment getItem(int p1){
				// TODO: Implement this method
				return new PAFragment(getCount()-p1-1);
			}

			@Override
			public int getCount(){
				// TODO: Implement this method
				return pfa.helper.size();
			}
		}
		public static class PAFragment extends android.support.v4.app.Fragment{
			int offset;
			public PAFragment(int offset){
				this.offset=offset;
			}
			public PAFragment(){

			}
			@Override
			public View onCreateView(LayoutInflater inflater,
									 ViewGroup container,
									 Bundle savedInstanceState) {
				View tmp=inflater.inflate(R.layout.mainframe, null);
				((ViewGroup)tmp.findViewById(R.id.activityRoot))
					.addView(removeFromParent(instance.get().helper2.get(offset)));
				return tmp;
			}
			public Class<? extends Activity> getClass(String cn){
				try{
					return (Class<? extends Activity>)Class.forName(cn);
				}catch (ClassNotFoundException e){
					return null;
				}
			}
			@Override
			public void onSaveInstanceState(Bundle outState){
				// TODO: Implement this method
				super.onSaveInstanceState(outState);
				outState.putInt("offset",offset);
			}
		}
		public static class MenuActivity extends Activity{
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
	}
	private View openActivity(Class<? extends Activity> clazz){
		return getLocalActivityManager().startActivity(clazz.toString(),new Intent(this,clazz)).getDecorView();
	}
	/*AsyncTask<ViewGroup,Void,Void> viewDebugger=new AsyncTask<ViewGroup,Void,Void>(){
		@Override
		protected Void doInBackground(ViewGroup[] p1){
			// TODO: Implement this method
			ViewGroup vg=p1[0];
			try{
				OutputStreamWriter osw=new OutputStreamWriter(openFileOutput("decor.txt", MODE_MULTI_PROCESS));
				printHirarchy(vg,0,osw);
				osw.close();
			}catch (IOException e){
				e.printStackTrace();
			}
			return null;
		}
		public void printHirarchy(ViewGroup group,int indent,Writer output)throws IOException{
			output.write("|"+group.getClass().getName());
		}
	};*/
}
