package com.github.leejay.customgridlayout.sample;

import com.github.leejay.customgridlayout.GridLayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

/**
 * Created by leejay on 15/02/02.
 */
public class GridLayoutActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridlayout);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridlayout);

        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.img1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.GridLayoutParams params1 = new GridLayout.GridLayoutParams(2, 1);
        gridLayout.addView(imageView1, params1);

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.img2);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.GridLayoutParams params2 = new GridLayout.GridLayoutParams(1, 2);
        gridLayout.addView(imageView2, params2);


        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.img3);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.GridLayoutParams params3 = new GridLayout.GridLayoutParams(1, 1);
        gridLayout.addView(imageView3, params3);

        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.drawable.img3);
        imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.GridLayoutParams params4 = new GridLayout.GridLayoutParams(1, 1);
        gridLayout.addView(imageView4, params4);

    }
}