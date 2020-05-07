package com.gago.sqliteuac.BasedDatos;

public class ModeloDB {
    public static final String NOMBRE_DB = "baseddatos";
    public static final String NOMBRE_TABLA = "personas";
    public static final String COL_CEDULA = "cedula";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_ESTRATO = "estrato";
    public static final String COL_SALARIO = "salario";
    public static final String COL_NIVEL_EDUCATIVO = "niveleducativo";

    public static final String CREAR_TABLA_REGISTROS = "CREATE TABLE " +
            "" + NOMBRE_TABLA + " ( " + COL_CEDULA + " INTEGER PRIMARY KEY, " +
            " " + COL_NOMBRE + " TEXT, " + COL_ESTRATO + " INTEGER, " +
            " " + COL_SALARIO + " INTEGER, " + COL_NIVEL_EDUCATIVO + " INTEGER)";
}
