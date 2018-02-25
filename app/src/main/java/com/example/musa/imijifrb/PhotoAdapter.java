package com.example.musa.imijifrb;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by musam on 18/02/2018.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<Picture> pictureList;
    private Context context;

    public PhotoAdapter(List<Picture> pictureList, Context context) {
        this.pictureList = pictureList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_preview,   parent, false);

        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picture picture = pictureList.get(position);

        holder.title.setText(picture.getTitle());
        Picasso.with(context)
                .load(picture.getUrl())
                .fit()
                .centerCrop()
                .into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.photo_title);
            photo = (ImageView) itemView.findViewById(R.id.photo);

        }
    }
}
