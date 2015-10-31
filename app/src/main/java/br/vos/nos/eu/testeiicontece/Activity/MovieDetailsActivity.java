package br.vos.nos.eu.testeiicontece.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.vos.nos.eu.testeiicontece.Model.Movie;
import br.vos.nos.eu.testeiicontece.R;


public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle, movieSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    public void setUpUI() {

        movieTitle = (TextView)findViewById(R.id.movie_title);
        movieSynopsis = (TextView)findViewById(R.id.movie_synopsis);

        if(movie != null) {
            movieTitle.setText(movie.getTitle());
            movieSynopsis.setText(movie.getOverview());
        } else {
            movieTitle.setText("Titulo nao disponivel.");
            movieSynopsis.setText("Synopse nao disponivel.");
        }
    }
}
