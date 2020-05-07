package com.gago.sqliteuac.Modelos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gago.sqliteuac.R;

import java.util.ArrayList;

public class ListaAdapter extends ArrayAdapter<Persona> {
    private ArrayList<Persona> lista;
    private Context contexto;
    private int layoutRecurso;

    public ListaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Persona> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.layoutRecurso = resource;
        this.lista = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(contexto).inflate(layoutRecurso, null);
        }

        Persona persona = lista.get(position);

        TextView tvCedula = view.findViewById(R.id.tvCedula);
        TextView tvNombre = view.findViewById(R.id.tvNombre);
        TextView tvEstrato = view.findViewById(R.id.tvEstrato);
        TextView tvSalario = view.findViewById(R.id.tvSalario);
        TextView tvNivel = view.findViewById(R.id.tvNivelEducativo);

        String temp = "";
        tvCedula.setText(String.valueOf(persona.getCedula()));
        tvNombre.setText(persona.getNombre());
        tvEstrato.setText(String.valueOf((persona.getEstrato() + 1)));
        tvSalario.setText(String.valueOf(persona.getSalario()));
        switch (persona.getNivel_educativo()) {
            case 0:
                temp = "Bachillerato";
                break;
            case 1:
                temp = "Pregrado";
                break;
            case 2:
                temp = "Maestria";
                break;
            case 3:
                temp = "Doctorado";
                break;
        }
        tvNivel.setText(temp);

        return view;
    }
}
