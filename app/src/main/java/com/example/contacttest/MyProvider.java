package com.example.contacttest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
public class DatabaseProvider extends ContentProvider{

    public static final int STUDENT_DIR = 0;
    public static final int STUDENT_ITEM = 1;
    public static final int SUBJECT_DIR = 2;
    public static final int SUBJECT_ITEM = 3;

    public static final String AUTHORITY = "com.example.contacttest.provider";

    private static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "student", STUDENT_DIR);
        uriMatcher.addURI(AUTHORITY, "student/#", STUDENT_ITEM);
        uriMatcher.addURI(AUTHORITY, "subject", SUBJECT_DIR);
        uriMatcher.addURI(AUTHORITY, "subject/#", SUBJECT_ITEM);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                deletedRows = db.delete("student", selection, selectionArgs);
                break;
            case STUDENT_ITEM:
                String studentId = uri.getPathSegments().get(1);
                deletedRows = db.delete("student", "id = ?", new String[] {studentId});
                break;
            case SUBJECT_DIR:
                deletedRows = db.delete("subject", selection, selectionArgs);
                break;
            case SUBJECT_ITEM:
                String subjectId = uri.getPathSegments().get(1);
                deletedRows = db.delete("subject", "id = ?", new String[] {subjectId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.contacttest.provider.student";
            case STUDENT_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.contacttest.provider.student";
            case SUBJECT_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.contacttest.provider.subject";
            case SUBJECT_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.contacttest.provider.subject";
            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case STUDENT_ITEM:
                long newStudentId = db.insert("student", null, values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/student/"+newStudentId);
                break;
            case SUBJECT_ITEM:
                long newSubjectId = db.insert("subject", null, values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/student/"+newSubjectId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "student.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                cursor = db.query("student", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STUDENT_ITEM:
                String studentId = uri.getPathSegments().get(1);
                cursor = db.query("student", projection, "id = ?", new String[] {studentId}, null, null, sortOrder);
                break;
            case SUBJECT_DIR:
                cursor = db.query("subject", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SUBJECT_ITEM:
                String subjectId = uri.getPathSegments().get(1);
                cursor = db.query("subject", projection, "id = ?", new String[] {subjectId}, null, null, sortOrder);
                break;
            default:break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case STUDENT_DIR:
                updatedRows = db.update("student", values, selection, selectionArgs);
                break;
            case STUDENT_ITEM:
                String studentId = uri.getPathSegments().get(1);
                updatedRows = db.update("student", values, "id = ?", new String[] {studentId});
                break;
            case SUBJECT_DIR:
                updatedRows = db.update("subject", values, selection, selectionArgs);
                break;
            case SUBJECT_ITEM:
                String subjectId = uri.getPathSegments().get(1);
                updatedRows = db.update("subject", values, "id = ?", new String[] {subjectId});
                break;
            default:
                break;
        }
        return updatedRows;
    }
}


