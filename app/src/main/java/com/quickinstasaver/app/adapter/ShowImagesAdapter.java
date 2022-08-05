package com.quickinstasaver.app.adapter;


import static com.quickinstasaver.app.util.Utils.shareImage;
import static com.quickinstasaver.app.util.Utils.shareVideo;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.quickinstasaver.app.FullViewActivity;
import com.quickinstasaver.app.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class ShowImagesAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<File> imageList;
    private final LayoutInflater inflater;
    FullViewActivity fullViewActivity;

    public ShowImagesAdapter(Context context, ArrayList<File> imageList, FullViewActivity fullViewActivity) {
        this.context = context;
        this.imageList = imageList;
        this.fullViewActivity = fullViewActivity;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.im_fullViewImage);
        final ImageView im_vpPlay = imageLayout.findViewById(R.id.im_vpPlay);
        final ImageView im_share = imageLayout.findViewById(R.id.im_share);
        final ImageView im_delete = imageLayout.findViewById(R.id.im_delete);
        VideoView videoView = imageLayout.findViewById(R.id.videoview);


        Glide.with(context).load(imageList.get(position).getPath()).into(imageView);
        view.addView(imageLayout, 0);
        String extension = imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf("."));
        if (extension.equals(".mp4")) {
            im_vpPlay.setVisibility(View.VISIBLE);
        } else {
            im_vpPlay.setVisibility(View.GONE);
        }

        im_vpPlay.setOnClickListener(v -> {
            imageView.setVisibility(View.GONE);
            im_vpPlay.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setVideoPath(imageList.get(position).getPath());

            videoView.start();
        });

        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = imageList.get(position).delete();
                if (b) {
                    fullViewActivity.deleteFileAA(position);
                }
            }
        });

        im_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String extension = imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf("."));
                if (extension.equals(".mp4")) {
                    shareVideo(context, imageList.get(position).getPath());
                } else {
                    shareImage(context, imageList.get(position).getPath());
                }
            }
        });


        return imageLayout;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}