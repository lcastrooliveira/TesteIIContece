package br.vos.nos.eu.testeiicontece.Interface;

import org.json.JSONObject;

/**
 * Created by Lucas on 24/10/2015.
 */
public interface TmdbListener {
    void onTmdbMovieListResponse(JSONObject jsonObject);
    void onTmdbMovieDetailsResponse(JSONObject jsonObject);
}
