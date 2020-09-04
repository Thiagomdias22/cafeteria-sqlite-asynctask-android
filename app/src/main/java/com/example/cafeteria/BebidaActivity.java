package com.example.cafeteria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BebidaActivity extends AppCompatActivity {

    private int indiceBebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebida);
        //Pega a bebida da intent
        Bundle args = getIntent().getExtras();
        indiceBebida = args.getInt("id");
        //Cria um cursor
        try {
            SQLiteOpenHelper bebidaBDHelper = new BebidaBDHelper(this);
            SQLiteDatabase db = bebidaBDHelper.getReadableDatabase();
            Cursor cursor = db.query("BEBIDA",
                    new String[]{"NOME", "DESCRICAO",
                            "IMAGEM_RECURSO_ID", "FAVORITO"},
                    "_id = ?", // busca pela chave primária
                    new String[]{Integer.toString(indiceBebida)},
                    null, null, null);
            //Move para o primeiro registro do Cursor
            //(só existe um registro no cursor)
            if (cursor.moveToFirst()) {
                //Pega os detalhes das bebidas do cursor
                String nomeText = cursor.getString(0);
                String descricaoText = cursor.getString(1);
                int fotoId = cursor.getInt(2);
                // 1 se o checkbox está marcado, 0 caso contrário
                boolean isFavorito = (cursor.getInt(3) == 1);

                //Seta o nome da bebida
                TextView nome = (TextView) findViewById(R.id.name);
                nome.setText(nomeText);

                //Seta a descrição da bebida
                TextView descricao = (TextView) findViewById(R.id.description);
                descricao.setText(descricaoText);

                //Seta a imagem da bebida
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(fotoId);
                photo.setContentDescription(nomeText);

                //Populate the favorite checkbox
                CheckBox favorito = (CheckBox) findViewById(R.id.favorito);
                favorito.setChecked(isFavorito);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Banco de dados indisponível", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Atualiza o banco de dados quando o checkbox é clicado
    public void onFavoritoClicked(View view){
    // indice da bebida clicada é um atributo global, atualizado no onCreate()
        new UpdateBebidaTarefa().execute(indiceBebida);
    }
    //Inner class para atualizar a bebida
    private class UpdateBebidaTarefa extends AsyncTask<Integer, Void, Boolean> {
        ContentValues bebidaValores;
        protected void onPreExecute() {
            CheckBox favorito = (CheckBox)findViewById(R.id.favorito);
            bebidaValores = new ContentValues();
            bebidaValores.put("FAVORITO", favorito.isChecked());
        }
        protected Boolean doInBackground(Integer... bebidas) {
            int drinkNo = bebidas[0];
            SQLiteOpenHelper bebidaBDHelper = new BebidaBDHelper(BebidaActivity.this);
            try {
                SQLiteDatabase db = bebidaBDHelper.getWritableDatabase();
                db.update("BEBIDA", bebidaValores,
                        "_id = ?", new String[] {Integer.toString(drinkNo)});
                db.close();
                return true;
            } catch(SQLiteException e) {
                return false;
            }
        }
        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(BebidaActivity.this,
                        "Banco de dados indisponível:", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    } // fim da classe inner private
}