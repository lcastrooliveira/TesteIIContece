package br.vos.nos.eu.testeiicontece.Activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import br.vos.nos.eu.testeiicontece.Fragment.AboutDialog;
import br.vos.nos.eu.testeiicontece.Fragment.ClientVolleyFragment;
import br.vos.nos.eu.testeiicontece.Fragment.MovieListFragment;
import br.vos.nos.eu.testeiicontece.Interface.MyFragmentInterface;
import br.vos.nos.eu.testeiicontece.Interface.OnFragmentInteractionListener;
import br.vos.nos.eu.testeiicontece.Interface.TmdbListener;
import br.vos.nos.eu.testeiicontece.Model.Movie;
import br.vos.nos.eu.testeiicontece.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, TmdbListener {

    private ClientVolleyFragment clientVolleyFragment;
    private MyFragmentInterface<Movie> movieListFragment;

    private String moviesJson;
    private List<Movie> movies;
    private String selectedSource;

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedSource = "tmdb";
        setContentView(R.layout.activity_main);
        setUpVolleyFragment();
        setUpUI();
        FragmentManager fm = getFragmentManager();
        if (savedInstanceState != null) {
            selectedSource = savedInstanceState.getString("Source");
            moviesJson = savedInstanceState.getString("MoviesJson");
            movieListFragment = (MyFragmentInterface) fm.findFragmentByTag("MovieFragmentTag");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Movie>>() {}.getType();
            movies = gson.fromJson(moviesJson, listType);
        } else if (movieListFragment == null) {
            movieListFragment = MovieListFragment.newInstance();
        }

        if (!((Fragment) movieListFragment).isInLayout()) {
            if (savedInstanceState == null && isConnected()) {
                clientVolleyFragment.getMovieList(selectedSource);
            }
            fm.beginTransaction().replace(R.id.fragment_container, (Fragment) movieListFragment, "MovieFragmentTag").commit();
        }
    }

    public void setUpUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAboutDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public void showAboutDialog() {
        DialogFragment dialog = new AboutDialog();
        dialog.show(getFragmentManager(),"AboutDialogFragment");
    }

    public void setUpVolleyFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        clientVolleyFragment = (ClientVolleyFragment)getFragmentManager().findFragmentByTag("ClientVolleyFragmentTag");
        if(clientVolleyFragment == null) {
            clientVolleyFragment = new ClientVolleyFragment();
            ft.add(clientVolleyFragment,"ClientVolleyFragmentTag");
        }
        ft.commit();
        getFragmentManager().executePendingTransactions();
    }

    @Override
    public ImageLoader getImageLoader() {
        return clientVolleyFragment.getImageLoader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, getResources().getString(R.string.action_settings), Toast.LENGTH_LONG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tmdb) {
            clientVolleyFragment.getMovieList("tmdb");
            selectedSource = "tmdb";
        } else if (id == R.id.nav_ruby) {
            clientVolleyFragment.getMovieList("aws");
            selectedSource = "aws";
        } else if (id == R.id.nav_share) {
            shareContent();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareContent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.share_message));
        startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.share_dialog_title)));
    }


    @Override
    public void onFragmentInteraction(String id) {
        if (selectedSource.equals("tmdb")) {
            clientVolleyFragment.getMovieDetails(id);
        } else {
            Intent movieDetailsIntent = new Intent(this,MovieDetailsActivity.class);
            movieDetailsIntent.putExtra("movie",movies.get(Integer.parseInt(id)));
            startActivity(movieDetailsIntent);
        }
    }

    @Override
    public void onTmdbMovieListResponse(JSONObject jsonObject) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>() {}.getType();
        try {
            moviesJson = jsonObject.getString("results");
            movies = gson.fromJson(moviesJson, listType);
            movieListFragment.update(movies);
        } catch (JSONException e) {
            Log.e("TesteIICOntece",e.getMessage());
        }
    }

    @Override
    public void onTmdbMovieDetailsResponse(JSONObject jsonObject) {
        if(jsonObject != null) {
            Intent movieDetailsIntent = new Intent(this,MovieDetailsActivity.class);
            try {
                Movie movie = new Movie();
                movie.setTitle(jsonObject.getString("title"));
                movie.setOverview(jsonObject.getString("overview"));
                movieDetailsIntent.putExtra("movie",movie);
                startActivity(movieDetailsIntent);
            } catch (JSONException e) {
                Log.e("TesteIICOntece",e.getMessage());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("Source", selectedSource);
        savedInstanceState.putString("MoviesJson", moviesJson);
        super.onSaveInstanceState(savedInstanceState);
    }
}
