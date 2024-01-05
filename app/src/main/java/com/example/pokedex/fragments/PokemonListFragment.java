package com.example.pokedex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.pokedex.CustomAdapter;
import com.example.pokedex.R;
import com.example.pokedex.pokemon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokemonListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokemonListFragment extends Fragment {

    private ArrayList<pokemon> pokemonList;

    public PokemonListFragment() {
        // Required empty public constructor
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
        GridView gridView = view.findViewById(R.id.pokeList);
        if (getArguments() != null){
            pokemonList = getArguments().getParcelableArrayList("pokemonList");
            if (pokemonList != null) pokemonList.sort(Comparator.comparingInt(p -> Integer.parseInt(p.getId())));
        }
        gridView.setAdapter(new CustomAdapter(getActivity(), getIDsList(), getPokemonList(), getPokemonImg(), getTypesList()) {
        });

        return view;
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