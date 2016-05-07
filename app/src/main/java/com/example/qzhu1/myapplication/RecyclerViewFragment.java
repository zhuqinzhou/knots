package com.example.qzhu1.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


public class RecyclerViewFragment extends Fragment {

    private OnListItemSelectedListener mListener;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    LinearLayoutManager mLayoutManager;
    Bundle savedInstanceStateonDestory;
    final Firebase ref = new Firebase("https://knotedb.firebaseio.com/bill");

    public RecyclerViewFragment() {
    }

    public RecyclerViewFragment newInstance(int position) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt("mode", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceStateonDestory != null) {
        } else if (savedInstanceState == null || ((ArrayList<Map<String, ?>>) savedInstanceState.getSerializable("MD")) == null) {
        } else {
        }
        Query q;
        if ((int) getArguments().get("mode") == 0) {
            q = ref;
        } else if ((int) getArguments().get("mode") == 1) {
            q = ref.orderByChild("owe").equalTo(KnotsApplication.email);
        } else {
            q = ref.orderByChild("pay").equalTo(KnotsApplication.email);
        }
        adapter = new RecyclerViewAdapter(Bill.class, R.layout.fragment_recycler_view, RecyclerViewAdapter.ViewHolder.class, q, getActivity(), (int) getArguments().get("mode"));

        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                //HashMap<String, ?> bill = (HashMap<String, ?>) adapter.getItem(positon));
                Bill bill = adapter.getItem(position);
                mListener.onListItemSelected(position, bill);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(),"haha",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOverflowMenuClick(View view, final int position) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_showDetail:
                                Bill bill = adapter.getItem(position);
                                mListener.onListItemSelected(position, bill);
                                return true;
                            case R.id.item_settle:
                                bill = adapter.getItem(position);
                                ref.child(bill.getKey()).removeValue();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu,popup.getMenu());
                popup.show();
            }

        });
        setAdapterAnimation();

        return rootView;
    }

    private void setAdapterAnimation() {
        SlideInRightAnimator animator = new SlideInRightAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
        recyclerView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        try {
            mListener = (OnListItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (adapter != null) {
//            ArrayList<Map<String, ?>> arrayList = new ArrayList<>(adapter.getMovieData().getMoviesList().size());
//            arrayList.addAll(adapter.getMovieData().getMoviesList());
//            savedInstanceState.putSerializable("MD", arrayList);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
//            savedInstanceStateonDestory = new Bundle();
//            ArrayList<Map<String, ?>> arrayList = new ArrayList<>(adapter.getMovieData().getMoviesList().size());
//            arrayList.addAll(adapter.getMovieData().getMoviesList());
//            savedInstanceStateonDestory.putSerializable("MD", arrayList);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (menu.findItem(R.id.menu_search) == null)
//            inflater.inflate(R.menu.actionbar_search, menu);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        if (searchView != null) {
//            searchView.setMaxWidth(800);
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
////                    int position = movieData.findFirst(query);
////                    if (position >= 0)
////                        recyclerView.scrollToPosition(position);
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String query) {
//                    return true;
//                }
//            });
//        }
//
//        MenuItem friendsItem = (MenuItem) menu.findItem(R.id.menu_friends).getActionView();
//        if(friendsItem!=null){
//            friendsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    getFragmentManager().popBackStack();
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
//                            .replace(R.id.drawer, FriendsFragment.newInstance())
//                            .addToBackStack(null)
//                            .commit();
//                    return false;
//                }
//            });
//        }
//
//        MenuItem contactItem = (MenuItem) menu.findItem(R.id.menu_contacts).getActionView();
//        if(contactItem!=null)
//            contactItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    getFragmentManager().popBackStack();
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
//                            .replace(R.id.drawer, ContactFragment.newInstance())
//                            .addToBackStack(null)
//                            .commit();
//                    return false;
//                }
//            });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface OnListItemSelectedListener {
        public void onListItemSelected(int position, Bill bill);
    }
}
