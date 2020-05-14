package com.gago.sqliteuac.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gago.sqliteuac.BasedDatos.DBControlador;
import com.gago.sqliteuac.Modelos.Persona;
import com.gago.sqliteuac.R;

import java.util.concurrent.ExecutionException;

public class BuscarActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edCedula;

    Button btBuscar, btRegresar;

    TextView tvCedula, tvNombre, tvEstrato, tvSalario, tvNivel;
    int cedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        edCedula = findViewById(R.id.edCedula);
        btBuscar = findViewById(R.id.btBuscar);
        btRegresar = findViewById(R.id.btRegresar);
        tvCedula = findViewById(R.id.tvCedula);
        tvNombre = findViewById(R.id.tvNombre);
        tvEstrato = findViewById(R.id.tvEstrato);
        tvSalario = findViewById(R.id.tvSalario);
        tvNivel = findViewById(R.id.tvNivelEducativo);


        btBuscar.setOnClickListener(this);
        btRegresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBuscar:
                Persona persona = null;
                try {
                    cedula = Integer.parseInt(edCedula.getText().toString());
                    BuscarItem buscarItem = new BuscarItem();
                    persona = buscarItem.execute(cedula).get();
                } catch (NumberFormatException numEx) {
                    Toast.makeText(getApplicationContext(), "numero muy grande", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error a buscar", Toast.LENGTH_SHORT).show();
                }

                if (persona != null) {
                    tvCedula.setText(String.valueOf(cedula));
                    tvNombre.setText(persona.getNombre());
                    tvEstrato.setText(String.valueOf(persona.getEstrato()));
                    tvSalario.setText(String.valueOf(persona.getSalario()));
                    switch (persona.getNivel_educativo()) {
                        case 0:
                            tvNivel.setText(getString(R.string.educativo_bachillerato));
                            break;
                        case 1:
                            tvNivel.setText(getString(R.string.educativo_pregado));
                            break;
                        case 2:
                            tvNivel.setText(getString(R.string.educativo_maestro));
                            break;
                        case 3:
                            tvNivel.setText(getString(R.string.educativo_doctorado));
                            break;
                    }
                } else {
                    tvCedula.setText(getString(R.string.invalido));
                    tvNombre.setText(getString(R.string.invalido));
                    tvEstrato.setText(getString(R.string.invalido));
                    tvSalario.setText(getString(R.string.invalido));
                    tvNivel.setText(getString(R.string.invalido));
                    Toast.makeText(getApplicationContext(), "no encontrado", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btRegresar:
                finish();
                break;
        }
    }

    private class BuscarItem extends AsyncTask<Integer, Void, Persona> {

        @Override
        protected Persona doInBackground(Integer... integers) {
            DBControlador controlador = new DBControlador(getApplicationContext());
            return controlador.buscarPersona(integers[0]);
        }
    }
}
