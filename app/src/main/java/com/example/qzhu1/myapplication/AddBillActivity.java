package com.example.qzhu1.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBillActivity extends AppCompatActivity {

    private EditText name;
    Firebase firebaseRef;
    String[] friendList;
    boolean[] friendSelected;
    List<String> oweList = new ArrayList<String>();
    private ListView areaCheckListView;

    private static final String FIREBASEREF = "https://knotedb.firebaseio.com/";

    private static final int PLACE_PICKER_REQUEST = 1020;
    private static final int REQUEST_CODE_PLAY_SERVICES = 1235;
    private String longitude;
    private String latitude;
    private String address;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_bill);
        Button al = (Button) findViewById(R.id.addLocationBtn);
        al.setOnClickListener(button_listener);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Add Bill");

        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.usdollars).centerCrop().into(imageView);

        final EditText description = (EditText) findViewById(R.id.addBill_description);
        final EditText amount = (EditText) findViewById(R.id.addBill_amount);
        final EditText location = (EditText) findViewById(R.id.addBillEditText_location);
        final EditText payer = (EditText) findViewById(R.id.addBillEditText_payer);
        final EditText owe = (EditText) findViewById(R.id.addBillEditText_owe);

        //This part is for getting all the friend's list
        firebaseRef = new Firebase(FIREBASEREF);
        Firebase userRef = firebaseRef.child("user");
        final List<String> fl = new ArrayList<String>();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User it = postSnapshot.getValue(User.class);
                    if (it.getEmail().toLowerCase().equals(KnotsApplication.email.toLowerCase())) {
                        String tempFriend = it.getFriends();
                        while (!tempFriend.equals("")) {
                            fl.add(tempFriend.substring(0, tempFriend.indexOf(";")));
                            tempFriend = tempFriend.substring(tempFriend.indexOf(";") + 1, tempFriend.length());
                        }
                        //System.out.println(fl);
                        friendList = fl.toArray(new String[fl.size()]);
                        friendSelected = new boolean[fl.size()];
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        //friendSelected = new Boolean[friendList.length]
        Button checkBoxBtn = (Button) findViewById(R.id.checkBoxButton);
        checkBoxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(AddBillActivity.this)
                        .setTitle("Select User")
                        .setMultiChoiceItems(friendList, friendSelected, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {//点击某个区域
                            }
                        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String s = "";
                                for (int i = 0; i < friendSelected.length; i++) {
                                    if (areaCheckListView.getCheckedItemPositions().get(i)) {
                                        oweList.add(areaCheckListView.getAdapter().getItem(i).toString());
                                        s += areaCheckListView.getAdapter().getItem(i) + ";";
                                    } else {
                                        areaCheckListView.getCheckedItemPositions().get(i, false);
                                    }
                                }
                                if (areaCheckListView.getCheckedItemPositions().size() > 0) {
                                    //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                                    owe.setText(s);
                                } else {
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel", null).create();
                areaCheckListView = ad.getListView();
                ad.show();
            }
        });


        Button addBillBtn = (Button) findViewById(R.id.addBill_button);
        addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase billRef = firebaseRef.child("bill");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer totalAmount = Integer.parseInt(amount.getText().toString());
                double eachShare = totalAmount / (oweList.size() + 1);
                String es = String.valueOf(eachShare);
                for (int i = 0; i < oweList.size(); i++) {
                    Map<String, String> bill = new HashMap<String, String>();
                    bill.put("amount", es);
                    bill.put("description", description.getText().toString());
                    bill.put("name", location.getText().toString());
                    bill.put("owe", oweList.get(i));
                    bill.put("pay", payer.getText().toString());
                    bill.put("time", df.format(new Date()));
                    bill.put("address", address);
                    bill.put("longitude", longitude);
                    bill.put("latitude", latitude);
                    bill.put("phoneNumber", phoneNumber);
                    Firebase br = billRef.push();
                    String key = br.getKey();
                    bill.put("key", key);
                    br.setValue(bill);
                }
                Intent intent = new Intent(AddBillActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(AddBillActivity.this, v, "testAnimation");
//                    startActivity(intent, options.toBundle());
//                } else {
//                    startActivity(intent);
//                }
            }
        });

    }

    private Button.OnClickListener button_listener = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(builder.build(AddBillActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
            } catch (GooglePlayServicesNotAvailableException e) {
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, this);
                Toast.makeText(getApplicationContext(), selectedPlace.getName().toString(), Toast.LENGTH_LONG).show();
                name = (EditText) findViewById(R.id.addBillEditText_location);
                longitude = String.valueOf(selectedPlace.getLatLng().longitude);
                latitude = String.valueOf(selectedPlace.getLatLng().latitude);
                address = selectedPlace.getAddress().toString();
                phoneNumber = selectedPlace.getPhoneNumber().toString();
                name.setText(selectedPlace.getName());
            }
        }
    }

}
