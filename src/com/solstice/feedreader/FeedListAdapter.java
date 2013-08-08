package com.solstice.feedreader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter to update the data in the ListView
 * 
 * @author sampathpasupunuri
 */
public class FeedListAdapter extends ArrayAdapter<FeedStructure> {

	List<FeedStructure> metaData = null;
	Bitmap img;
	ImageView imageView;
	ImageLoader imageLoader;

	public FeedListAdapter(Activity activity, List<FeedStructure> metaDataParse) {
		super(activity, 0, metaDataParse);
		metaData = metaDataParse;
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
		LayoutInflater inflater = activity.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.listrow, null);
		TextView titleTextView = (TextView) rowView.findViewById(R.id.title);
		TextView creatorTextView = (TextView) rowView
				.findViewById(R.id.author_date);
		imageView = (ImageView) rowView.findViewById(R.id.list_image);
		try {
			titleTextView.setText(metaData.get(position).getTitle());

			// reformat current date into another format
			if (metaData.get(position).getPubDate() != null) {
				String pubDate = metaData.get(position).getPubDate().split("T")[0];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
						Locale.ENGLISH);
				Date pDate = df.parse(pubDate);
				creatorTextView.setText("Published on: " + df.format(pDate));
			}

			// pull in ImgLink, if there is no image use static image
			if (metaData.get(position).getImageLink() != null) {
				final URL feedImage = new URL(metaData.get(position)
						.getImageLink().toString());
				if (!feedImage.toString().equalsIgnoreCase("null")) {
					imageLoader.DisplayImage(metaData.get(position)
							.getImageLink(), imageView);
				} else {
					//TODO: set a dummy image
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return rowView;
	}

	/**
	 *  background processing of loading
	 *  the thumbnails and setting in the imageview 
	 */
	private class ImageLoader {
		private Map<ImageView, String> imageViews = Collections
				.synchronizedMap(new WeakHashMap<ImageView, String>());
		ExecutorService executorService;
		public ImageLoader(Context context) {
			executorService = Executors.newFixedThreadPool(5);
		}
		public void DisplayImage(String url, ImageView imageView) {
			imageViews.put(imageView, url);
			queuePhoto(url, imageView);
		}

		private void queuePhoto(String url, ImageView imageView) {
			PhotoToLoad p = new PhotoToLoad(url, imageView);
			executorService.submit(new PhotosLoader(p));
		}

		private Bitmap getBitmap(String url) {
			// from web
			try {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				bitmap = BitmapFactory.decodeStream(conn.getInputStream());
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		// Task for the queue
		private class PhotoToLoad {
			public String url;
			public ImageView imageView;

			public PhotoToLoad(String u, ImageView i) {
				url = u;
				imageView = i;
			}
		}

		class PhotosLoader implements Runnable {
			PhotoToLoad photoToLoad;

			PhotosLoader(PhotoToLoad photoToLoad) {
				this.photoToLoad = photoToLoad;
			}

			@Override
			public void run() {
				Bitmap bmp = getBitmap(photoToLoad.url);
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Activity a = (Activity) photoToLoad.imageView.getContext();
				a.runOnUiThread(bd);
			}
		}

		// Used to display bitmap in the UI thread
		class BitmapDisplayer implements Runnable {
			Bitmap bitmap;
			PhotoToLoad photoToLoad;

			public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
				bitmap = b;
				photoToLoad = p;
			}

			public void run() {
				if (bitmap != null){
					photoToLoad.imageView.setImageBitmap(bitmap);
				}else{
					//TODO:set a dummy image
				}
			}
		}
	}
}