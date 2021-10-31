package edu.sjsu.android.project4template;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public LocationsDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO: execute the SQL statement to create the table
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
        // TODO: insert the values to the table
        // Remember to delete the throw statement after you done
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * A method that deletes all locations from the table.
     *
     * @return number of locations deleted
     */
    public int deleteAll() {
        // TODO: delete all data from the table
        // Remember to delete the throw statement after you done
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * A method that returns all the locations from the table.
     *
     * @return Cursor
     */
    public Cursor getAllLocations() {
        // TODO: query all data from the table
        // Remember to delete the throw statement after you done
        throw new UnsupportedOperationException("Not yet implemented");
    }
}