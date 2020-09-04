package com.example.cafeteria;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BebidaBDHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "bdCafe"; // o nome do bd
    private static final int DB_VERSION = 1; // a versão do banco de dados
    // Construtor
    BebidaBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        atualizarBD(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        atualizarBD(db, oldVersion, newVersion);
    }

    private void atualizarBD(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE BEBIDA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NOME TEXT, "
                    + "DESCRICAO TEXT, "
                    + "IMAGEM_RECURSO_ID INTEGER);");
            inserirBebida(db, "Latte",
                    "Latte é uma bebida de café expresso com uma quantidade generosa de espuma de leite no topo.", R.drawable.latte);
            inserirBebida(db, "Cappuccino",
                    "Um cappuccino clássico \" +\n" +
                            "\"e consiste em um terço de café expresso, um terço de leite vaporizado \" +\n" +
                            "\"e um terço de espuma de leite vaporizado.", R.drawable.capuccino);
            inserirBebida(db, "Filter",
                    "Café coado do grão torrado e fresco da mais alta qualidade.", R.drawable.filter);
            }

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE BEBIDA ADD COLUMN FAVORITO NUMERIC;");
        }
    }

    private static void inserirBebida(SQLiteDatabase db, String nome, String descricao, int recursoId) {
        ContentValues bebidaValores = new ContentValues();
        bebidaValores.put("NOME", nome);
        bebidaValores.put("DESCRICAO", descricao);
        bebidaValores.put("IMAGEM_RECURSO_ID", recursoId);
        db.insert("BEBIDA", null, bebidaValores);
    }
}
