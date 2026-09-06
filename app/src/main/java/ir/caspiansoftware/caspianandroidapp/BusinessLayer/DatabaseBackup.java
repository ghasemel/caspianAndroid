package ir.caspiansoftware.caspianandroidapp.BusinessLayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The parts of the diagnostic database export that do not need Android.
 *
 * Kept separate from InitialSettingFragment so they can be unit tested: the
 * fragment still owns the Android side (resolving the database path, building
 * the FileProvider URI, firing the share intent), but the filename rules and
 * the byte copy are plain logic and are covered by DatabaseBackupTest.
 */
public final class DatabaseBackup {

    /** Prefix and extension are fixed so support can recognise these at a glance. */
    private static final String PREFIX = "caspian_";
    private static final String EXTENSION = ".sqlite";

    private DatabaseBackup() { }

    /**
     * Names an export after the device and date that produced it.
     *
     * Support receives these files from many users, so "caspian_db.sqlite"
     * three times over is useless. The device id is base64 and therefore
     * contains '+', '/' and '=' -- all either illegal or actively harmful in a
     * filename -- so everything outside [A-Za-z0-9] is stripped rather than
     * escaped. A null or empty id still has to produce a usable name.
     *
     * @param deviceId    raw device id, may be null
     * @param jalaliToday today as yyyy/MM/dd; slashes are removed
     */
    public static String buildFileName(String deviceId, String jalaliToday) {
        String device = deviceId == null ? "" : deviceId.replaceAll("[^A-Za-z0-9]", "");
        if (device.isEmpty())
            device = "unknown";

        String stamp = jalaliToday == null ? "" : jalaliToday.replace("/", "");

        return PREFIX + device + "_" + stamp + EXTENSION;
    }

    /**
     * Byte-for-byte copy.
     *
     * Deliberately a raw file copy rather than a table-by-table export: the
     * point of this file is to show the database exactly as it is on the
     * device, corruption included. Rebuilding it row by row would produce a
     * clean database and hide the very fault being investigated.
     */
    public static void copyFile(File source, File target) throws IOException {
        FileInputStream in = new FileInputStream(source);
        try {
            FileOutputStream out = new FileOutputStream(target);
            try {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) > 0)
                    out.write(buffer, 0, read);
                out.flush();
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
