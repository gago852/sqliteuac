package com.gago.sqliteuac.BasedDatos;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.DeleteItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.PutItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.ScanOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.DynamoDBEntry;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.gago.sqliteuac.Modelos.Persona;

import java.util.ArrayList;

public class DBControlador {
    private static final String COGNITO_POOL_ID = "test";
    private static final Regions MY_REGION = Regions.US_EAST_2;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;
    private Context context;
    private final String DYNAMODB_TABLE = "Personas";
    CognitoCachingCredentialsProvider credentialsProvider;
    private static volatile DBControlador instancia;


    public DBControlador(Context context) {
        this.context = context;
        credentialsProvider = new CognitoCachingCredentialsProvider(context, COGNITO_POOL_ID, MY_REGION);
        dbClient = new AmazonDynamoDBClient(credentialsProvider);
        dbClient.setRegion(Region.getRegion(Regions.US_EAST_2));
        dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
    }

    public static synchronized DBControlador getInstance(Context context) {
        if (instancia == null) {
            instancia = new DBControlador(context);
        }
        return instancia;
    }

    public long agregarRegistro(Persona persona) {
        try {
            Document document = new Document();
            document.put("Cedula", persona.getCedula());
            document.put("Nombre", persona.getNombre());
            document.put("Estrato", persona.getEstrato());
            document.put("Salario", persona.getSalario());
            document.put("NivelEducativo", persona.getNivel_educativo());

            PutItemOperationConfig putItemOperationConfig = new PutItemOperationConfig();
            putItemOperationConfig.withReturnValues(ReturnValue.ALL_OLD);

            dbTable.putItem(document, putItemOperationConfig);
            return 1L;
        } catch (Exception e) {
            return -1L;
        }
    }

    public int actualizarRegistro(Persona persona) {
        try {
            Document document = new Document();
            document.put("Cedula", persona.getCedula());
            document.put("Nombre", persona.getNombre());
            document.put("Estrato", persona.getEstrato());
            document.put("Salario", persona.getSalario());
            document.put("NivelEducativo", persona.getNivel_educativo());
            PutItemOperationConfig putItemOperationConfig = new PutItemOperationConfig();
            putItemOperationConfig.withReturnValues(ReturnValue.ALL_OLD);
            Document retorno = dbTable.putItem(document, putItemOperationConfig);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int borrarRegistro(Persona persona) {
        try {
            dbTable.deleteItem(new Primitive(persona.getCedula()), new DeleteItemOperationConfig().withReturnValues(ReturnValue.ALL_OLD));
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public Persona buscarPersona(int cedula) {

        Document resultado = dbTable.getItem(new Primitive(cedula));

        if (resultado == null) {
            return null;
        }
        DynamoDBEntry nombre = resultado.get("Nombre");
        DynamoDBEntry estrato = resultado.get("Estrato");
        DynamoDBEntry salario = resultado.get("Salario");
        DynamoDBEntry nivelEducativo = resultado.get("NivelEducativo");
        Persona persona = new Persona(cedula, nombre.asString()
                , estrato.asNumber().intValue(), salario.asNumber().intValue(), nivelEducativo.asNumber().intValue());
        return persona;
    }

    public ArrayList<Persona> optenerRegistros() {
        ScanOperationConfig scanOperationConfig = new ScanOperationConfig();
        ArrayList<String> atributos = new ArrayList<>();
        atributos.add("Cedula");
        atributos.add("Estrato");
        atributos.add("NivelEducativo");
        atributos.add("Nombre");
        atributos.add("Salario");
        scanOperationConfig.withAttributesToGet(atributos);
        ArrayList<Document> documents = (ArrayList<Document>) dbTable.scan(scanOperationConfig).getAllResults();

        if (documents == null) {
            return new ArrayList<>();
        }

        ArrayList<Persona> personas = new ArrayList<>();
        for (Document document : documents) {
            DynamoDBEntry cedula = document.get("Cedula");
            DynamoDBEntry nombre = document.get("Nombre");
            DynamoDBEntry estrato = document.get("Estrato");
            DynamoDBEntry salario = document.get("Salario");
            DynamoDBEntry nivelEducativo = document.get("NivelEducativo");
            Persona persona = new Persona(cedula.asNumber().intValue(), nombre.asString()
                    , estrato.asNumber().intValue(), salario.asNumber().intValue(), nivelEducativo.asNumber().intValue());
            personas.add(persona);
        }

        return personas;
    }
}
