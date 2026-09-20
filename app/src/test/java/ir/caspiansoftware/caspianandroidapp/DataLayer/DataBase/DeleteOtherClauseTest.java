package ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;

/**
 * Covers the three faults that made person sync fail with
 * FOREIGN KEY constraint failed (787) on a customer database:
 *
 *  1. mali was not guarded, so deleting a person a voucher referenced
 *     violated the constraint.
 *  2. the code list was bound as ONE argument, so "a,b,c" never matched and
 *     every row was a deletion candidate.
 *  3. NOT IN returns no rows when the subquery contains a NULL, and mali
 *     routinely leaves one of its two person columns empty.
 *
 * The SQL is asserted as a string because the module has no Robolectric, so
 * SQLiteDatabase cannot run in a JVM test. buildDeleteOtherWhere/Args are
 * package-visible on PersonDataSource/KalaDataSource for exactly this reason.
 */
public class DeleteOtherClauseTest {

    private static List<PersonModel> persons(int yearId, String... codes) {
        List<PersonModel> list = new ArrayList<>();
        for (String code : codes) {
            PersonModel p = new PersonModel();
            p.setYearId_FK(yearId);
            p.setCode(code);
            list.add(p);
        }
        return list;
    }

    private static List<KalaModel> kalas(int yearId, String... codes) {
        List<KalaModel> list = new ArrayList<>();
        for (String code : codes) {
            KalaModel k = new KalaModel();
            k.setYearId_FK(yearId);
            k.setCode(code);
            list.add(k);
        }
        return list;
    }

    // region fault 2 - one placeholder per code

    @Test
    public void personWhere_hasOnePlaceholderPerCode() {
        String where = PersonDataSource.buildDeleteOtherWhere(3);
        assertTrue("expected a three-placeholder IN list, got: " + where,
                where.contains("NOT IN(?,?,?)"));
    }

    @Test
    public void personWhere_singleCodeStillUsesOnePlaceholder() {
        assertTrue(PersonDataSource.buildDeleteOtherWhere(1).contains("NOT IN(?)"));
    }

    @Test
    public void personArgs_areYearIdThenEachCodeSeparately() {
        String[] args = PersonDataSource.buildDeleteOtherArgs(
                persons(7, "630100404", "630100002"));

        // the year id is bound too, rather than concatenated into the SQL
        assertEquals(Arrays.asList("7", "630100404", "630100002"), Arrays.asList(args));
    }

    @Test
    public void personArgs_neverJoinCodesIntoOneArgument() {
        for (String arg : PersonDataSource.buildDeleteOtherArgs(persons(1, "A", "B", "C")))
            assertFalse("codes must not be comma-joined into one bind: " + arg,
                    arg.contains(","));
    }

    // endregion

    // region fault 1 - every foreign key that points at person is guarded

    @Test
    public void personWhere_guardsMpFaktorReference() {
        assertTrue(PersonDataSource.buildDeleteOtherWhere(1).contains("mpfaktor.personId_FK"));
    }

    @Test
    public void personWhere_guardsBothMaliReferences() {
        String where = PersonDataSource.buildDeleteOtherWhere(1);
        // the pair missed when the mali form was added - the actual cause of 787
        assertTrue("mali bed column not guarded", where.contains("mali.personBedId_FK"));
        assertTrue("mali bes column not guarded", where.contains("mali.personBesdId_FK"));
    }

    // endregion

    // region fault 3 - NULL-safe reference checks

    @Test
    public void personWhere_usesNotExistsSoNullsDoNotVoidTheDelete() {
        String where = PersonDataSource.buildDeleteOtherWhere(1);
        assertTrue("reference checks must use NOT EXISTS", where.contains("NOT EXISTS"));
        assertFalse("NOT IN(SELECT ...) is not NULL-safe",
                where.replace("NOT IN(?)", "").contains("NOT IN(SELECT"));
    }

    // endregion

    // region kala - same placeholder fault, FK coverage already complete

    @Test
    public void kalaWhere_hasOnePlaceholderPerCode() {
        assertTrue(KalaDataSource.buildDeleteOtherWhere(2).contains("NOT IN(?,?)"));
    }

    @Test
    public void kalaWhere_guardsSpFaktorReferenceNullSafely() {
        String where = KalaDataSource.buildDeleteOtherWhere(1);
        assertTrue(where.contains("NOT EXISTS"));
        assertTrue(where.contains("spfaktor.kalaId_FK"));
    }

    @Test
    public void kalaArgs_areYearIdThenEachCodeSeparately() {
        String[] args = KalaDataSource.buildDeleteOtherArgs(kalas(4, "500100008", "500100009"));
        assertEquals(Arrays.asList("4", "500100008", "500100009"), Arrays.asList(args));
    }

    // endregion

    // region arg/placeholder agreement - a mismatch is a runtime SQLite error

    @Test
    public void personArgCountMatchesPlaceholderCount() {
        List<PersonModel> list = persons(2, "A", "B", "C", "D");
        String where = PersonDataSource.buildDeleteOtherWhere(list.size());
        assertEquals(countChar(where, '?'),
                PersonDataSource.buildDeleteOtherArgs(list).length);
    }

    @Test
    public void kalaArgCountMatchesPlaceholderCount() {
        List<KalaModel> list = kalas(2, "A", "B", "C");
        String where = KalaDataSource.buildDeleteOtherWhere(list.size());
        assertEquals(countChar(where, '?'),
                KalaDataSource.buildDeleteOtherArgs(list).length);
    }

    private static int countChar(String s, char c) {
        int n = 0;
        for (char ch : s.toCharArray())
            if (ch == c)
                n++;
        return n;
    }

    // endregion
}
