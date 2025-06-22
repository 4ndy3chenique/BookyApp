package com.andyechenique.booky.SqLite;

public class HistorialTable {
    public static final String TABLE_NAME = "historial";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "documento_id TEXT," +
            "titulo TEXT," +
            "fecha_visto TEXT" +
            ");";
}