package com.jiuzhang.guojing.dribbbo.view.bucket_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.model.Bucket;
import com.jiuzhang.guojing.dribbbo.view.base.BaseViewHolder;
import com.jiuzhang.guojing.dribbbo.view.base.InfiniteAdapter;
import com.jiuzhang.guojing.dribbbo.view.shot_list.ShotListFragment;

import java.util.List;

public class BucketListAdapter extends InfiniteAdapter<Bucket> {

    public BucketListAdapter(@NonNull Context context,
                             @NonNull List<Bucket> data,
                             @NonNull LoadMoreListener loadMoreListener) {
        super(context, data, loadMoreListener);
    }

    @Override
    protected BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.list_item_bucket, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(BaseViewHolder holder, int position) {
        final Bucket bucket = getData().get(position);
        final BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;

        bucketViewHolder.bucketName.setText(bucket.name);
        bucketViewHolder.bucketCount.setText(formatShotCount(bucket.shots_count));
        bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BucketShotListActivity.class);
                intent.putExtra(ShotListFragment.KEY_BUCKET_ID, bucket.id);
                intent.putExtra(BucketShotListActivity.KEY_BUCKET_NAME, bucket.name);
                getContext().startActivity(intent);
            }
        });
    }

    private String formatShotCount(int shotCount) {
        return shotCount == 0
                ? getContext().getString(R.string.shot_count_single, shotCount)
                : getContext().getString(R.string.shot_count_plural, shotCount);
    }
}
