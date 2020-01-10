package cn.mmvc.notepad.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    public static final String DATABASE_NAME="Notepad";//数据库名
    public static final String DATABASE_TABLE="Note";//表名
    public static final int DATABASE_VARION=1;//数据库的版本

    public static final String NOTEPAD_ID="id";
    public static final String NOTEPAD_CONTENT="content";
    public static final String NOTEPAD_TIME="notetime";

    public static final String getTime()
    {
        SimpleDateFormat simpleFormatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        return simpleFormatter.format(date);
    }
}
