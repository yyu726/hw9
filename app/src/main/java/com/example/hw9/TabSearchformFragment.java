package com.example.hw9;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw9.databinding.FragmentSearchformBinding;

public class TabSearchformFragment extends Fragment {
    private SearchForm searchform = new SearchForm();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSearchformBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_searchform, container, false);
        View view = binding.getRoot();
        //here data must be an instance of the class MarsDataProvider

        binding.setSearchform(searchform);
        //return inflater.inflate(R.layout.fragment_searchform, container, false);
        return view;
    }

    public SearchForm getSearchForm() {
        return searchform;
    }

}
