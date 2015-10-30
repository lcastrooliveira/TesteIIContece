package br.vos.nos.eu.testeiicontece;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Lucas on 24/10/2015.
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader;

    public MovieListAdapter(Context context, int itemLayoutId, int defaultTextId, List<Movie> movies, ImageLoader imageLoader) {
        super(context,itemLayoutId, defaultTextId,movies);
        layoutInflater = LayoutInflater.from(context);
        this.imageLoader = imageLoader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.movie_list_item,null);
            holder = new ViewHolder();
            holder.thumbnail = (NetworkImageView)convertView.findViewById(R.id.movie_thumbnail);
            holder.title = (TextView)convertView.findViewById(R.id.movie_title);
            holder.rating = (RatingBar)convertView.findViewById(R.id.movie_rating);
            holder.release =(TextView)convertView.findViewById(R.id.movie_release);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Movie movie = this.getItem(position);
        holder.thumbnail.setImageUrl(movie.getPoster_path(), imageLoader);
        holder.rating.setRating(movie.getVote_average());
        holder.title.setText(movie.getTitle());
        holder.release.setText(movie.getRelease_date());
        return convertView;
    }

    public static class ViewHolder {
        NetworkImageView thumbnail;
        TextView title;
        TextView release;
        RatingBar rating;
    }
}
