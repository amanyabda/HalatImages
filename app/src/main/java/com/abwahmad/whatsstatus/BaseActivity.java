package com.imagesw.whatsstatus;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    protected Toolbar toolbar;

    private AdView mAdView;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        MobileAds.initialize(this, "ca-app-pub-9393527026409681~5501040946");

        AdView mAdView = new AdView(getApplicationContext());
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_admob));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_video));
        mInterstitialAd.setAdListener(new AdListener() {
        });
        AdRequest adRequest1 = new AdRequest.Builder()
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(false);  // hides action bar icon
        getSupportActionBar().setDisplayShowTitleEnabled(false); // hides action bar title


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        final String[] titles =
                new String[]{"إسلامية",
                        "الصباح والمساء",
                        "حب وغرام",
                        "الجمعة",
                        "حكم وعبر",
                        "خواطر",
                        "الأب والأم",
                        "صداقة",
                        "زواج وخطوبة",
                        "متنوعة",
                        "ورود",
                        "المفضلة"
        };


        if (id == R.id.islamic) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[0]);
            startActivity(sublist_intent);

            mInterstitialAd.show();

        } else if (id == R.id.day_night) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[1]);
            startActivity(sublist_intent);

        } else if (id == R.id.love) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[2]);
            startActivity(sublist_intent);
            mInterstitialAd.show();

        } else if (id == R.id.friday) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[3]);
            startActivity(sublist_intent);

        } else if (id == R.id.wisdom) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[4]);
            startActivity(sublist_intent);
            mInterstitialAd.show();

        } else if (id == R.id.khwater) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[5]);
            startActivity(sublist_intent);
        } else if (id == R.id.mom_dad) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[6]);
            startActivity(sublist_intent);
        } else if (id == R.id.freindship) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[7]);
            startActivity(sublist_intent);
        } else if (id == R.id.mariage) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[8]);
            startActivity(sublist_intent);
        } else if (id == R.id.other) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[9]);
            startActivity(sublist_intent);
        } else if (id == R.id.worod) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[10]);
            startActivity(sublist_intent);
        } else if (id == R.id.favorite_drawer) {
            Intent sublist_intent = new Intent(BaseActivity.this, com.imagesw.whatsstatus.sublist.class);
            sublist_intent.putExtra("section", titles[11]);
            startActivity(sublist_intent);
            mInterstitialAd.show();

        } else if (id == R.id.privacy_policy) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://firebasestorage.googleapis.com/v0/b/whatsstatus-5128a.appspot.com/o/privacy_policy.html?alt=media&token=8132b42b-410c-43df-a166-4d822dead279")));

        } else if (id == R.id.nav_share) {
            try {
                final String appPackageName = getPackageName();
                Intent abb = new Intent(Intent.ACTION_SEND); // مشاركة تطبيقك
                abb.setType("text/plain");
                abb.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                startActivity(Intent.createChooser(abb, "اختيار:"));
            } catch (Exception e) {
                e.toString();
            }
        } else if (id == R.id.rate_us) {
            final String appPackageName = getPackageName(); // getPackageName() طلبنا اسم الباكيج الخاص للتطبيق من هذا التطبيق, لو أردت تقييم تطبيق اخر ضع اسم الباكيج الخاصة به
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

            return true;

        }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "DroidKufi_Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new com.imagesw.whatsstatus.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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
}

