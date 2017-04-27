package com.example.hahaha.musicplayer.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.hahaha.musicplayer.model.entity.Song;
import com.example.hahaha.musicplayer.widget.ScrollTextView;

import java.util.List;

/**
 * Created by hahaha on 9/15/16.
 */
public class ListViewAdapter extends ArrayAdapter<Song> {
    private int resid;

    public ListViewAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        resid=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song =getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resid,null);
        ScrollTextView scrollTextView= null;
        scrollTextView.setText(song.name);
        return view;
    }
}
