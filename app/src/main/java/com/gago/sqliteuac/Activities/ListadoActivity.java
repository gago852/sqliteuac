package com.gago.sqliteuac.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gago.sqliteuac.BasedDatos.DBControlador;
import com.gago.sqliteuac.Modelos.ListaAdapter;
import com.gago.sqliteuac.Modelos.Persona;
import com.gago.sqliteuac.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListadoActivity extends AppCompatActivity implements LifecycleObserver {

    ListView lista;

    ArrayList<Persona> listaPersona;


    ListaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        lista = findViewById(R.id.listView);

        poblarLista();


        registerForContextMenu(lista);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                poblarLista();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "modificacion cancelada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.listado_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.action_modificar_registro:
                modificarRegistro(menuInfo.position);
                return true;
            case R.id.action_borrar_registro:
                borrarRegistro(menuInfo.position);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void modificarRegistro(int posicion) {
        Intent intent = new Intent(this, ModificarActivity.class);
        intent.putExtra("indice", posicion);
        startActivityForResult(intent, 2);
    }

    private void borrarRegistro(int posicion) {
        BorrarItem borrarItem = new BorrarItem();
        int retorno = 0;
        try {
            retorno = borrarItem.execute(listaPersona.get(posicion)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (retorno == 1) {
            Toast.makeText(getApplicationContext(), "registro eliminado", Toast.LENGTH_SHORT).show();
            poblarLista();
        } else {
            Toast.makeText(getApplicationContext(), "error al borrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void poblarLista() {
        ObtenerItems items = new ObtenerItems();
        ArrayList<Persona> personas = new ArrayList<>();
        try {
            personas = new ArrayList<>(items.execute().get());
            listaPersona = new ArrayList<>(personas);
            if (adapter == null) {
                adapter = new ListaAdapter(getApplicationContext(), R.layout.item_lista_layout, listaPersona);
                lista.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ObtenerItems extends AsyncTask<Void, Void, ArrayList<Persona>> {

        @Override
        protected ArrayList<Persona> doInBackground(Void... voids) {
            DBControlador controlador = DBControlador.getInstance(getApplicationContext());
            return controlador.optenerRegistros();
        }

        @Override
        protected void onPostExecute(ArrayList<Persona> personas) {
            super.onPostExecute(personas);
        }
    }

    private class BorrarItem extends AsyncTask<Persona, Void, Integer> {


        @Override
        protected Integer doInBackground(Persona... personas) {
            DBControlador controlador = DBControlador.getInstance(getApplicationContext());
            return controlador.borrarRegistro(personas[0]);
        }
    }

}
