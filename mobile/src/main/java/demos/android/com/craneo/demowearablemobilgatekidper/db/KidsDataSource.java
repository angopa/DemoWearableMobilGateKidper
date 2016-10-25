package demos.android.com.craneo.demowearablemobilgatekidper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import demos.android.com.craneo.demowearablemobilgatekidper.model.Kid;

/**
 * Created by crane on 10/22/2016.
 */

public class KidsDataSource {

    private static final String TAG = "KidsDataSource";

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    private static final String[] allColums= {
            KidsDBOpenHelper.COLUMN_ID,
            KidsDBOpenHelper.COLUMN_NAME,
            KidsDBOpenHelper.COLUMN_LASTNAME,
            KidsDBOpenHelper.COLUMN_IMAGE};

    public KidsDataSource(Context context) {
        dbHelper = new KidsDBOpenHelper(context);
    }

    public void open(){
        Log.i(TAG, "Database opened");
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        Log.i(TAG, "Database closed");
        dbHelper.close();
    }

    public Kid create(Kid kid){
        ContentValues values = new ContentValues();
        values.put(KidsDBOpenHelper.COLUMN_NAME, kid.getName());
        values.put(KidsDBOpenHelper.COLUMN_LASTNAME, kid.getLastName());
        values.put(KidsDBOpenHelper.COLUMN_IMAGE, kid.getImage());
        long insertId = database.insert(KidsDBOpenHelper.TABLE_NAME, null, values);
        kid.setId(insertId);
        return kid;
    }

    public List<Kid> findAll(){
        List<Kid> kids = new ArrayList<>();
        Cursor cursor = database.query(KidsDBOpenHelper.TABLE_NAME, allColums,
                null, null, null, null, null);
        Log.i(TAG, "Returned "+ cursor.getCount()+" rows");
        if (cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Kid kid = new Kid();
                kid.setId(cursor.getLong(cursor.getColumnIndex(KidsDBOpenHelper.COLUMN_ID)));
                kid.setName(cursor.getString(cursor.getColumnIndex(KidsDBOpenHelper.COLUMN_NAME)));
                kid.setLastName(cursor.getString(cursor.getColumnIndex(KidsDBOpenHelper.COLUMN_LASTNAME)));
                kid.setImage(cursor.getString(cursor.getColumnIndex(KidsDBOpenHelper.COLUMN_IMAGE)));
                Log.d(TAG, kid.getImage());
                kids.add(kid);
            }
        }
        return kids;
    }

}
