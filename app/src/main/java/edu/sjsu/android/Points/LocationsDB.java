package edu.sjsu.android.Points;

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
    protected static final String ID = "_id";
    protected static final String LAT = "latitude";
    protected static final String LNG = "longitude";
    protected static final String ZOOM = "zoom_level";
    static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " ("+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LAT + " DOUBLE NOT NULL, "
                    + LNG + " DOUBLE NOT NULL, "
                    + ZOOM + " FLOAT NOT NULL);";

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
        return database.query(TABLE_NAME, new String[]{ID, LAT, LNG, ZOOM}, null, null, null, null, null);
    }
}