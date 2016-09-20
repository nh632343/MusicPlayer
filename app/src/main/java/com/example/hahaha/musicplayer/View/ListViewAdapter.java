package com.example.hahaha.musicplayer.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.hahaha.musicplayer.Info.SongInfo;
import com.example.hahaha.musicplayer.R;
import com.example.hahaha.musicplayer.SelfMadeVIew.ScrollTextView;

import java.util.List;

/**
 * Created by hahaha on 9/15/16.
 */
public class ListViewAdapter extends ArrayAdapter<SongInfo> {
    private int resid;

    public ListViewAdapter(Context context, int resource, List<SongInfo> objects) {
        super(context, resource, objects);
        resid=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongInfo songInfo=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resid,null);
        ScrollTextView scrollTextView= (ScrollTextView) view.findViewById(R.id.item_tv);
        scrollTextView.setText(songInfo.name);
        return view;
    }
}
