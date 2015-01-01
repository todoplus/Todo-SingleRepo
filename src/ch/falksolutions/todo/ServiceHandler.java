/** Zustaendig für die Server Kommunikation
 * Erstellt einen HTTP Client und uebernimmt die Kommunikation
 * übernommen und modifiziert von "http://www.androidhive.info/2012/01/android-json-parsing-tutorial/"
 */


package ch.falksolutions.todo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class ServiceHandler {

	String response;
	public final static int GET = 1;
	public final static int POST = 2;
	public final static int PUT = 3;
	public static final int DELETE = 4;
	

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			// Checking http request method type
			if (method == POST) {
				
			HttpPost httpPost = new HttpPost(url);
			// adding post params
			if (params != null) {
			httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
			Log.e("ServiceHandler","encoded Params: " + new UrlEncodedFormEntity(params,"utf-8"));
				}
				Log.d("ServiceHandler", "url= " + url);
				httpResponse = httpClient.execute(httpPost);
			

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(httpGet);

			} else if (method == PUT) {
				HttpPut httpPut = new HttpPut(url);
				if (params != null) {
					httpPut.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
				}
				httpResponse = httpClient.execute(httpPut);
			} else if (method == DELETE) {
				HttpDelete httpDelete = new HttpDelete(url);
				if (params != null) {
					//ToDo
				}
				httpResponse = httpClient.execute(httpDelete);
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;

	}
	
	
}
