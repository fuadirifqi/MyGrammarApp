package com.fuadirifqi.android.mygrammarapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by fuadirifqi on 5/19/16.
 */
public class ImageThumbnailFragment extends DialogFragment {

    private static final String ARG_PATH = "path";
    private Context mContext;

    private ImageView mImageView;

    public static ImageThumbnailFragment newInstance(File photoFile){

        Bundle args = new Bundle();
        args.putSerializable(ARG_PATH, photoFile);

        ImageThumbnailFragment fragment = new ImageThumbnailFragment();

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_thumbnail_image, null);

        mImageView = (ImageView)v.findViewById(R.id.dialog_thumbnail_image_imageview);

        File photoFile = (File) getArguments().getSerializable(ARG_PATH);

        Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
        mImageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.image_thumbnail_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
