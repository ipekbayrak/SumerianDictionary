package com.kardelenapp.sumeriandictionary;

/**
 * Created by dell on 09.01.2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sumerian.db";
    private static String DB_NAME = "sumerian.db";
    public static final String TABLE_tokiponaEnglish = "SumerianToEnglish";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_tokipona= "Sumerian";
    public static final String COLUMN_english = "English";

    public static final String COLUMN_Tokipona= "Sumerian";
    public static final String COLUMN_English = "English";

    private static String DB_PATH = "/data/data/com.kardelenapp.sumeriandictionary/databases/";
    private static String DB_PATH2 = "/data/user/0/com.kardelenapp.sumeriandictionary/databases/";
    private List<Map<String, Object>> data;

    Context c;
    public static final String TABLE_TokiponaEnglish = "SumerianToEnglish";

    SQLiteDatabase database;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        c = context;

        SQLiteDatabase.loadLibs(c);


        createFirst();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public SQLiteDatabase getDatabase()
    {

        String myPath = DB_PATH + DB_NAME;


        database = SQLiteDatabase.openDatabase(myPath,"",null,SQLiteDatabase.OPEN_READONLY);


        return  database;
    }


    public String getMeaningEng(int id){
        SQLiteDatabase db = getDatabase();
        Cursor res =  db.rawQuery( "select "+COLUMN_english+" from "+TABLE_tokiponaEnglish+" where "+COLUMN_ID+"= "+id+" ", null );
        res.moveToFirst();

        String s = "";
        while(res.isAfterLast() == false){
            s =  res.getString(res.getColumnIndex(COLUMN_english));
            res.moveToNext();
        }
        db.close();
        return  s ;
    }

    public void createFirst(){


        boolean dbExist =  doesDatabaseExist(c,DB_PATH + DB_NAME); //checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            // this.getReadableDatabase( c.getClass().getName() );


            File new_file =new File(c.getDatabasePath(DB_NAME).toString());
            try
            {
                new_file.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            File new_file2 =new File(DB_PATH2 + DB_NAME);
            try
            {
                new_file2.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


           // File databaseFile = c.getDatabasePath("sumerian.db");
          // databaseFile.mkdirs();
            DBCreate dbCreate = new DBCreate(c,DB_NAME);

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error(e.toString());

            }
        }



    }

    public List<Map<String, Object>> getSimilar(String s)
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = getDatabase();
        Cursor res =  db.rawQuery( "select " + COLUMN_ID + "," + COLUMN_tokipona + " from " +TABLE_tokiponaEnglish+ " Where "+COLUMN_tokipona+" Like " +
                " '"+ s +"%' ORDER BY " +COLUMN_ID+" ASC LIMIT 30", null );
        res.moveToFirst();

        data = new ArrayList<Map<String, Object>>();

        while(res.isAfterLast() == false){


            Map<String, Object> item;
            item = new HashMap<String, Object>();
            item.put("id", res.getString(res.getColumnIndex(COLUMN_ID)));
            item.put("isim", res.getString(res.getColumnIndex(COLUMN_tokipona)));
            //item.put("anlam", res.getString(res.getColumnIndex(COLUMN_MEANING)));
            data.add(item);

            res.moveToNext();
        }

        db.close();
        return data;
    }

    public String getMeaningToki(int id){
        SQLiteDatabase db = getDatabase();
        Cursor res =  db.rawQuery( "select "+COLUMN_Tokipona+" from "+TABLE_TokiponaEnglish+" where "+COLUMN_ID+"= "+id+" ", null );
        res.moveToFirst();

        String s = "";
        while(res.isAfterLast() == false){
            s =  res.getString(res.getColumnIndex(COLUMN_Tokipona));
            res.moveToNext();
        }
        db.close();
        return  s ;
    }


    public List<Map<String, Object>> getSimilarToki(String s)
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = getDatabase();
        Cursor res =  db.rawQuery( "select " + COLUMN_ID + " , " + COLUMN_English + " from " +TABLE_TokiponaEnglish+ " Where "+COLUMN_English+" Like " +
                " '"+ s +"%' ORDER BY " +COLUMN_ID+" ASC LIMIT 30", null );
        res.moveToFirst();

        data = new ArrayList<Map<String, Object>>();

        while(res.isAfterLast() == false){


            Map<String, Object> item;
            item = new HashMap<String, Object>();
            item.put("id", res.getString(res.getColumnIndex(COLUMN_ID)));
            item.put("isim", res.getString(res.getColumnIndex(COLUMN_English)));
            //item.put("anlam", res.getString(res.getColumnIndex(COLUMN_MEANING)));
            data.add(item);

            res.moveToNext();
        }
        db.close();
        return data;
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    //boktan ve saçmalık
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            File databaseFile = new File(myPath);
            databaseFile.mkdirs();
            databaseFile.delete();
            checkDB  = SQLiteDatabase.openDatabase(myPath,"",null,SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = c.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = c.getDatabasePath(DB_NAME).toString();

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

}
