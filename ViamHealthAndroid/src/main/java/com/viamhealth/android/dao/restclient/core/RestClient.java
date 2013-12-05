package com.viamhealth.android.dao.restclient.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

import com.viamhealth.android.dao.restclient.old.RequestMethod;

// Code from: http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/
public class RestClient {

	private boolean authentication;
	private ArrayList<NameValuePair> headers;
	
	private String jsonBody;
	private String message;
	
	private ArrayList<NameValuePair> params;
	private String response;
	private int responseCode;
	
	private String mUrl;
	private int maxRetries = 3;

	// HTTP Basic Authentication
	private String username;
	private String password;
	
	protected Context context;

    private RequestMethod mMethod;

    private boolean disableAutoRedirect = false;


	public RestClient(String url) {
		this.mUrl = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}
	//Be warned that this is sent in clear text, don't use basic auth unless you have to.
	public void addBasicAuthentication(String user, String pass) {
		authentication = true;
		username = user;
		password = pass;
	}

    public boolean isDisableAutoRedirect() {
        return disableAutoRedirect;
    }

    public void setDisableAutoRedirect(boolean disableAutoRedirect) {
        this.disableAutoRedirect = disableAutoRedirect;
    }

    public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

    public void AddParam(String name, Double value) {
        AddParam(name, value.toString());
    }

    public void AddParam(String name, Integer value) {
        AddParam(name, value.toString());
    }

    public void AddParam(String name, Long value) {
        AddParam(name, value.toString());
    }

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

    public void Execute(RequestMethod method) throws Exception {
        Execute(method, mUrl);
    }

	public void Execute(RequestMethod method, String url) throws Exception {
		mMethod = method;
        switch (method) {
			case GET: {
				HttpGet request = new HttpGet(url + addGetParams());
				request = (HttpGet) addHeaderParams(request);
				executeRequest(request, url);
				break;
			}
			case POST: {
				HttpPost request = new HttpPost(url);
				request = (HttpPost) addHeaderParams(request);
				request = (HttpPost) addBodyParams(request);
				executeRequest(request, url);
				break;
			}
			case PUT: {
				HttpPut request = new HttpPut(url);
				request = (HttpPut) addHeaderParams(request);
				request = (HttpPut) addBodyParams(request);
				executeRequest(request, url);
				break;
			}
			case DELETE: {
				HttpDelete request = new HttpDelete(url);
				request = (HttpDelete) addHeaderParams(request);
				executeRequest(request, url);
			}
		}
	}

	private HttpUriRequest addHeaderParams(HttpUriRequest request) throws Exception {
		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
		}
		return request;
	}

	private HttpUriRequest addBodyParams(HttpUriRequest request) throws Exception {
		if (jsonBody != null) {
			request.addHeader("Content-Type", "application/json");
			if (request instanceof HttpPost)
				((HttpPost) request).setEntity(new StringEntity(jsonBody,"UTF-8"));
			else if (request instanceof HttpPut)
				((HttpPut) request).setEntity(new StringEntity(jsonBody,"UTF-8"));
	
			} else if (!params.isEmpty()) {
				if (request instanceof HttpPost)
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(params,	HTTP.UTF_8));
				else if (request instanceof HttpPut)
					((HttpPut) request).setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			}
		return request;
	}

	private String addGetParams() throws Exception {
	//Using StringBuffer append for better performance.
		StringBuffer combinedParams = new StringBuffer();
		if (!params.isEmpty()) {
			combinedParams.append("?");
			for (NameValuePair p : params) {
				combinedParams.append((combinedParams.length() > 1 ? "&" : "")
				+ p.getName() + "="
				+ URLEncoder.encode(p.getValue(), "UTF-8"));
			}
		}
	  return combinedParams.toString();
	}

	public String getErrorMessage() {
		return message;
	}

	public String getResponse() {
		return response;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setContext(Context ctx) {
		context = ctx;	
	}	

	public void setJSONString(String data) {
		jsonBody = data;
	}

	private void executeRequest(HttpUriRequest request, String url) throws Exception {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		
		// Setting 30 second timeouts
		HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
		HttpConnectionParams.setSoTimeout(params, 60 * 1000);

        if(disableAutoRedirect)
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, false);

        client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if(i > maxRetries) return false;
                return true;
            }
        });
		HttpResponse httpResponse;

		try {
            Log.i("RestClient", "Before Request " + request.toString());
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();
            Log.i("RestClient", "After Request " + httpResponse.toString());
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
		
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
		    }

            //TODO this needs to be handled differently - need to give it a thought
            if(responseCode == HttpStatus.SC_MOVED_TEMPORARILY){
                Header[] headers = httpResponse.getHeaders("Location");
                if (headers != null && headers.length != 0) {
                    String newUrl = headers[headers.length - 1].getValue();
                    // call again with new URL
                    Execute(mMethod, newUrl);
                }
                //TODO is this a error that needs to be handled?
            }
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			return sb.toString();
	}

    @Override
    public String toString() {
        return "RestClient{" +
                "authentication=" + authentication +
                ", headers=" + headers +
                ", jsonBody='" + jsonBody + '\'' +
                ", message='" + message + '\'' +
                ", params=" + params +
                ", response='" + response + '\'' +
                ", responseCode=" + responseCode +
                ", mUrl='" + mUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", context=" + context +
                "} " + super.toString();
    }
}