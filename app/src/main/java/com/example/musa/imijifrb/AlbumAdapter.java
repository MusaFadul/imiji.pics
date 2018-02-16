package com.example.musa.imijifrb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by musam on 16/02/2018.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumDataSource> albumDataSourceList;
    private Context context;

    public AlbumAdapter(List<AlbumDataSource> albumDataSourceList, Context context) {
        this.albumDataSourceList = albumDataSourceList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_album,   parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        AlbumDataSource album = albumDataSourceList.get(position);

        holder.albumTitle.setText(album.getAlbumTitle());
        holder.albumDescription.setText(album.getAlbumDescription());
        holder.albumOwner.setText(album.getAlbumOwner());

    }

    @Override
    public int getItemCount() {
        return albumDataSourceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView albumTitle;
        TextView albumDescription;
        TextView albumOwner;

        public ViewHolder(View itemView) {

            super(itemView);

            albumTitle = (TextView) itemView.findViewById(R.id.albumTitle);
            albumDescription = (TextView) itemView.findViewById(R.id.albumDescription);
            albumOwner = (TextView) itemView.findViewById(R.id.albumOwner);
        }
    }
}
