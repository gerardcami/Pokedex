package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pokedex.fragments.PokemonDetailFragment;
import com.example.pokedex.fragments.PokemonListFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PokemonListFragment.OnPokemonSelectedListener {
    // ...

    @Override
    public void onPokemonSelected(String pokemonId) {
        getPokemonDetails(pokemonId);
    }

    int limit = 10;
    private final ArrayList<pokemon> data = new ArrayList<>();
    JSONObject singlePokemonDetails;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPokemonData(pokeUrls -> {
            for (String url : pokeUrls) {
                getSinglePokemon(url, result -> {
                    try {
                        String id = result.getString("id");
                        String name = result.getString("name");
                        // Get types from JSON
                        JSONArray auxTypes = result.getJSONArray("types");
                        String[] types = new String[auxTypes.length()];
                        for (int i = 0; i < auxTypes.length(); i++) {
                            JSONObject t = auxTypes.getJSONObject(i);
                            JSONObject n = t.getJSONObject("type");
                            types[i] = n.getString("name");
                        }
                        JSONObject auxImg = result.getJSONObject("sprites");
                        String imgUrl = auxImg.getString("front_default");
                        storeNewData(id, name, types, imgUrl, this::showPokemonListFragment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        tabLayout = findViewById(R.id.tabLayout);
        setupTabLayout();

    }

    private void getPokemonDetails(String id){
        String url = "https://pokeapi.co/api/v2/pokemon/" + id;
        getSinglePokemon(url, new pokemonCallback() {
            @Override
            public void onCallback(JSONObject result) {
                singlePokemonDetails = result;
                showPokemonDetailFragment(singlePokemonDetails);
            }
        });
    }

    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        showPokemonListFragment(data);
                        break;
                    case 1:
                        showPokemonDetailFragment(singlePokemonDetails);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void showPokemonDetailFragment(JSONObject details) {
        PokemonDetailFragment fragment = PokemonDetailFragment.newInstance(details);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPokemonListFragment(ArrayList<pokemon> list) {
        PokemonListFragment fragment = PokemonListFragment.newInstance(list);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void storeNewData(String id, String name, String[] types, String imgUrl, final DataLoadedCallback dataLoadedCallback) {
        data.add(new pokemon(id, name, types, imgUrl));
        if (data.size() == limit){
            dataLoadedCallback.onDataLoaded(data);
        }
    }

    public interface DataLoadedCallback {
        void onDataLoaded(ArrayList<pokemon> data);
    }


    public interface pokemonCallback {
        void onCallback(JSONObject result);
    }

    public interface pokemonDataCallback {
        void onCallback(String[] pokeUrls);
    }

    public void getPokemonData(final pokemonDataCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://pokeapi.co/api/v2/pokemon?limit="+limit+"&offset=0", null, response -> {
            try {
                JSONArray results = response.getJSONArray("results");
                String[] pokeUrls = new String[results.length()];
                for (int i = 0; i < results.length(); i++){
                    JSONObject p = results.getJSONObject(i);
                    String url = p.getString("url");
                    pokeUrls[i] = url;
                }
                callback.onCallback(pokeUrls);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.d("Error", R.string.requestErr + error.toString()));
        queue.add(request);
    }

    public void getSinglePokemon(String url, final pokemonCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, callback::onCallback, error -> Log.d("Error", R.string.requestErr + error.toString()));
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.goBack) finish();
        else if (item.getItemId() == R.id.aboutMenu) startActivity(new Intent(this, About.class));
        else if (item.getItemId() == R.id.action_settings) startActivity(new Intent(this, Settings.class));

        return true;
    }
}

