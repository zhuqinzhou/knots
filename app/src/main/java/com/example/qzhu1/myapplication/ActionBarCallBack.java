package com.example.qzhu1.myapplication;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;

class ActionBarCallBack implements ActionMode.Callback {
    int position;
    RecyclerViewAdapter adapter;
    final Firebase ref = new Firebase("https://knotedb.firebaseio.com/bill");
    public ActionBarCallBack(int position,RecyclerViewAdapter rvAdapter) {
        this.position=position;
        this.adapter=rvAdapter;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_delete:
                Bill bill = adapter.getItem(position);
                ref.child(bill.getKey()).removeValue();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
        mode.getMenuInflater().inflate(R.menu.actionbar_cab, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
//        mode.setTitle((String)adapter.getMovieData().getItem(position).get("name"));
        return false;
    }
}