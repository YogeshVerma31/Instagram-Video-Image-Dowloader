package com.quickinstasaver.app.fragment;

import static androidx.databinding.DataBindingUtil.inflate;

import static com.quickinstasaver.app.util.Utils.RootDirectoryInstaShow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickinstasaver.app.FullViewActivity;
import com.quickinstasaver.app.R;
import com.quickinstasaver.app.adapter.FileListAdapter;
import com.quickinstasaver.app.databinding.FragmentAllBinding;
import com.quickinstasaver.app.interfaces.FileListClickInterface;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;


public class AllFragment extends Fragment implements FileListClickInterface {
    private FragmentAllBinding binding;
    private FileListAdapter fileListAdapter;
    private ArrayList<File> fileArrayList;
    private Activity activity;
    private String type;


    public AllFragment(String type) {
     this.type = type;
    }

    @Override
    public void onAttach(@NotNull Context _context) {
        super.onAttach(_context);
        activity = (Activity) _context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, R.layout.fragment_all, container, false);
        initViews();
        return binding.getRoot();
    }
    private void initViews(){

        getAllFiles();
        binding.swiperefresh.setOnRefreshListener(() -> {
            getAllFiles();
            binding.swiperefresh.setRefreshing(false);
        });
    }

    private void getAllFiles(){
        fileArrayList = new ArrayList<>();
        Log.d( "getAllFiles: ",RootDirectoryInstaShow.toString());
        File[] files = RootDirectoryInstaShow.listFiles();
        if (files!=null) {
            for (File file : files) {
                if (type.equals("VIDEO")) {
                    if ((file.getName().substring(file.getName().lastIndexOf(".")).equals(".mp4"))) {
                        fileArrayList.add(file);
                    }
                }else if (type.equals("IMAGE")){
                        if (!(file.getName().substring(file.getName().lastIndexOf(".")).equals(".mp4"))) {
                            Log.d("getAllFiles: ", file.getAbsolutePath());
                            fileArrayList.add(file);
                        }
                    }else{
                        fileArrayList.add(file);
                    }
                }
            }
            fileListAdapter = new FileListAdapter(activity, fileArrayList, AllFragment.this);
            binding.rvFileList.setAdapter(fileListAdapter);

    }
    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(activity, FullViewActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra("Position", position);
        activity.startActivity(inNext);
    }
}