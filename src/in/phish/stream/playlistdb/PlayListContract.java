package in.phish.stream.playlistdb;



import android.provider.BaseColumns;


/**
 * @author corn
 *
 */
public final class PlayListContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + PlayListEntry.TABLE_NAME + " (" +
            PlayListEntry._ID + " INTEGER PRIMARY KEY, " +
            PlayListEntry.COLUMN_NAME_IDX       + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_TITLE     + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_TRACK_ID  + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_PLAYING   + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_POSITION  + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_DURATION  + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_SET_NAME  + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_MP3       + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_DATE      + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_VENUE_ID  + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_TOUR_ID   + TEXT_TYPE + COMMA_SEP +
            PlayListEntry.COLUMN_NAME_SHOW_ID   + TEXT_TYPE + COMMA_SEP +
            " )";
    static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + PlayListEntry.TABLE_NAME;


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PlayListContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PlayListEntry implements BaseColumns {
        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_NAME_IDX          = "idx";
        public static final String COLUMN_NAME_TITLE        = "title";
        public static final String COLUMN_NAME_TRACK_ID     = "id";
        public static final String COLUMN_NAME_PLAYING      = "playing";
        public static final String COLUMN_NAME_POSITION     = "position";
        public static final String COLUMN_NAME_DURATION     = "duration";
        public static final String COLUMN_NAME_SET_NAME     = "setname";
        public static final String COLUMN_NAME_MP3          = "mp3";
        public static final String COLUMN_NAME_DATE         = "date";
        public static final String COLUMN_NAME_VENUE_ID     = "venueid";
        public static final String COLUMN_NAME_TOUR_ID      = "tourid";
        public static final String COLUMN_NAME_SHOW_ID      = "showid";
    }
}
