package org.wordpress.biowiki.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.test.RenamingDelegatingContext;

import kr.kdev.dg1s.biowiki.BioWikiDB;
import kr.kdev.dg1s.biowiki.models.Note;
import org.wordpress.biowiki.TestUtils;
import kr.kdev.dg1s.biowiki.BioWiki;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BioWikiDB_NotificationsTest extends InstrumentationTestCase {
    protected Context testContext;
    protected Context targetContext;

    @Override
    protected void setUp() {
        // Run tests in an isolated context
        targetContext = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        testContext = getInstrumentation().getContext();
    }

    public static Note createEmptyNote() {
        Bundle b = new Bundle();
        b.putString("title", "Hey");
        b.putString("msg", "Hoy");
        b.putString("type", "c");
        b.putString("icon", "");
        b.putString("noticon", "");
        b.putString("msg", "");
        b.putString("note_id", ""); // empty string note_id makes addNote() crash
        Note note = new Note(b);
        return note;
    }

    // This test reproduces #134 (crash when not fixed)
    public void testAddNote_issue134() {
        SQLiteDatabase db = TestUtils.loadDBFromDump(targetContext, testContext, "empty_tables.sql");
        BioWikiDB wpdb = BioWiki.wpDB;
        Note note = createEmptyNote();
        wpdb.addNote(note, true);
    }

    public void testGenerateNoteId() {
        SQLiteDatabase db = TestUtils.loadDBFromDump(targetContext, testContext, "empty_tables.sql");
        BioWikiDB wpdb = BioWiki.wpDB;

        wpdb.generateIdFor(null);
        Note note = createEmptyNote();
        int id = wpdb.generateIdFor(note); // -1452768546
    }

    public void testGetNoteById() {
        SQLiteDatabase db = TestUtils.loadDBFromDump(targetContext, testContext, "empty_tables.sql");
        BioWikiDB wpdb = BioWiki.wpDB;

        Note note = wpdb.getNoteById(12123);
    }

    public void testGetNoteById2() {
        SQLiteDatabase db = TestUtils.loadDBFromDump(targetContext, testContext, "empty_tables.sql");
        BioWikiDB wpdb = BioWiki.wpDB;

        Note note = createEmptyNote();
        wpdb.addNote(note, true);
        int id = BioWikiDB.generateIdFor(note);
        Note note2 = wpdb.getNoteById(id);
        assertEquals(note.getSubject(), note2.getSubject());
    }

    public void testAddNoteClearNotes()
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SQLiteDatabase db = TestUtils.loadDBFromDump(targetContext, testContext, "empty_tables.sql");
        BioWikiDB wpdb = BioWiki.wpDB;

        Note note = createEmptyNote();
        wpdb.addNote(note, true);
        ArrayList<Note> notes = wpdb.getLatestNotes();
        assertTrue(notes.size() >= 0);

        // call wpdb.clearNotes();
        Method method = BioWikiDB.class.getDeclaredMethod("clearNotes");
        method.setAccessible(true);
        method.invoke(wpdb);

        notes = wpdb.getLatestNotes();
        assertTrue(notes.size() == 0);
    }

    public void tearDown() throws Exception {
        targetContext = null;
        testContext = null;
        super.tearDown();
    }
}
