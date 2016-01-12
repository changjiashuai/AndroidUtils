package com.cjs.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Email: changjiashuai@gmail.com
 *
 * Created by CJS on 16/1/12 12:03.
 */
public class StorageUtils {

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "cache";

    /**
     *
     * @param context
     * @return application cache directory.
     *         Cache directory will be created on SD card "/Android/data/[app_package_name]/cache"
     * @throws IOException
     */
    public static File getCacheDirectory(Context context) throws IOException {
        return getCacheDirectory(context, true);
    }

    public static File getCacheDirectory(Context context, boolean external) throws IOException {
        File appCacheDir = null;
        String externalStorageState = Environment.getExternalStorageState();
        if (external && Environment.MEDIA_MOUNTED.equals(externalStorageState)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir==null){
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir==null){
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    public static File getExternalCacheDir(Context context) throws IOException {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            new File(appCacheDir, ".nomedia").createNewFile();
        }
        return appCacheDir;
    }

    /**
     *
     * @param context
     * @return individual application cache directory
     * @throws IOException
     */
    public static File getIndividualCacheDirectory(Context context) throws IOException {
        return getIndividualCacheDirectory(context, INDIVIDUAL_DIR_NAME);
    }

    public static File getIndividualCacheDirectory(Context context, String cacheDir) throws IOException {
        File appCacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(appCacheDir, cacheDir);
        if (!individualCacheDir.exists()){
            if (!individualCacheDir.mkdir()){
                individualCacheDir = appCacheDir;
            }
        }
        return individualCacheDir;
    }

    /**
     *
     * @param context
     * @param cacheDir
     * @return specified application cache directory.
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        return getOwnCacheDirectory(context, cacheDir, true);
    }

    public static File getOwnCacheDirectory(Context context, String cacheDir, boolean external) {
        File appCacheDir = null;
        if (external && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int permission = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return permission == PackageManager.PERMISSION_GRANTED;
    }
}