package br.vos.nos.eu.testeiicontece.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.vos.nos.eu.testeiicontece.Adapter.MovieListAdapter;
import br.vos.nos.eu.testeiicontece.Interface.MyFragmentInterface;
import br.vos.nos.eu.testeiicontece.Interface.OnFragmentInteractionListener;
import br.vos.nos.eu.testeiicontece.Model.Movie;
import br.vos.nos.eu.testeiicontece.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MovieListFragment extends Fragment implements AbsListView.OnItemClickListener, MyFragmentInterface<Movie> {


    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private List<Movie> movies;
    private int selectedItemPosition = 0;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MovieListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }


    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            selectedItemPosition = savedInstanceState.getInt("selectedItemPosition");
        }
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if(mAdapter.getCount() > 0) {
            mListView.setItemChecked(selectedItemPosition, true);
            mListView.setSelection(selectedItemPosition);
            mListView.smoothScrollToPositionFromTop(selectedItemPosition, 200, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        if (movies == null) {
            movies = new ArrayList<>();
        }
        mAdapter = new MovieListAdapter(getActivity(),R.layout.movie_list_item,R.id.movie_title, movies, mListener.getImageLoader());
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        setEmptyText(getResources().getText(R.string.sem_registros));
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            String movieId = movies.get(position).getId();
            if (movieId == null) {
                movieId = String.valueOf(position);
            }
            mListener.onFragmentInteraction(movieId);
            selectedItemPosition = position;
        }
    }


    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void update(List<Movie> retrievedItems) {
        movies.clear();
        movies.addAll(retrievedItems);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("selectedItemPosition",selectedItemPosition);
        super.onSaveInstanceState(outState);
    }
}
