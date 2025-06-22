package com.andyechenique.booky.SqLite;

public class DescargasTable {
    public static final String TABLE_NAME = "descargas";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "documento_id TEXT," +
            "titulo TEXT," +
            "ruta_local TEXT," +
            "fecha_descarga TEXT" +
            ");";
}
