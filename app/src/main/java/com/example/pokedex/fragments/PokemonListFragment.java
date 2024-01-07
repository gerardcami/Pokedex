package com.example.pokedex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.pokedex.CustomAdapter;
import com.example.pokedex.MainActivity;
import com.example.pokedex.R;
import com.example.pokedex.pokemon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PokemonListFragment extends Fragment {
    GridView gridView;
    private ArrayList<pokemon> pokemonList;
    private OnPokemonSelectedListener mListener;


    public PokemonListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPokemonSelectedListener) {
            mListener = (OnPokemonSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar OnPokemonSelectedListener");
        }
    }

    public static PokemonListFragment newInstance(ArrayList<pokemon> list) {
        PokemonListFragment fragment = new PokemonListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("pokemonList", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);
        gridView = view.findViewById(R.id.pokeList);
        if (getArguments() != null){
            pokemonList = getArguments().getParcelableArrayList("pokemonList");
            if (pokemonList != null) pokemonList.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        }
        gridView.setAdapter(new CustomAdapter(getActivity(), getIDsList(), getPokemonList(), getPokemonImg(), getTypesList()) {
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pokemonId = pokemonList.get(position).getId();
                mListener.onPokemonSelected(pokemonId);
            }
        });
    }

    public interface OnPokemonSelectedListener {
        void onPokemonSelected(String id);
    }

    private String[] getIDsList() {
        String[] idList = new String[1000];
        int i = 0;
        for (pokemon p : pokemonList){
            idList[i] = p.getId();
            i++;
        }
        return idList;
    }

    private String[] getPokemonList() {
        String[] list = new String[1000];
        int i = 0;
        for (pokemon p : pokemonList){
            list[i] = p.getName();
            i++;
        }
        return list;
    }

    private String[] getPokemonImg() {
        String[] imgList = new String[1000];
        int i = 0;
        for (pokemon p : pokemonList){
            imgList[i] = p.getImgUrl();
            i++;
        }
        return imgList;
    }

    private List<String[]> getTypesList(){
        List<String[]> typesList = new ArrayList<>();
        for (pokemon p : pokemonList){
            typesList.add(p.getTypes());
        }
        return typesList;
    }
}