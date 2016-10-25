package demos.android.com.craneo.demowearablemobilgatekidper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by crane on 10/22/2016.
 */

public class KidsDBOpenHelper extends SQLiteOpenHelper {
    private static final String LOGTAG = "KidsDBOpenHelper";
    private static final String DATABASE_NAME = "tempkid.db";
    private static final int DATABASE_VERSION = 3;
    
    public static final String TABLE_NAME = "kids";
    public static final String COLUMN_ID = "kidId";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastName";
    public static final String COLUMN_IMAGE = "image";
    
    private static final String CREATE_TABLE =
            "CREATE TABLE "+ TABLE_NAME + " ("+
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_NAME + " TEXT, "+
            COLUMN_LASTNAME + " TEXT, "+
            COLUMN_IMAGE + " TEXT"+
            ")";
    

    public KidsDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.i(LOGTAG, "Table has been created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
