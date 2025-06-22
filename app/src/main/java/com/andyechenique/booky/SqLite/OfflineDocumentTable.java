package com.andyechenique.booky.SqLite;

public class OfflineDocumentTable {
    public static final String TABLE_NAME = "offline_documento";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "documento_id TEXT," +
            "titulo TEXT," +
            "contenido TEXT" +
            ");";
}
