package com.elite.inventory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elite.inventory.R;
import com.elite.inventory.dialog.DialogManager;

public class Fragment_2 extends Fragment {

    public static final String TAG = "content";
    private View view;
    private TextView textView;
    private String content;
    private DialogManager dm;

    public static Fragment_2 newInstance(String content) {
        Fragment_2 fragment = new Fragment_2();
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
        dm = DialogManager.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);
        init();
        return view;
    }

    private void init() {
        textView = (TextView) view.findViewById(R.id.fragment_2_tv);
        textView.setText(content);
    }

}
