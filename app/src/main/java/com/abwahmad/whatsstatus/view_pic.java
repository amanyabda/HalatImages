package com.imagesw.whatsstatus;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.abwahmad.whatsstatus.CustomPagerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class view_pic extends AppCompatActivity {
    ImageView imageView;
    String id, image, thumbImage,section;
    ProgressDialog progressDialog;
    String user_id;
    boolean fav_boolean;
    DatabaseReference add_to_fav_ref;
    Menu menu;
    InterstitialAd mInterstitialAd;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    int currentItem;

     int currentPoistion;
    private AdView mAdView;
    AdView adView;
    com.imagesw.whatsstatus.my_pic_class my_pic_class;
    ArrayList<com.imagesw.whatsstatus.my_pic_class> myPicClassArrayList;

    int position;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pic);
          my_pic_class=new com.imagesw.whatsstatus.my_pic_class();

        adView = (AdView) findViewById(R.id.adview3);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        MobileAds.initialize(this, "ca-app-pub-9393527026409681~4983770854");
        View adContainer = findViewById(R.id.adview3);

        AdView mAdView = new AdView(getApplicationContext());
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_admob));
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_admob));
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        AdRequest adRequest1 = new AdRequest.Builder()
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest1);


        define_toolbar(R.id.view_pic_toolbar);



        try {
            myPicClassArrayList = getIntent().getExtras().getParcelableArrayList("image_list");
            position = getIntent().getExtras().getInt("position");
             Log.d("postion", String.valueOf(position));
            id = myPicClassArrayList.get(position).id;

        } catch (NullPointerException e) {
            startActivity(new Intent(view_pic.this, com.imagesw.whatsstatus.sublist.class));
        }


         viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this, myPicClassArrayList));
        viewPager.setCurrentItem(position);
        imageView = findViewById(R.id.imageSwitcher);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPoistion=i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });




        user_id = getSharedPreferences("shared", 0).getString("user_id", "0");
        add_to_fav_ref = FirebaseDatabase.getInstance().getReference().child("sections").child("المفضلة")
                .child("users_favorite").child(user_id).child(id);
        }

    public void define_toolbar(int ID) {
        Toolbar toolbar = findViewById(ID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }


    public void setImage(final String image, final String thumb_image) {
        Picasso.with(view_pic.this).load(image)
                .into(imageView, new Callback() {

                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso.with(view_pic.this).load(thumbImage)
                                .into(imageView);
                    }
                });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_pic_menu, menu);
        this.menu = menu;
        Fav_boolean();

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.exit_item:
                finishAffinity(); // اغلاق كافة التطبيق
                return true;

            case R.id.view_pic_share:

                shareItem(myPicClassArrayList.get(currentPoistion).image);

                    //Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.view_pic_favorite:
                if (fav_boolean) {
                    remove_frm_fav();
                    menu.findItem(R.id.view_pic_favorite).setIcon(R.drawable.unfav);
                    fav_boolean = false;
                } else {
                    add_to_fav();
                    menu.findItem(R.id.view_pic_favorite).setIcon(R.drawable.favorite);
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            mInterstitialAd.show();

                        }

                    });
                    fav_boolean = true;
                }
                return true;
            case R.id.view_pic_download:
                download_pic(myPicClassArrayList.get(currentPoistion).image);

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();

                    }

                });
                return true;
            case R.id.home: // برمجة العنصر الرابع
                Intent recipes_intent = new Intent(view_pic.this, com.imagesw.whatsstatus.sublist.class);
                recipes_intent.putExtra("section", "إسلامية");
                startActivity(recipes_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareItem(String url) {

        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/jpg");
                // i.setType("*/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        //Uri bmpUri = null;
        Uri imageUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            //  bmpUri = Uri.fromFile(file);
            imageUri = FileProvider.getUriForFile(
                    view_pic.this,
                    "com.imagesw.whatsstatus.provider", //(use your app signature + ".provider" )
                    file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }

    public void Fav_boolean() {
        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference().child("sections").child("المفضلة").child("users_favorite").child(user_id);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(id)) {

                    fav_boolean = true;
                    menu.findItem(R.id.view_pic_favorite).setIcon(R.drawable.favorite);
                    // run some code
                } else {
                    menu.findItem(R.id.view_pic_favorite).setIcon(R.drawable.unfav);
                    fav_boolean = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void add_to_fav() {

        add_to_fav_ref.setValue(new com.imagesw.whatsstatus.my_pic_class(image, thumbImage, id));


    }

    public void remove_frm_fav() {

        add_to_fav_ref.removeValue();

    }

    public boolean checkPermissionWRITE_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public void download_pic(String image){
        Picasso.with(getApplicationContext()).load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                if (checkPermissionWRITE_EXTERNAL_STORAGE(view_pic.this)) {
                    try {
                        File directory = new File(Environment.getExternalStorageDirectory()
                                + File.separator, getResources().getString(R.string.app_name));

                        if (!directory.exists()) {
                            directory.mkdirs();

                        }


                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String name = " " + n + ".jpg";
                        File pictureFile = new File(directory, name);
                        pictureFile.createNewFile();
                        FileOutputStream out = new FileOutputStream(pictureFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.close();
                        Toast.makeText(view_pic.this, "تم حفظ الصورة", Toast.LENGTH_SHORT).show();
                        MediaScannerConnection.scanFile(view_pic.this, new String[]
                                { pictureFile.getPath() }, new String[] { "image/jpg" }, null);


                    } catch (Exception e) {
                        Toast.makeText(view_pic.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        Toast.makeText(view_pic.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        Toast.makeText(view_pic.this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }



            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if ( resultCode == Activity.RESULT_OK) {
            // download_pic();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download_pic(myPicClassArrayList.get(currentPoistion).image);
                } else {
                    Toast.makeText(view_pic.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }

    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.app_id_admob))
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        showInterstitial();
    }

}

