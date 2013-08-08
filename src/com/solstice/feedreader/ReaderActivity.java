package com.solstice.feedreader;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 *  MainActivity-Loads the feeds and populates the listView on launch
 *  
 *  @author sampathpasupunuri
 */
public class ReaderActivity extends Activity implements OnItemClickListener {

	ListView listView;
	List<FeedStructure> rssStr;
	private FeedListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		// TODO:Check Network connectivity and show alert if not connected to
		// working Internet
		listView = (ListView) findViewById(R.id.listview);
		listView.setVerticalFadingEdgeEnabled(true);
		listView.setOnItemClickListener(this);
		RssFeedTask rssTask = new RssFeedTask();
		rssTask.execute();
	}

	/**
	 *  setup asynctask to download feeds, parse and populate the listView
	 */
	private class RssFeedTask extends AsyncTask<String, Void, String> {

		private ProgressDialog Dialog;
		String response = "";

		@Override
		protected void onPreExecute() {
			Dialog = new ProgressDialog(ReaderActivity.this);
			Dialog.setMessage("Loading...");
			Dialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				String feed = "http://blog.solstice-mobile.com/feeds/posts/default";
				XMLHandler rh = new XMLHandler();
				rssStr = rh.getLatestArticles(feed);
			} catch (Exception e) {
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			if (rssStr != null) {
				adapter = new FeedListAdapter(ReaderActivity.this, rssStr);
				listView.setAdapter(adapter);
			}
			Dialog.dismiss();
		}
	}

	//Perform the action when a ListItem is selected
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, WebActivity.class);
		intent.putExtra("content", rssStr.get(arg2).getContent());
		startActivity(intent);
	}
}
