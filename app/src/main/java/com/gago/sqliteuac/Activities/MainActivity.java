package com.gago.sqliteuac.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gago.sqliteuac.BasedDatos.DBControlador;
import com.gago.sqliteuac.Modelos.Persona;
import com.gago.sqliteuac.R;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edCedula, edNombre, edSalario;
    Spinner spEstrato, spNivel;

    Button btGuardar, btCancelar;

    int estrato, nivelEducativo;

    private String TAG = "DynamoDb_Demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edCedula = findViewById(R.id.edCedula);
        edNombre = findViewById(R.id.edNombre);
        edSalario = findViewById(R.id.edSalario);
        spEstrato = findViewById(R.id.spEstrato);
        spNivel = findViewById(R.id.spNivelEducativo);
        btGuardar = findViewById(R.id.btGuardar);
        btCancelar = findViewById(R.id.btCancelar);



        ArrayAdapter<CharSequence> adapterEstrato = ArrayAdapter.createFromResource(this
                , R.array.spinner_estrato, R.layout.support_simple_spinner_dropdown_item);
        spEstrato.setAdapter(adapterEstrato);

        spEstrato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estrato = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapterNivel = ArrayAdapter.createFromResource(this
                , R.array.spinner_nivel_educativo, R.layout.support_simple_spinner_dropdown_item);

        spNivel.setAdapter(adapterNivel);

        spNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nivelEducativo = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btGuardar.setOnClickListener(this);
        btCancelar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btGuardar:
                try {
                    int cedula = edCedula.getText().toString().isEmpty() ? 0 : Integer.parseInt(edCedula.getText().toString());
                    int salario = edSalario.getText().toString().isEmpty() ? 0 : Integer.parseInt(edSalario.getText().toString());
                    Persona persona = new Persona(cedula, edNombre.getText().toString(), estrato, salario, nivelEducativo);
                    CrearItemAsyncTask crearAsync = new CrearItemAsyncTask();

                    long retorno = crearAsync.execute(persona).get();
                    if (retorno != -1) {
                        Toast.makeText(v.getContext(), "registro guardado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "registro fallido", Toast.LENGTH_SHORT).show();
                    }
                    limpiarCampo();
                } catch (NumberFormatException numEx) {
                    Toast.makeText(getApplicationContext(), "numero muy grande", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), "registro fallido error en el hilo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btCancelar:
                limpiarCampo();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_buscar_registro:
                Intent intent1 = new Intent(this, BuscarActivity.class);
                startActivity(intent1);
                return true;
            case R.id.action_listado:
                Intent intent = new Intent(this, ListadoActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void limpiarCampo() {
        edCedula.setText("");
        edNombre.setText("");
        edSalario.setText("");
    }

    private class CrearItemAsyncTask extends AsyncTask<Persona, Void, Long> {

        @Override
        protected Long doInBackground(Persona... personas) {
            DBControlador controlador = DBControlador.getInstance(getApplicationContext());
            return controlador.agregarRegistro(personas[0]);
        }


    }


}
