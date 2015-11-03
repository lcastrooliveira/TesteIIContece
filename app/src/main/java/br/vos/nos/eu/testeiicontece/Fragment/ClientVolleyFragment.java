package br.vos.nos.eu.testeiicontece.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import br.vos.nos.eu.testeiicontece.Interface.TmdbListener;
import br.vos.nos.eu.testeiicontece.Util.BitMapCache;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientVolleyFragment extends Fragment {

    private String base_URL = "http://api.themoviedb.org/3/movie/";
    private String api_key = "api_key=???";

    private RequestQueue requestQueue = null;
    private ImageLoader imageLoader = null;
    private BitMapCache bitMapCache = null;

    private TmdbListener tmdbListener;

    public ClientVolleyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.tmdbListener = (TmdbListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        bitMapCache = new BitMapCache();
        imageLoader = new ImageLoader(requestQueue,bitMapCache);
        setRetainInstance(true);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void cancelAllRequests() {
        requestQueue.cancelAll(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelAllRequests();
    }

    public void getMovieList(String moviesSource) {
        if (moviesSource.equals("tmdb")) {
            String requestURL = base_URL + "now_playing?" + api_key;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    tmdbListener.onTmdbMovieListResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    tmdbListener.onTmdbMovieListResponse(null);
                }
            });
            requestQueue.add(request);
        } else if (moviesSource.equals("aws")) {
            String requestURL = "http://moises-movies3-dev.elasticbeanstalk.com/api/movies";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    tmdbListener.onTmdbMovieListResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    tmdbListener.onTmdbMovieListResponse(null);
                }
            });
            requestQueue.add(request);
        }
    }

    public void getMovieDetails(String movieId) {
            String requestUrl = base_URL + movieId + "?" + api_key;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    tmdbListener.onTmdbMovieDetailsResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    tmdbListener.onTmdbMovieDetailsResponse(null);
                }
            });
            requestQueue.add(request);
    }
}
