package com.example.jigsawpuzzle.ui.main;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/* 加载图片的 Fragment */
public class PictureFragment extends Fragment {

    private int position;
    private Context mContext;

    public PictureFragment(int position, Context context)
    {
        this.position = position;
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState)
    {
        AssetManager assetManager = mContext.getAssets();
        String backName = "back" + (position + 1) + ".jpg";
        InputStream assetInputStream = null;
        try {
            assetInputStream = assetManager.open(backName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将输入流解码为位图
        Bitmap bitmap = BitmapFactory.decodeStream(assetInputStream);
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageBitmap(bitmap);

        return imageView;
    }
}

