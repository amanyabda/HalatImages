package com.abwahmad.whatsstatus;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imagesw.whatsstatus.R;
import com.imagesw.whatsstatus.my_pic_class;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<com.imagesw.whatsstatus.my_pic_class> myPicClassArrayList;

    public CustomPagerAdapter(Context context, ArrayList<my_pic_class> myPicClassArrayList) {
        mContext = context;
        this.myPicClassArrayList = myPicClassArrayList;
    }

    @Override
    public int getCount() {
        return myPicClassArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_image_slider, container,
                false);
        final ImageView imageView = layout.findViewById(R.id.imageView);
        //imageView.setImageResource(myPicClassArrayList.get(position).image);
        Picasso.with(mContext).load(myPicClassArrayList.get(position).image)
                .into(imageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        // progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext).load(myPicClassArrayList.get(position).image)
                                .into(imageView);
                        // progressDialog.dismiss();
                    }
                });

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}