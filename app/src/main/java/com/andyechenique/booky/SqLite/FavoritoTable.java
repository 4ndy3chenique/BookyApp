package com.andyechenique.booky.SqLite;
public class FavoritoTable {
    public static final String TABLE_NAME = "favorito";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "documento_id TEXT," +
            "titulo TEXT," +
            "fecha_agregado TEXT" +
            ");";
}
