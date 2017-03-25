package com.wpoppin.whatspoppin;

/**
 * Created by joseph on 3/23/2017.
 */


        import android.support.v4.util.ArrayMap;

        import java.io.UnsupportedEncodingException;
        import java.util.Map;
        import org.json.JSONException;
        import org.json.JSONObject;
        import com.android.volley.NetworkResponse;
        import com.android.volley.ParseError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.Response.ErrorListener;
        import com.android.volley.Response.Listener;
        import com.android.volley.toolbox.HttpHeaderParser;

public class CustomVolleyRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;
    private Map<String, String> headers;


    public CustomVolleyRequest(String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomVolleyRequest(int method, String url, Map<String, String> params,
                         Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;

    }


    public Map<String, String> getHeaders()
            throws com.android.volley.AuthFailureError {
        Map<String, String> headers = new ArrayMap<String, String>();
        headers.put("Authorization", "Token a9edb73eb1ecfa66b87037cbfeada07406749f96");
        return headers;
    };


    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}