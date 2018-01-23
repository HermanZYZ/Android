package com.example.zyz.lab9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ZYZ on 2017/12/21.
 */

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.mViewHolder> {
    private List<Github> users;
    private Context context;
    private OnItemClickListener OnItemClickListener = null;

    public CommonAdapter(List<Github> users, Context context)
    {
        this.users = users;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        mViewHolder viewHolder = new mViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final mViewHolder viewHolder, final int position)
    {
        viewHolder.title.setText(users.get(position).getLogin());
        viewHolder.subtitle.setText("id:" + String.valueOf(users.get(position).getId_()));
        viewHolder.subsubtitle.setText("blog:" + users.get(position).getBlog());

        if(OnItemClickListener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    OnItemClickListener.onClick(position);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView subtitle;
        TextView subsubtitle;

        public mViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            subsubtitle = (TextView) view.findViewById(R.id.subsubtitle);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.OnItemClickListener = onItemClickListener;
    }
}
