package br.vos.nos.eu.testeiicontece.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucas on 24/10/2015.
 */
public class Movie implements Parcelable {

    private String title;
    private String id;
    private String poster_path;
    private float vote_average;
    private String release_date;
    private String overview;

    public Movie() {}

    public Movie(Parcel in) {
        title = in.readString();
        id = in.readString();
        poster_path = in.readString();
        vote_average = in.readFloat();
        release_date = in.readString();
        overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(title);
        out.writeString(id);
        out.writeString(poster_path);
        out.writeFloat(vote_average);
        out.writeString(release_date);
        out.writeString(overview);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster_path(String source) {
        if (poster_path == null) {
            return "http://image.tmdb.org/t/p/w500/6cMPLU1jv8pG9BMmieXCHDTVRPm.jpg";
        } else if (source.equals("tmdb")) {
            return "http://image.tmdb.org/t/p/w500/" + poster_path;
        } else {
            return poster_path;
        }
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getVote_average() {
        return vote_average/2;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
