package iut.projet_android;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	public AsyncHttpClient client = new AsyncHttpClient();
	private static final String BASE_URL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Activity
		final Activity activity = this;
		
		// Find elements (spinner, input, ...) from R class
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Spinner spinner = (Spinner)findViewById(R.id.spinner1);
		final ListView imageListe = (ListView)findViewById(R.id.listView1);
		Button buttonResearch = (Button)findViewById(R.id.button1);

		// Spinner adapter
		ArrayList<String> imageQuantity = new ArrayList<String>();
		for (int i = 2; i < 9; i++)
			imageQuantity.add(Integer.toString(i));
		
		ArrayAdapter<String> spinAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,imageQuantity);
		spinner.setAdapter(spinAdaptor);
		
		// Get JSON response on click from reaserch button
		buttonResearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Get some information and concatenate them to the request
				String itemNumber = ((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString();
				String inputBrut = editText.getText().toString();
				String inputResearch = inputBrut.replaceAll("\\n"," ");
				get(inputResearch+"&rsz="+itemNumber, null, new JsonHttpResponseHandler() {
		        	JSONArray images = null;
		        	JSONArray pages = null;
		        	ArrayList<Image> imagesListe = new ArrayList<Image>();
		        	
		        	// On success, get URL from all images
		            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		            	try {
		            		// Get all results
		            		images = response.getJSONObject("responseData").getJSONArray("results");
	            		
		            		// Convert the information to image objects
		            		for (int i = 0; i < images.length(); i++){
		            			Image img = new Image(images.getJSONObject(i).get("tbUrl").toString(), 
		            					images.getJSONObject(i).get("titleNoFormatting").toString(), 
		            					images.getJSONObject(i).get("unescapedUrl").toString());
		            			
		            			imagesListe.add(img);
		            		}
		            		
		            		// Image ListView adapter with inflator
		            		ImageAdapter imgAdapter = new ImageAdapter(activity,R.layout.listview_item_row, imagesListe);
		            		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);

		            		
		            		// Avoid HeaderView append if it exists and set adapter
		            		if(imageListe.getHeaderViewsCount() == 0){
			            		imageListe.addHeaderView(header, null, false);
		            		}
			            	imageListe.setAdapter(imgAdapter); 
		            		
			            	// If a click occurs on an item list
			            	imageListe.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									Image imgItem = (Image)parent.getItemAtPosition(position);
									Intent showImg = new Intent(getApplicationContext(), FullscreenActivity.class);
									showImg.putExtra(Intent.EXTRA_TEXT, imgItem.getUnscapedUrl());
									startActivity(showImg);
									
								}
							});
						} catch (JSONException e) {
							Toast.makeText(activity, "Impossible de récupérer les données: "+e.getCause(), Toast.LENGTH_LONG).show();
						}
		            }
		            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, JSONObject response) {
		            	Toast.makeText(activity, "Impossible de récupérer les données", Toast.LENGTH_LONG).show();
		            }
		        });
			}
		});
			
	}
	
	/**
	 * Method to get the absolute url
	 * @param relativeUrl which represents the last url part (params)
	 * @return base url concatenated to the relative url
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
	
	/**
	 * Method which used GET HTTP method
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	private void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	@SuppressWarnings("unused")
	private void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
}
