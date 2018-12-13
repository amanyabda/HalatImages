package com.imagesw.whatsstatus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class sublist extends com.imagesw.whatsstatus.BaseActivity {
    String section;
    public RecyclerView sub_list;
    public DatabaseReference sub_titles;
    boolean first_run_boolean;
    String user_id;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    GridLayoutManager gridLayoutManager;
    int lastFirstVisiblePosition;
    ArrayList<com.imagesw.whatsstatus.my_pic_class> myPicClassArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_sublist, null, false);
        drawer.addView(contentView, 0);

        first_run_boolean = getSharedPreferences("shared", 0).getBoolean("first_run_boolean", true);
        is_first_run();


        MobileAds.initialize(this, getResources().getString(R.string.app_id_admob));
        AdView mAdView = (AdView) findViewById(R.id.adview2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        try {
            section = getIntent().getExtras().getString("section");

        } catch (NullPointerException e) {

            section = "إسلامية";
        }
        String user_id = getSharedPreferences("shared", 0).getString("user_id", "0");


        myPicClassArrayList = new ArrayList<>();
        sub_list = findViewById(R.id.sub_list);
        sub_list.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 3);
        sub_list.setLayoutManager(gridLayoutManager);
        if (section.equals("المفضلة")) {
            sub_titles = FirebaseDatabase.getInstance().getReference().child("sections").
                    child(section).child("users_favorite").child(user_id);

        } else {
            sub_titles = FirebaseDatabase.getInstance().getReference().child("sections").child(section).child("status");
            //  gridLayoutManager.setReverseLayout(true);

        }
        sub_titles.keepSynced(true);
        // sub_list.scrollToPosition(10);
    }

    protected void onStart() {
        super.onStart();
        startListening();

    }


    public void startListening() {
        //  Query query = FirebaseDatabase.getInstance().getReference().child("users");

        FirebaseRecyclerOptions<com.imagesw.whatsstatus.my_pic_class> options =
                new FirebaseRecyclerOptions.Builder<com.imagesw.whatsstatus.my_pic_class>()
                        .setQuery(sub_titles, com.imagesw.whatsstatus.my_pic_class.class)
                        .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<com.imagesw.whatsstatus.my_pic_class,
                ViewHolder>(options) {

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new_back instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_sub_list, parent, false);

                return new ViewHolder(view);
            }


            @NonNull
            @Override
            public com.imagesw.whatsstatus.my_pic_class getItem(int position) {
                return super.getItem(getItemCount() - position - 1);
            }


            @Override
            protected void onBindViewHolder(final ViewHolder viewHolder, final int position, final com.imagesw.whatsstatus.my_pic_class titles) {
                // Bind the Chat object to the ChatHolder
                myPicClassArrayList.add(titles);
                viewHolder.setImage(titles.thumbImage, sublist.this);
                //set typeface
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile_intent = new Intent(sublist.this, com.imagesw.whatsstatus.view_pic.class);
                        profile_intent.putExtra("position", position);
                        Toast.makeText(sublist.this, ""+position, Toast.LENGTH_SHORT).show();
                        profile_intent.putParcelableArrayListExtra("image_list", myPicClassArrayList);
//                        profile_intent.putExtra("id", titles.id);
//                        profile_intent.putExtra("image", titles.image);
//                        profile_intent.putExtra("thumbImage", titles.thumbImage);
//                        profile_intent.putExtra("section", section);
                        startActivity(profile_intent);
                        lastFirstVisiblePosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                    }
                });


            }

        };
        sub_list.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                sub_list.scrollToPosition(lastFirstVisiblePosition);

            }
        });

    }

    // you must to put static here
    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setImage(final String thumb_image, final Context context) {
            final ImageView row_img = mView.findViewById(R.id.row_img);
            Picasso.with(context).load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(row_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(thumb_image)
                                    .into(row_img);

                        }
                    });

        }


    }

    public void is_first_run() {
        if (first_run_boolean) {
            DatabaseReference databaseReference =
                    FirebaseDatabase.getInstance().getReference().child("sections").child("المفضلة")
                            .child("users_favorite").push();
            user_id = databaseReference.getKey();


            getSharedPreferences("shared", 0).edit()
                    .putString("user_id", user_id)
                    .putBoolean("first_run_boolean", false).apply();

        } else {

        }
    }

}