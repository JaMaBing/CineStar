package de.sonnabend.cinestar;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.sonnabend.cinestar.R;
import de.sonnabend.cinestar.SettingsActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CineStar<T> extends Activity 
{
	private TableLayout tlAll;
//	private TextView tvFilms;
	private ListView lvFilms;
	private Spinner spFilms;

	private ArrayList<String> listItems;
	private ArrayAdapter<String> adapter;

	private final static String url = "http://www.cinestar.de/de/kino/wildau-cinestar/kinoprogramm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cine_star);

		tlAll = (TableLayout) findViewById(R.id.TableAll);
//		tvFilms = (TextView) findViewById(R.id.TVFilms);
//		lvFilms = (ListView) findViewById(R.id.LWFilms);
//		spFilms = (Spinner) findViewById(R.id.SPFilms);

		new LongOperation().execute(url);
//		tvFilms.setText("loading...");

		listItems=new ArrayList<String>();
//		adapter=new ArrayAdapter<String>(this,R.layout.custom_list_item,listItems);
//		lvFilms.setAdapter(adapter);

//		listItems=new ArrayList<String>();
//		adapter=new ArrayAdapter<String>(this,R.layout.custom_list_item,listItems);
//		spFilms.setAdapter(adapter);


/*		List valueList = new ArrayList<String>();
		for (int i = 0; i < 10; i++)
		{
			valueList.add("value "+i);
		}
		ListAdapter adapter = new ArrayAdapter<T>(getApplicationContext(), R.layout.custom_list_item, valueList);

	    lvFilms.setAdapter(adapter);*/
	}
/*	private void addItems(String item)
	{
		if (item.length()>0)
		{
			this.listItems.add(item);
			this.adapter.notifyDataSetChanged();
//			this.editText1.setText("");
		}
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0,0,0,"Exit");
		menu.add(0,1,0,"Settings");
//		getMenuInflater().inflate(R.menu.cine_star, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId())
		{
			case 0:
				try
				{
					CloseApp();
				}
				catch (Exception e)
				{
				}
				return true;
			case 1:
				try
				{
					startActivity(new Intent(this, SettingsActivity.class));
				}
				catch (Exception ex)
				{
					Log.e("onOptionsItemSelected", ex.toString());
				}
				return true;
		}
		return false;
	}
	public void CloseApp()
	{
		try
		{
			finish();
			System.exit(0);
		} 
		catch (Throwable ex)
		{
		}
	}
	private class LongOperation extends AsyncTask<String, Void, ArrayList<ArrayList<String>>>
	{

		private String output="";
		ArrayList<String> listFilms = new ArrayList<String>();
//		ArrayAdapter<String> adapter;
		
		@Override
		protected ArrayList<ArrayList<String>> doInBackground(String... params)
		{
			ArrayList<ArrayList<String>> FilmList = new ArrayList<ArrayList<String>>();
			ArrayList<String> listTimes = new ArrayList<String>();
			try
			{
				Document doc = Jsoup.connect(params[0]).get();

				Log.i("Star","Titel:"+doc.title());
				
				for( Element element : doc.select("a[href]") )
				{
					if(element.text().length()>0)
					{
						if(element.text().matches("^[\\d]+:[\\d]+$"))
						{
							Log.i("Star","match time "+element.text()+" # "+element.attr("href"));
							listTimes.add(element.text());
						}else
						{
							Log.i("Star","match else "+element.text()+" # "+element.attr("href"));
							listItems.add(element.text());
							FilmList.add(listTimes);
							listTimes = new ArrayList<String>();
							listTimes.add(element.text());
						}
					}
					output += element.text()+"\n";
				}
				FilmList.add(listTimes);
//				tvFilms.setText(output);
			}catch (UnknownHostException exc)
			{
				Log.i("Star","Mit dem Server konnte nicht verbunden werden. ("+exc+")");
				output="Exception:"+exc;
			}catch (Exception exc)
			{
//				tvFilms.setText("Exception"+exc);
				Log.i("Star","Exception:"+exc);
				output="Exception:"+exc;
			}

			return FilmList;
		}      

		@Override
		protected void onPostExecute(ArrayList<ArrayList<String>> result)
		{
			Activity activity=CineStar.this;
			for (ArrayList FilmList : result )
			{
				TableRow tableRow = new TableRow(activity);
				TableLayout.LayoutParams tableRowParams=new TableLayout.LayoutParams (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
				tableRowParams.setMargins(1,1,1,1);
				tableRow.setLayoutParams(tableRowParams);
				tableRow.setBackgroundColor(0xff99D689);//android:layout_margin="1dp"			    android:background="#99D689"
//				tableRow.setm
				TextView textView = new TextView(activity);
				Spinner spinner = new Spinner(activity);
				textView.setSingleLine(false);
				if(FilmList.size()>0)
					textView.setText(FilmList.get(0).toString());
				else
					textView.setText("");
				textView.setWidth(250);
//				Log.i("Star","Film: "+FilmList.get(0));
				ListIterator<String> it = FilmList.listIterator();
				ArrayList<String> times=new ArrayList<String>();
				if(it.hasNext())
					it.next();
				while(it.hasNext())
				{
					String grrrr = it.next().toString();
					times.add(grrrr);
//					spinner.add(it.next().toString());
					Log.i("Star","Time:"+grrrr);
				}
				if(times.size()>0)
				{
					adapter=new ArrayAdapter<String>(activity,R.layout.custom_list_item,times);
					spinner.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					tableRow.addView(textView);
					tableRow.addView(spinner);
				}else
				{
//					TableRow.LayoutParams params = (TableRow.LayoutParams)textView.getLayoutParams();
//					params.span = 2;
//					textView.setLayoutParams(params);
//					tableRow.addView(textView);
				}
				tlAll.addView(tableRow);
			}
//			addItems("test");
//			TextView txt = (TextView) findViewById(R.id.TVFilms);
//			ListView lvFilms = (ListView) findViewById(R.id.LWFilms);
//			adapter=new ArrayAdapter<String>(this,R.id.LWFilms,listItems);
//			setListAdapter(adapter);
//			txt.setText(output); // txt.setText(result);
			//might want to change "executed" for the returned string passed into onPostExecute() but that is upto you
		}
		
		@Override
		protected void onPreExecute()
		{
		}
		
		@Override
		protected void onProgressUpdate(Void... values)
		{
			adapter.notifyDataSetChanged();
		}
	}  
}
