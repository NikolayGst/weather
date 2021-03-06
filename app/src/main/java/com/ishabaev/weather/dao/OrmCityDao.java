package com.ishabaev.weather.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.ishabaev.weather.dao.OrmCity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ORM_CITY".
*/
public class OrmCityDao extends AbstractDao<OrmCity, Long> {

    public static final String TABLENAME = "ORM_CITY";

    /**
     * Properties of entity OrmCity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_ID");
        public final static Property City_name = new Property(1, String.class, "city_name", false, "CITY_NAME");
        public final static Property Country = new Property(2, String.class, "country", false, "COUNTRY");
        public final static Property Lat = new Property(3, Double.class, "lat", false, "LAT");
        public final static Property Lon = new Property(4, Double.class, "lon", false, "LON");
    }


    public OrmCityDao(DaoConfig config) {
        super(config);
    }
    
    public OrmCityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ORM_CITY\" (" + //
                "\"_ID\" INTEGER PRIMARY KEY ," + // 0: _id
                "\"CITY_NAME\" TEXT," + // 1: city_name
                "\"COUNTRY\" TEXT," + // 2: country
                "\"LAT\" REAL," + // 3: lat
                "\"LON\" REAL);"); // 4: lon
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ORM_CITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrmCity entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
 
        String city_name = entity.getCity_name();
        if (city_name != null) {
            stmt.bindString(2, city_name);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(3, country);
        }
 
        Double lat = entity.getLat();
        if (lat != null) {
            stmt.bindDouble(4, lat);
        }
 
        Double lon = entity.getLon();
        if (lon != null) {
            stmt.bindDouble(5, lon);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset) ? null : cursor.getLong(offset);
    }    

    /** @inheritdoc */
    @Override
    public OrmCity readEntity(Cursor cursor, int offset) {
        return new OrmCity( //
            cursor.isNull(offset) ? null : cursor.getLong(offset), // _id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // city_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // country
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // lat
            cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4) // lon
        );
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrmCity entity, int offset) {
        entity.set_id(cursor.isNull(offset) ? null : cursor.getLong(offset));
        entity.setCity_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCountry(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLat(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setLon(cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OrmCity entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OrmCity entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
