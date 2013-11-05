package com.example.mapdemo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@TargetApi(8)
public class GsonRequest<T> extends Request<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "GsonRequest";

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Listener<T> listener;
    private final static String ROOT_URL = "http://petitviolet.net/hitorimeshi/api";
    private final static String USERNAME = "botteam";
    private final static String PASSWORD = "sadp2013";


    public GsonRequest(int method, String url, Class<T> clazz,
            Map<String, String> params, Listener<T> listener,
            ErrorListener errorListener) {
        super(method, ROOT_URL + url, errorListener);

        this.clazz = clazz;
        this.headers = createAuthHeaders();
        this.listener = listener;
        this.params = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
    	return params != null ? params : super.getParams();
    }
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

	private Map<String, String> createAuthHeaders() {
		// HTTPリクエストヘッダにbasic認証情報を付加する
		String userpassword = USERNAME + ":" + PASSWORD;
		final String encoded = new String(Base64.encode(
				userpassword.getBytes(), Base64.DEFAULT));
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + encoded);
		return headers;
	}

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
