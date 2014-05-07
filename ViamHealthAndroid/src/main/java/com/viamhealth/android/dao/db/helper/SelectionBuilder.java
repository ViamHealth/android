package com.viamhealth.android.dao.db.helper;

/**
 * Created by naren on 13/12/13.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.viamhealth.android.utils.LogUtils;
import com.viamhealth.android.utils.LogUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for building selection clauses for {@link android.database.sqlite.SQLiteDatabase}.
 *
 * <p>This class provides a convenient frontend for working with SQL. Instead of composing statements
 * manually using string concatenation, method calls are used to construct the statement one
 * clause at a time. These methods can be chained together.
 *
 * <p>If multiple where() statements are provided, they're combined using {@code AND}.
 *
 * <p>Example:
 *
 * <pre>
 *     SelectionBuilder builder = new SelectionBuilder();
 *     Cursor c = builder.table(FeedContract.Entry.TABLE_NAME)       // String TABLE_NAME = "entry"
 *                       .where(FeedContract.Entry._ID + "=?", id);  // String _ID = "_ID"
 *                       .query(db, projection, sortOrder)
 *
 * </pre>
 *
 * <p>In this example, the table name and filters ({@code WHERE} clauses) are both explicitly
 * specified via method call. SelectionBuilder takes care of issuing a "query" command to the
 * database, and returns the resulting {@link android.database.Cursor} object.
 *
 * <p>Inner {@code JOIN}s can be accomplished using the mapToTable() function. The map() function
 * can be used to create new columns based on arbitrary (SQL-based) criteria. In advanced usage,
 * entire subqueries can be passed into the map() function.
 *
 * <p>Advanced example:
 *
 * <pre>
 *     // String SESSIONS_JOIN_BLOCKS_ROOMS = "sessions "
 *     //        + "LEFT OUTER JOIN blocks ON sessions.block_id=blocks.block_id "
 *     //        + "LEFT OUTER JOIN rooms ON sessions.room_id=rooms.room_id";
 *
 *     // String Subquery.BLOCK_NUM_STARRED_SESSIONS =
 *     //       "(SELECT COUNT(1) FROM "
 *     //        + Tables.SESSIONS + " WHERE " + Qualified.SESSIONS_BLOCK_ID + "="
 *     //        + Qualified.BLOCKS_BLOCK_ID + " AND " + Qualified.SESSIONS_STARRED + "=1)";
 *
 *     String Subqery.BLOCK_SESSIONS_COUNT =
 *     Cursor c = builder.table(Tables.SESSIONS_JOIN_BLOCKS_ROOMS)
 *               .map(Blocks.NUM_STARRED_SESSIONS, Subquery.BLOCK_NUM_STARRED_SESSIONS)
 *               .mapToTable(Sessions._ID, Tables.SESSIONS)
 *               .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
 *               .mapToTable(Sessions.BLOCK_ID, Tables.SESSIONS)
 *               .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
 *               .where(Qualified.SESSIONS_BLOCK_ID + "=?", blockId);
 * </pre>
 *
 * <p>In this example, we have two different types of {@code JOIN}s: a left outer join using a
 * modified table name (since this class doesn't directly support these), and an inner join using
 * the mapToTable() function. The map() function is used to insert a count based on specific
 * criteria, executed as a sub-query.
 *
 * This class is <em>not</em> thread safe.
 *
 * Helper for building selection clauses for {@link SQLiteDatabase}. Each
 * appended clause is combined using {@code AND}. This class is <em>not</em>
 * thread safe.
 */
public class SelectionBuilder {
    private static final String TAG = LogUtils.makeLogTag(SelectionBuilder.class);

    private String mTable = null;
    private Map<String, String> mProjectionMap = new HashMap<String, String>();
    private StringBuilder mSelection = new StringBuilder();
    private ArrayList<String> mSelectionArgs = new ArrayList<String>();

    /**
     * Reset any internal state, allowing this builder to be recycled.
     */
    public SelectionBuilder reset() {
        mTable = null;
        mSelection.setLength(0);
        mSelectionArgs.clear();
        return this;
    }

    /**
     * Append the given selection clause to the internal state. Each clause is
     * surrounded with parenthesis and combined using {@code AND}.
     */
    public SelectionBuilder where(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException(
                        "Valid selection required when including arguments=");
            }

            // Shortcut when clause is empty
            return this;
        }

        if (mSelection.length() > 0) {
            mSelection.append(" AND ");
        }

        mSelection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(mSelectionArgs, selectionArgs);
        }

        return this;
    }

    public SelectionBuilder table(String table) {
        mTable = table;
        return this;
    }

    private void assertTable() {
        if (mTable == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    public SelectionBuilder mapToTable(String column, String table) {
        mProjectionMap.put(column, table + "." + column);
        return this;
    }

    public SelectionBuilder map(String fromColumn, String toClause) {
        mProjectionMap.put(fromColumn, toClause + " AS " + fromColumn);
        return this;
    }

    /**
     * Return selection string for current internal state.
     *
     * @see #getSelectionArgs()
     */
    public String getSelection() {
        return mSelection.toString();
    }

    /**
     * Return selection arguments for current internal state.
     *
     * @see #getSelection()
     */
    public String[] getSelectionArgs() {
        return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
    }

    private void mapColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            final String target = mProjectionMap.get(columns[i]);
            if (target != null) {
                columns[i] = target;
            }
        }
    }

    @Override
    public String toString() {
        return "SelectionBuilder[table=" + mTable + ", selection=" + getSelection()
                + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
        return query(db, columns, null, null, orderBy, null);
    }

    /**
     * Execute query using the current internal state as {@code WHERE} clause.
     */
    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy,
                        String having, String orderBy, String limit) {
        assertTable();
        if (columns != null) mapColumns(columns);
        LogUtils.LOGV(TAG, "query(table="+mTable+"; columns=" + Arrays.toString(columns) + ") " + this);
        return db.query(mTable, columns, getSelection(), getSelectionArgs(), groupBy, having,
                orderBy, limit);
    }

    /**
     * Execute update using the current internal state as {@code WHERE} clause.
     */
    public int update(SQLiteDatabase db, ContentValues values) {
        assertTable();
        LogUtils.LOGV(TAG, "update() " + this);
        return db.update(mTable, values, getSelection(), getSelectionArgs());
    }

    /**
     * Execute delete using the current internal state as {@code WHERE} clause.
     */
    public int delete(SQLiteDatabase db) {
        assertTable();
        LogUtils.LOGV(TAG, "delete() " + this);
        return db.delete(mTable, getSelection(), getSelectionArgs());
    }
}