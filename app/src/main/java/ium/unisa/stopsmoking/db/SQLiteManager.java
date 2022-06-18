package ium.unisa.stopsmoking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "GoalsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Goal";

    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "name";
    private static final String PRICE_FIELD = "price";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(ID_FIELD)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(PRICE_FIELD)
                .append(" REAL)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO qualcosa
    }

    public void addGoalToDatabase(Goal goal) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, goal.getName());
        values.put(PRICE_FIELD, goal.getPrice());

        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void updateGoalInDatabase(Goal goal) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE_FIELD, goal.getName());
        values.put(PRICE_FIELD, goal.getPrice());

        sqLiteDatabase.update(TABLE_NAME, values, ID_FIELD + "=?", new String[]{String.valueOf(goal.getId())});
    }

    public void deleteGoalFromDatabase(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_FIELD + "=?",
                new String[] { String.valueOf(goal.getId()) });
        db.close();
    }

    public ArrayList<Goal> getGoalListArray() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            ArrayList<Goal> goalArrayList = new ArrayList<>();

            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    long id = result.getInt(0);
                    String name = result.getString(1);
                    double price = result.getDouble(2);
                    Goal goal = new Goal(id, name, price);
                    goalArrayList.add(goal);
                }
            }
            return goalArrayList;
        }
    }
}
