package com.example.mangareader.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mangareader.R;
import com.example.mangareader.adapter.MangaFeaturesAdapter;
import com.example.mangareader.databinding.FragmentSearchBinding;
import com.example.mangareader.domain.model.MangaModel;
import com.example.mangareader.view.Manga;
import com.example.mangareader.viewModel.GetMangaViewModel;

import java.util.ArrayList;
import java.util.List;


public class Search extends Fragment implements MangaFeaturesAdapter.OnClick {

    private List<MangaModel> searchMangaList = new ArrayList<>();
    private FragmentSearchBinding binding;
    private GetMangaViewModel mangaViewModel;
    private MangaFeaturesAdapter mangaSearchAdapter;
    private String search = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mangaViewModel = new ViewModelProvider(getActivity()).get(GetMangaViewModel.class);

        binding.rvSearchManga.setLayoutManager( new GridLayoutManager(view.getContext(), 2));
        binding.rvSearchManga.setHasFixedSize(true);
        mangaSearchAdapter = new MangaFeaturesAdapter(searchMangaList, this);
        binding.rvSearchManga.setAdapter(mangaSearchAdapter);

        mangaViewModel.search.observe(getViewLifecycleOwner(), sucess -> {
            if (sucess) {
                searchMangaList = mangaViewModel.getSearchList();
                binding.rvSearchManga.setLayoutManager( new GridLayoutManager(view.getContext(), 2));
                binding.rvSearchManga.setHasFixedSize(true);
                mangaSearchAdapter = new MangaFeaturesAdapter(searchMangaList, this);
                binding.rvSearchManga.setAdapter(mangaSearchAdapter);
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                search = c.toString();
                mangaViewModel.searchManga(search);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.ivClose.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        });

        return view;
    }

    @Override
    public void OnClickListener(MangaModel manga) {
        Intent intent = new Intent(getActivity(), Manga.class);
        intent.putExtra("manga", manga);
        startActivity(intent);
    }

}