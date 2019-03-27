package com.site.vs.videostation.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.site.vs.videostation.entity.DetailEntity;
import com.site.vs.videostation.entity.HistoryEntity;
import com.site.vs.videostation.entity.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * 观看历史及
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/19 9:58
 */

public class DBManager {
    private static DBHelper dbHelper;

    public static void instance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
    }

    public static List<HistoryEntity> getHistory() {
        List<HistoryEntity> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from history order by time desc limit 20", null);
        while (cursor.moveToNext()) {
            HistoryEntity move = new HistoryEntity();
            move.id = cursor.getString(cursor.getColumnIndex("_id"));
            move.title = cursor.getString(cursor.getColumnIndex("title"));
            move.name = cursor.getString(cursor.getColumnIndex("name"));
            move.pic = cursor.getString(cursor.getColumnIndex("pic"));
            move.playTime = cursor.getInt(cursor.getColumnIndex("pic"));
            move.originIndex = cursor.getInt(cursor.getColumnIndex("origin_index"));
            move.playIndex = cursor.getInt(cursor.getColumnIndex("play_index"));
            move.playName = cursor.getString(cursor.getColumnIndex("play_name"));
            move.playTime = cursor.getInt(cursor.getColumnIndex("play_time"));
            list.add(move);
        }
        cursor.close();
        return list;
    }

    public static HistoryEntity getHistory(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from history where _id = ?", new String[]{id});
        while (cursor.moveToNext()) {
            HistoryEntity move = new HistoryEntity();
            move.id = cursor.getString(cursor.getColumnIndex("_id"));
            move.title = cursor.getString(cursor.getColumnIndex("title"));
            move.name = cursor.getString(cursor.getColumnIndex("name"));
            move.pic = cursor.getString(cursor.getColumnIndex("pic"));
            move.playTime = cursor.getInt(cursor.getColumnIndex("pic"));
            move.originIndex = cursor.getInt(cursor.getColumnIndex("origin_index"));
            move.playIndex = cursor.getInt(cursor.getColumnIndex("play_index"));
            move.playName = cursor.getString(cursor.getColumnIndex("play_name"));
            move.playTime = cursor.getInt(cursor.getColumnIndex("play_time"));
            cursor.close();
            return move;
        }
        cursor.close();
        return null;
    }

    public static boolean putHistory(HistoryEntity en) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id", en.id);
        cv.put("name", en.name);
        cv.put("pic", en.pic);
        cv.put("title", en.title);
        cv.put("play_time", en.playTime);
        cv.put("origin_index", en.originIndex);
        cv.put("play_index", en.playIndex);
        cv.put("play_name", en.playName);
        cv.put("time", System.currentTimeMillis());
        long result = db.replace("history", null, cv);
        db.close();
        return result != -1;
    }

    public static boolean clearHistory() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long i = db.delete("history", null, new String[]{});
        db.close();
        return i != 0;
    }

    public static boolean deleteHistory(HistoryEntity move) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id", move.id);
        long result = db.delete("history", "_id = ?", new String[]{move.id});
        db.close();
        return result != -1;
    }

    /**
     * 获取收藏列表
     */
    public static List<Move> getFavorite() {
        List<Move> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from favorite  order by time desc  limit 20", null);
        while (cursor.moveToNext()) {
            Move move = new Move();
            move.id = cursor.getString(cursor.getColumnIndex("_id"));
            move.actor = cursor.getString(cursor.getColumnIndex("actor"));
            move.area = cursor.getString(cursor.getColumnIndex("area"));
            move.year = cursor.getString(cursor.getColumnIndex("year"));
            move.title = cursor.getString(cursor.getColumnIndex("title"));
            move.name = cursor.getString(cursor.getColumnIndex("name"));
            move.litpic = cursor.getString(cursor.getColumnIndex("pic"));
            list.add(move);
        }
        cursor.close();
        return list;
    }

    /**
     * 是否收藏
     */
    public static boolean isFavorite(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from favorite where _id = ?", new String[]{id});
        int count = cursor.getCount();
        cursor.close();
        return count != 0;
    }

    /**
     * 收藏切换
     */
    public static boolean swithFavorite(DetailEntity move) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from favorite where _id = ?", new String[]{move.id});
        int count = cursor.getCount();
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("_id", move.id);
        cv.put("actor", move.actor);
        cv.put("area", move.area);
        cv.put("name", move.name);
        cv.put("pic", move.pic);
        cv.put("title", move.gold);
        cv.put("year", move.year);
        cv.put("time", System.currentTimeMillis());
        if (count != 0) {
            db.delete("favorite", "_id = ?", new String[]{move.id});
            db.close();
            return false;
        } else {
            db.replace("favorite", null, cv);
            db.close();
            return true;
        }
    }

    public static boolean clearFavorite() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long i = db.delete("favorite", null, new String[]{});
        db.close();
        return i != 0;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "gulud.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuffer historySb = new StringBuffer();
            historySb.append("CREATE TABLE IF NOT EXISTS ");
            historySb.append("history(");
            historySb.append("_id PRIMARY KEY ");
            historySb.append(",name TEXT");
            historySb.append(",pic TEXT");
            historySb.append(",title TEXT");
            historySb.append(",play_time INTEGER");
            historySb.append(",time INTEGER");
            historySb.append(",origin_index INTEGER");
            historySb.append(",play_index INTEGER");
            historySb.append(",play_name TEXT");
            historySb.append(")");
            db.execSQL(historySb.toString());

            StringBuffer favoriteSb = new StringBuffer();
            favoriteSb.append("CREATE TABLE IF NOT EXISTS ");
            favoriteSb.append("favorite(");
            favoriteSb.append("_id PRIMARY KEY");
            favoriteSb.append(",name TEXT");
            favoriteSb.append(",pic TEXT");
            favoriteSb.append(",title TEXT");
            favoriteSb.append(",year TEXT");
            favoriteSb.append(",area TEXT");
            favoriteSb.append(",actor TEXT");
            favoriteSb.append(",time LONG");
            favoriteSb.append(")");
            db.execSQL(favoriteSb.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
