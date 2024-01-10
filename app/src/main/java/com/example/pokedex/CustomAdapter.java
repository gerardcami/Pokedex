package com.example.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    private final String[] pokemonList;
    private final String[] pokemonIDs;
    private final String[] imgUrls;
    private final List<String[]> typesList;
    LayoutInflater inflater;

    public CustomAdapter(Context ctx, String[] idList, String[] list, String[] urls, List<String[]> typesList){
        this.context = ctx;
        this.pokemonList = list;
        this.pokemonIDs = idList;
        this.imgUrls = urls;
        this.typesList = typesList;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        int notnull = 0;
        for (String s : pokemonList) {
            if (s != null) {
                notnull++;
            }
        }
        return notnull;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_custom_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(convertView)
                .load(imgUrls[position])
                .into(holder.img);

        String strNum = pokemonIDs[position];
        int num = Integer.parseInt(strNum);
        String formattedNum = String.format("%04d", num);
        String idText = "N.º " + formattedNum;
        TextView pokemonID = holder.id;
        pokemonID.setText(idText);

        TextView pokemonName = holder.name;
        pokemonName.setText(capitalize(pokemonList[position]));


        StringBuilder typeString = new StringBuilder();
        String[] singlePokemonTypes = typesList.get(position);
        for (int i = 0; i < singlePokemonTypes.length; i++){
            typeString.append(singlePokemonTypes[i]);
            if (i != singlePokemonTypes.length - 1) {
                typeString.append(", ");
            }
        }
        TextView types = holder.types;
        types.setText(capitalize(typeString.toString()));

        return convertView;
    }

    public String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        } else {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
    }
    private static class ViewHolder {
        public TextView name;
        public ImageView img;
        public TextView id;
        public TextView types;
        public ViewHolder(View view) {
            name = view.findViewById(R.id.pokemonName);
            img = view.findViewById(R.id.pokemonImg);
            id = view.findViewById(R.id.pokemonID);
            types = view.findViewById(R.id.pokemonTypes);
        }
    }
}

