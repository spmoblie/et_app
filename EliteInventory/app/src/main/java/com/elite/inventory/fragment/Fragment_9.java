package com.elite.inventory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elite.inventory.R;

public class Fragment_9 extends Fragment {

    public static final String TAG = "content";
    private View view;
    private TextView textView;
    private String content;

    public static Fragment_9 newInstance(String content) {
        Fragment_9 fragment = new Fragment_9();
        Bundle args = new Bundle();
        args.putString(TAG, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        content = bundle != null ? bundle.getString(TAG) : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_9, container, false);
        init();
        return view;
    }

    private void init() {
        textView = (TextView) view.findViewById(R.id.fragment_9_tv);
        textView.setText(content);
    }

}
