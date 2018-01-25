package com.technophile.lazyloading;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Technophile on 1/24/2018.
 */

class RvImageGridADP extends RecyclerView.Adapter<RvImageGridADP.VH> {
    private Context context;
    private boolean isLoading, canLoadMore = true, preventLoadMore = false;
    private int visibleThreshold = 5;
    private int lastVisibleItem,
            totalItemCount;

    private ArrayList<FlickrModel.Photo> photosArray = new ArrayList<FlickrModel.Photo>();

    public RvImageGridADP(Context context, RecyclerView recyclerView, final OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = gridLayoutManager.getItemCount();
                if (!preventLoadMore) {
                    if (canLoadMore) {
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                        }
                    }
                }

            }
        });
    }

    public void addData(ArrayList<FlickrModel.Photo> photo) {
        if (photo != null) {
            photosArray = photo;
            notifyDataSetChanged();
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(View.inflate(context, R.layout.adp_rv_img_grid, null));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tv_img_title.setText(photosArray.get(position).getTitle());
        Picasso.with(context).load(photosArray.get(position).getImgUrl()).into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return photosArray.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoadNoMore() {
        canLoadMore = false;
    }

    public void preventLoading(boolean b) {
        preventLoadMore = b;
    }

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    public class VH extends RecyclerView.ViewHolder {
        CustomImageView iv_img;
        TextView tv_img_title;

        public VH(View itemView) {
            super(itemView);
            iv_img = (CustomImageView) itemView.findViewById(R.id.iv_img);
            tv_img_title = (TextView) itemView.findViewById(R.id.tv_img_title);
        }
    }
}
