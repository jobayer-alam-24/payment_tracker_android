package com.testapp.paymenttracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "batch_payment.db";
    private static final int DATABASE_VERSION = 3;

    // Batch table
    public static final String TABLE_BATCH = "batches";
    public static final String COLUMN_BATCH_ID = "id";
    public static final String COLUMN_BATCH_NAME = "name";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_BATCH_FEE = "fee_per_month";

    // Student table
    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_STUDENT_BATCH_ID = "batch_id";
    public static final String COLUMN_STUDENT_NAME = "name";
    //Monthly Payment Table
    public static final String TABLE_MONTHLY_PAYMENT = "monthly_payments";
    public static final String COLUMN_PAYMENT_ID = "id";
    public static final String COLUMN_PAYMENT_STUDENT_ID = "student_id";
    public static final String COLUMN_PAYMENT_MONTH = "month"; // 1-12
    public static final String COLUMN_PAYMENT_PAID = "paid"; // 0 or 1
    //Monthly payment creation
    private static final String CREATE_TABLE_MONTHLY_PAYMENT =
            "CREATE TABLE " + TABLE_MONTHLY_PAYMENT + " (" +
                    COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PAYMENT_STUDENT_ID + " INTEGER, " +
                    COLUMN_PAYMENT_MONTH + " INTEGER, " +
                    COLUMN_PAYMENT_PAID + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY(" + COLUMN_PAYMENT_STUDENT_ID + ") REFERENCES " + TABLE_STUDENT + "(" + COLUMN_STUDENT_ID + ")" +
                    ");";
    // Batch table creation
    private static final String CREATE_TABLE_BATCH =
            "CREATE TABLE " + TABLE_BATCH + " (" +
                    COLUMN_BATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BATCH_NAME + " TEXT NOT NULL, " +
                    COLUMN_START_DATE + " TEXT, " +
                    COLUMN_BATCH_FEE + " REAL" +
                    ");";

    // Student table creation
    private static final String CREATE_TABLE_STUDENT =
            "CREATE TABLE " + TABLE_STUDENT + " (" +
                    COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_BATCH_ID + " INTEGER, " +
                    COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_STUDENT_BATCH_ID + ") REFERENCES " + TABLE_BATCH + "(" + COLUMN_BATCH_ID + ")" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BATCH);
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL(CREATE_TABLE_MONTHLY_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_PAYMENT);

        onCreate(db);
    }

    // ----------------- Batch Methods -----------------
    public boolean insertBatch(String name, String startDate, double fee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BATCH_NAME, name);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_BATCH_FEE, fee);
        long result = db.insert(TABLE_BATCH, null, values);
        db.close();
        return result != -1;
    }

    public boolean deleteBatch(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_BATCH, COLUMN_BATCH_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return deletedRows > 0;
    }
    public boolean updateStudent(int studentId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, newName);
        int rowsUpdated = db.update(TABLE_STUDENT, values, COLUMN_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowsUpdated > 0;
    }
    public Cursor getAllBatches() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BATCH, null);
    }

    // ----------------- Student Methods -----------------
    public boolean insertStudent(int batchId, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_BATCH_ID, batchId);
        values.put(COLUMN_STUDENT_NAME, name);
        long result = db.insert(TABLE_STUDENT, null, values);
        db.close();
        return result != -1;
    }
    public boolean deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_STUDENT, COLUMN_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)});
        db.close();
        return deletedRows > 0;
    }
    public boolean[] getMonthlyPaymentStatus(int studentId) {
        boolean[] status = new boolean[12]; // Jan-Dec
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_PAYMENT_MONTH + ", " + COLUMN_PAYMENT_PAID +
                        " FROM " + TABLE_MONTHLY_PAYMENT +
                        " WHERE " + COLUMN_PAYMENT_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int month = cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_MONTH));
                @SuppressLint("Range") int paid = cursor.getInt(cursor.getColumnIndex(COLUMN_PAYMENT_PAID));
                status[month - 1] = paid == 1;
            }
            cursor.close();
        }
        return status;
    }
    public double getTotalFeesForAllBatches() {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        // Get all students with their batch fee
        Cursor cursor = db.rawQuery(
                "SELECT s." + COLUMN_STUDENT_ID + ", b." + COLUMN_BATCH_FEE +
                        " FROM " + TABLE_STUDENT + " s " +
                        " INNER JOIN " + TABLE_BATCH + " b " +
                        " ON s." + COLUMN_STUDENT_BATCH_ID + " = b." + COLUMN_BATCH_ID,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
                @SuppressLint("Range") double feePerMonth = cursor.getDouble(cursor.getColumnIndex(COLUMN_BATCH_FEE));

                // Get paid months for this student
                boolean[] paidMonths = getMonthlyPaymentStatus(studentId);
                int countPaid = 0;
                for (boolean paid : paidMonths) {
                    if (paid) countPaid++;
                }

                total += countPaid * feePerMonth;

            } while (cursor.moveToNext());

            cursor.close();
        }

        return total;
    }


    // Update a month payment
    public void updateMonthlyPayment(int studentId, int month, boolean paid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_PAID, paid ? 1 : 0);

        int updated = db.update(TABLE_MONTHLY_PAYMENT, values,
                COLUMN_PAYMENT_STUDENT_ID + "=? AND " + COLUMN_PAYMENT_MONTH + "=?",
                new String[]{String.valueOf(studentId), String.valueOf(month)});

        if (updated == 0) {
            // Insert if not exists
            values.put(COLUMN_PAYMENT_STUDENT_ID, studentId);
            values.put(COLUMN_PAYMENT_MONTH, month);
            db.insert(TABLE_MONTHLY_PAYMENT, null, values);
        }
        db.close();
    }
    public List<Student> getStudentsByBatch(int batchId) {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT + " WHERE " + COLUMN_STUDENT_BATCH_ID + " = ?",
                new String[]{String.valueOf(batchId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME));
                students.add(new Student(id, batchId, name));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return students;
    }
}
