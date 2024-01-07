package com.example.pokedex.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonDetailFragment extends Fragment {

    private JSONObject details;
    public PokemonDetailFragment() {
        // Required empty public constructor
    }

    public static PokemonDetailFragment newInstance(JSONObject pokemonDetails) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        String detailsString = pokemonDetails.toString();
        args.putString("detailsString", detailsString);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            String strPokemonDetails = getArguments().getString("detailsString");
            try {
                if (strPokemonDetails != null)
                {
                    details = new JSONObject(strPokemonDetails);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);
        TextView name = view.findViewById(R.id.singlePokemonName);
        TextView healthPoints = view.findViewById(R.id.healthPoints);
        TextView attack = view.findViewById(R.id.attack);
        TextView defense = view.findViewById(R.id.defense);
        TextView special_attack = view.findViewById(R.id.special_attack);
        TextView special_defense = view.findViewById(R.id.special_defense);
        TextView speed = view.findViewById(R.id.speed);
        TextView ability1 = view.findViewById(R.id.ability1);
        TextView ability2 = view.findViewById(R.id.ability2);
        ImageView img = view.findViewById(R.id.singlePokemonImg);

        name.setText(getPokemonName());
        String aux = getStatName(0) + ": " + getStatValue(0);
        healthPoints.setText(aux);
        aux = getStatName(1) + ": " + getStatValue(1);
        attack.setText(aux);
        aux = getStatName(2) + ": " + getStatValue(2);
        defense.setText(aux);
        aux = getStatName(3) + ": " + getStatValue(3);
        special_attack.setText(aux);
        aux = getStatName(4) + ": " + getStatValue(4);
        special_defense.setText(aux);
        aux = getStatName(5) + ": " + getStatValue(5);
        speed.setText(aux);
        Glide.with(view)
                .load(getImgUrl())
                .into(img);
        ability1.setText(getAbilityName(0));
        ability2.setText(getAbilityName(1));
        return view;
    }

    private String getAbilityName(int index) {
        String result = "";
        try {
            JSONArray abilities = details.getJSONArray("abilities");
            JSONObject ability = abilities.getJSONObject(index);
            JSONObject abilityInfo = ability.getJSONObject("ability");
            result = abilityInfo.getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private String getImgUrl() {
        String result = "";
        try {
            JSONObject sprites = details.getJSONObject("sprites");
            result = sprites.getString("front_default");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private Object getStatValue(int index) {
        String result = "";
        try {
            JSONArray stats = details.getJSONArray("stats");
            JSONObject statDetail = stats.getJSONObject(index);
            result = statDetail.getString("base_stat");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private String getStatName(int index) {
        String result = "";
        try {
            JSONArray stats = details.getJSONArray("stats");
            JSONObject statDetail = stats.getJSONObject(index);
            JSONObject statExtra = statDetail.getJSONObject("stat");
            result = statExtra.getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private String getPokemonName(){
        String name = "";
        try {
            name = details.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }
}