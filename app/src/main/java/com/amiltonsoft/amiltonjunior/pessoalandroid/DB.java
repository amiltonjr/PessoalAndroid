package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.Context;
import android.content.ContentValues;

public final class DB {

    // Atributos da classe
    public Context context;
    private PostDbHelper mDbHelper;
    private SQLiteDatabase db;
    public String ASC               = "ASC";
    public String DESC              = "DESC";
    public String LESS              = "<";
    public String LESSOREQUALS      = "<=";
    public String EQUALS            = "=";
    public String GREATHEROREQUALS  = ">=";
    public String GREATHER          = ">";

    // Método construtor
    public DB(Context context) {
        this.context    = context;
        mDbHelper       = new PostDbHelper(context);
        db              = mDbHelper.getWritableDatabase();
    }

    // Método que faz a inserção de um dado no banco de dados
    public long insert(String name, int age, String sex) {
        // Prepara os valores a serem inseridos
        ContentValues values = new ContentValues();
        values.put(PersonEntry.COLUMN_NAME, name);
        values.put(PersonEntry.COLUMN_AGE, age);
        values.put(PersonEntry.COLUMN_SEX, sex);

        // Faz a inserção e retorna com o ID da linha inserida
        return db.insert(PersonEntry.TABLE_NAME, null, values);
    }

    // Método que faz a leitura de uma pessoa no banco de dados
    public Cursor read(String column, String operand, String cValue, String columnOrder, String order) {
        // Define os parâmetros da consulta
        String[] projection = {
                PersonEntry._ID,
                PersonEntry.COLUMN_NAME,
                PersonEntry.COLUMN_AGE,
                PersonEntry.COLUMN_SEX
        };
        String selection        = column + " " + operand + " ?";
        String[] selectionArgs  = { cValue };
        String sortOrder        = columnOrder + " " + order;

        // Faz a consulta no banco de dados
        Cursor res = db.query(PersonEntry.TABLE_NAME, projection, selection, selectionArgs,null,null, sortOrder);
        // Vai para o primeiro resultado
        res.moveToFirst();

        // Retorna o objeto Cursor
        return res;
    }

    // Método que faz a leitura de todas as pessoas no banco de dados
    public Cursor readAll() {
        return read(PersonEntry._ID, this.GREATHEROREQUALS, "0", PersonEntry._ID, this.ASC);
    }

    // Método que atualiza os dados de uma pessoa do banco de dados e retorna com o número de linhas afetadas
    public int update(int id, String name, int age, String sex) {
        // Prepara os valores
        ContentValues values = new ContentValues();
        values.put(PersonEntry.COLUMN_NAME, name);
        values.put(PersonEntry.COLUMN_AGE, age);
        values.put(PersonEntry.COLUMN_SEX, sex);

        // Define o seletor para o ID do registro
        String selection        = PersonEntry._ID + " = ?";
        String[] selectionArgs  = { String.valueOf(id) };

        // Retorna com o número de linhas afetadas
        return db.update(PersonEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    // Método que remove uma pessoa do banco de dados
    public void delete(int id) {
        // Define o seletor para o ID do registro
        String selection = PersonEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        // Faz a remoção do banco de dados
        db.delete(PersonEntry.TABLE_NAME, selection, selectionArgs);
    }

    // Método que remove todas as pessoas do banco de dados
    public void deleteAll() {
        // Define o seletor para o ID do registro
        String selection = PersonEntry._ID + " >= ?";
        String[] selectionArgs = { "0" };

        // Faz a remoção do banco de dados
        db.delete(PersonEntry.TABLE_NAME, selection, selectionArgs);
    }

    // Método que testa o funcionamento do banco de dados
    public void testDB() {
        // Inserir dados
        this.insert("Amilton Junior", 24, "M");
        this.insert("Deise Carolina", 25, "F");

        // Ler dados
        Cursor res = this.readAll();
        res.moveToFirst();

        // Percorre os resultados
        while (res.getCount() > 0) {
            // Obtém os dados da pessoa
            int id      = res.getInt(res.getColumnIndexOrThrow(DB.PersonEntry._ID));
            String name = res.getString(res.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_NAME));
            int age     = res.getInt(res.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_AGE));
            String sex  = res.getString(res.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_SEX));

            System.out.println("\n-\n" + "ID: " + id + "\nNome: " + name + "\nIdade: " + age + "\nSexo: " + sex);

            // Se era o último resultado, encerra o loop
            if (res.isLast())
                break;

            // Move para o próximo resultado
            res.moveToNext();
        }

        // Apaga todos os dados do banco de dados
        this.deleteAll();
    }

    /****************************/

    // Classe que define o esquema do banco de dados
    public static class PersonEntry implements BaseColumns {

        // Atributos da classe
        public static final String TABLE_NAME   = "person"; // Nome da tabela
        public static final String _ID          = "id"; // Campo ID
        public static final String COLUMN_NAME  = "name"; // Campo Nome
        public static final String COLUMN_AGE   = "age"; // Campo Idade
        public static final String COLUMN_SEX   = "sex"; // Campo Sexo
    }

    /****************************/

    // Classe que faz a criação do banco de dados
    public class PostDbHelper extends SQLiteOpenHelper {

        // Atributos da classe
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";

        private static final String SQL_CREATE_PERSON =
                "CREATE TABLE " + PersonEntry.TABLE_NAME + " (" +
                        PersonEntry._ID + " INTEGER PRIMARY KEY," +
                        PersonEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                        PersonEntry.COLUMN_AGE + TEXT_TYPE + COMMA_SEP +
                        PersonEntry.COLUMN_SEX + TEXT_TYPE + " )";

        private static final String SQL_DELETE_PERSON =
                "DROP TABLE IF EXISTS " + PersonEntry.TABLE_NAME;

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "PersonDB.db";

        public PostDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_PERSON);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_PERSON);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
