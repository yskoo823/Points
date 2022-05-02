package edu.sjsu.android.project4kevinkoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * A class for a SQLite database of locations.
 */
class LocationsDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locationsDatabase";
    private static final String TABLE_NAME = "locations";
    private static final int VERSION = 1;
    // TODO: 4 Strings (protected static final) for 4 column names
    protected static final String COLUMN_0 = "_id";
    protected static final String COLUMN_1 = "latitude";
    protected static final String COLUMN_2 = "longitude";
    protected static final String COLUMN_3 = "zoom_level";
    static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " ("+ COLUMN_0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_1 + " DOUBLE NOT NULL, "
                    + COLUMN_2 + " DOUBLE NOT NULL, "
                    + COLUMN_3 + " FLOAT NOT NULL);";

    public LocationsDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * A method that inserts a new location to the table.
     *
     * @param contentValues location detail
     * @return the id
     */
    public long insert(ContentValues contentValues) {
        // Remember to delete the throw statement after you done
        SQLiteDatabase database = getWritableDatabase();
        return database.insert(TABLE_NAME, null, contentValues);
    }

    /**
     * A method that deletes all locations from the table.
     *
     * @return number of locations deleted
     */
    public int deleteAll() {
        // Remember to delete the throw statement after you done
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(TABLE_NAME, null, null);
    }

    /**
     * A method that returns all the locations from the table.
     *
     * @return Cursor
     */
    public Cursor getAllLocations() {
        // Remember to delete the throw statement after you done
        SQLiteDatabase database = getReadableDatabase();
        return database.query(TABLE_NAME, new String[]{COLUMN_1, COLUMN_2, COLUMN_3}, null, null, null, null, null);
    }
}