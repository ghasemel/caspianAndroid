package ir.caspiansoftware.caspianandroidapp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.DatabaseBackup;

/**
 * Covers the diagnostic database export.
 *
 * The Android side (FileProvider URI, share intent) is not testable on the JVM
 * and was verified on the emulator instead. What is covered here is everything
 * that can go wrong silently: the filename rules and the integrity of the copy.
 */
public class DatabaseBackupTest {

    // region file name

    @Test
    public void fileName_stripsBase64PunctuationFromDeviceId() {
        // Real device ids are base64 and carry '+', '/' and '='. A '/' in
        // particular would be read as a directory separator.
        String name = DatabaseBackup.buildFileName("gTzSo2QqpXaHwZjou0yN+Q==", "1405/06/15");

        assertEquals("caspian_gTzSo2QqpXaHwZjou0yNQ_14050615.sqlite", name);
        assertTrue("must not contain a path separator", name.indexOf('/') < 0);
        assertTrue("must not contain '+'", name.indexOf('+') < 0);
        assertTrue("must not contain '='", name.indexOf('=') < 0);
    }

    @Test
    public void fileName_stripsSlashesFromTheDate() {
        String name = DatabaseBackup.buildFileName("abc", "1405/06/15");
        assertEquals("caspian_abc_14050615.sqlite", name);
    }

    @Test
    public void fileName_survivesNullDeviceId() {
        // getDeviceId() returns null before the device is configured, and the
        // export must still produce a usable name rather than crash.
        assertEquals("caspian_unknown_14050615.sqlite",
                DatabaseBackup.buildFileName(null, "1405/06/15"));
    }

    @Test
    public void fileName_survivesDeviceIdThatIsAllPunctuation() {
        // Stripping punctuation can leave nothing at all; the name must not
        // collapse to "caspian__14050615.sqlite".
        assertEquals("caspian_unknown_14050615.sqlite",
                DatabaseBackup.buildFileName("+/==", "1405/06/15"));
    }

    @Test
    public void fileName_isAlwaysRecognisableToSupport() {
        String name = DatabaseBackup.buildFileName("dev1", "1405/06/15");
        assertTrue(name.startsWith("caspian_"));
        assertTrue(name.endsWith(".sqlite"));
    }

    // endregion

    // region copy

    @Test
    public void copy_reproducesTheFileByteForByte() throws Exception {
        // A backup that is not byte-identical is worthless: the whole point is
        // to see the database exactly as it exists on the device, corruption
        // included. Random binary content, because a SQLite file is not text.
        byte[] content = new byte[64 * 1024 + 137];   // deliberately not a buffer multiple
        new Random(42).nextBytes(content);

        File source = File.createTempFile("src", ".sqlite");
        File target = File.createTempFile("dst", ".sqlite");
        try {
            FileOutputStream out = new FileOutputStream(source);
            out.write(content);
            out.close();

            DatabaseBackup.copyFile(source, target);

            assertEquals("size must match", source.length(), target.length());
            assertArrayEquals("bytes must match", content, readAll(target));
        } finally {
            source.delete();
            target.delete();
        }
    }

    @Test
    public void copy_overwritesAnExistingExport() throws Exception {
        // Exporting twice in one day reuses the same name, so the second run
        // must replace the first rather than append to it.
        File source = File.createTempFile("src", ".sqlite");
        File target = File.createTempFile("dst", ".sqlite");
        try {
            FileOutputStream stale = new FileOutputStream(target);
            stale.write(new byte[5000]);
            stale.close();

            byte[] content = "fresh".getBytes("UTF-8");
            FileOutputStream out = new FileOutputStream(source);
            out.write(content);
            out.close();

            DatabaseBackup.copyFile(source, target);

            assertEquals(content.length, target.length());
            assertArrayEquals(content, readAll(target));
        } finally {
            source.delete();
            target.delete();
        }
    }

    @Test
    public void copy_handlesAnEmptyFile() throws Exception {
        File source = File.createTempFile("src", ".sqlite");
        File target = File.createTempFile("dst", ".sqlite");
        try {
            DatabaseBackup.copyFile(source, target);
            assertEquals(0, target.length());
        } finally {
            source.delete();
            target.delete();
        }
    }

    // endregion

    private static byte[] readAll(File file) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            byte[] bytes = new byte[(int) raf.length()];
            raf.readFully(bytes);
            return bytes;
        } finally {
            raf.close();
        }
    }
}
