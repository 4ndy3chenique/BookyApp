package com.andyechenique.booky.SqLite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andyechenique.booky.actividades.CargaActivity;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "booky_local.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(CargaActivity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoritoTable.CREATE_TABLE);
        db.execSQL(HistorialTable.CREATE_TABLE);
        db.execSQL(DescargasTable.CREATE_TABLE);
        db.execSQL(OfflineDocumentTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorito");
        db.execSQL("DROP TABLE IF EXISTS historial");
        db.execSQL("DROP TABLE IF EXISTS descargas");
        db.execSQL("DROP TABLE IF EXISTS offline_documento");
        onCreate(db);
    }
}
