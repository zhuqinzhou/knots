package com.example.qzhu1.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements RecyclerViewAdapter.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        RecyclerViewFragment.OnListItemSelectedListener {

    private static final String TAG = "NetworkStateReceiver";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    FragmentPagerAdapter adapterViewPager;
    //    final Firebase ref = new Firebase("https://knotedb.firebaseio.com");
    final Firebase ref = new Firebase("https://knotedb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.floating_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddBillActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(8);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new FlipPageViewTransformer());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        String queryStr="";
        try {
            queryStr = KnotsApplication.email.substring(0, KnotsApplication.email.indexOf("@"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            KnotsApplication.email = "123@123.com";
//            queryStr="123";
        }

        ref.child("user").child(queryStr).child("avatar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ImageView photo = (ImageView) findViewById(R.id.header_image);
                    String photoStr = (String) dataSnapshot.getValue();
                    if (photoStr == null)
                        return;
                    if (photoStr.equalsIgnoreCase(""))
                        return;
                    byte[] decodedBytes = Base64.decode(photoStr, 0);
                    Bitmap bm = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    if (bm == null)
                        return;
                    photo.setImageBitmap(bm);
                } catch (Exception e){
                    System.out.println("==================== bitmap null ====================");
                    System.out.println(KnotsApplication.email);
                    e.printStackTrace();
                } finally {
                    // continue
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        actionBarDrawerToggle.syncState();
    }

    public static class HomePagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;
//        private MovieData movieData=new MovieData();

        public HomePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new PieChartFragment();
            RecyclerViewFragment temp = new RecyclerViewFragment();
            return temp.newInstance(position);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "balance";
                case 1:
                    return "owe";
                case 2:
                    return "pay";
                default:
                    return "error";
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.menu_contacts:
                getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.drawer, ContactFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            case R.id.menu_friends:
                getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.drawer, FriendsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMoiveFragmentSelected(HashMap<String, ?> movie) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer, EventFragment.newInstance(movie))
                .addToBackStack(null)
                .commit();
    }

    public void onMovieMenuSelected(final int position, View view, final RecyclerViewAdapter rvAdapter) {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.item_delete:
//                        Item cloudDelete = rvAdapter.getItem(position);
//                        ref.child(cloudDelete.getId()).removeValue();
//                        return true;
//                    case R.id.item_duplicate:
//                        Item cloud = rvAdapter.getItem(position);
//                        cloud.setName(cloud.getName() + "_New");
//                        cloud.setId(cloud.getId() + "_new");
//                        ref.child(cloud.getId()).setValue(cloud);
//                        return true;
                }
                return true;
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    public void onMovieLongClickSelected(final int position, View view, final RecyclerViewAdapter rvAdapter) {
        startSupportActionMode(new ActionBarCallBack(position, rvAdapter));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.drawer_add_bill:
                intent = new Intent(this, AddBillActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            case R.id.drawer_friends:
                getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.drawer, FriendsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.drawer_contacts:
                getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.drawer, ContactFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.drawer_add_avatar:
                getFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.drawer, PhotoFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.drawer_logout:
                ref.unauth();
                intent=new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public class FlipPageViewTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            float percentage = 1 - Math.abs(position);
            page.setCameraDistance(12000);
            setVisibility(page, position);
            setTranslation(page);
            setSize(page, position, percentage);
            setRotation(page, position, percentage);
        }

        private void setVisibility(View page, float position) {
            if (position < 0.5 && position > -0.5) {
                page.setVisibility(View.VISIBLE);
            } else {
                page.setVisibility(View.INVISIBLE);
            }
        }

        private void setTranslation(View page) {
            ViewPager viewPager = (ViewPager) page.getParent();
            int scroll = viewPager.getScrollX() - page.getLeft();
            page.setTranslationX(scroll);
        }

        private void setSize(View page, float position, float percentage) {
            page.setScaleX((position != 0 && position != 1) ? percentage : 1);
            page.setScaleY((position != 0 && position != 1) ? percentage : 1);
        }

        private void setRotation(View page, float position, float percentage) {
            if (position > 0) {
                page.setRotationY(-180 * (percentage + 1));
            } else {
                page.setRotationY(180 * (percentage + 1));
            }
        }
    }

    public void onListItemSelected(int position, Bill bill) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer, BillDetailFragment.newInstance(bill))
                .addToBackStack(null)
                .commit();
//        Intent intent = new Intent(this, BillDetailActivity.class);
//        intent.putExtra("bill",bill);
//        startActivity(intent);
    }

}
