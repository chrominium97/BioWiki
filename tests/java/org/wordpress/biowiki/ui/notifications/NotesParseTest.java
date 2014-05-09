package org.wordpress.biowiki.ui.notifications;

import android.text.SpannableStringBuilder;

import junit.framework.TestCase;

import kr.kdev.dg1s.biowiki.util.HtmlUtils;

public class NotesParseTest extends TestCase {
    public void testParagraphInListItem1() {
        String text = "<li><p>Paragraph in li</p></li>";
        SpannableStringBuilder html = HtmlUtils.fromHtml(text);
        // if this didn't throw a RuntimeException we're ok
        assertNotNull(html);
    }

    // Trying to reproduce https://github.com/wordpress-mobile/WordPress-Android/issues/900
    public void testSpanInListItem1() {
        String text = "<ul><li><span>Current Record: </span><span>20</span></li><li><span>Old Record: </span><span>1</span></li></ul>";
        SpannableStringBuilder ssb = HtmlUtils.fromHtml(text);
        assertEquals("Current Record: 20\nOld Record: 1\n", ssb.toString());
    }

    public void testSpanInListItemFullTest() {
        String text = "<p>Au Mercredi 18 septembre 2013 vous avez pulvérisé votre précédent record de follows enregistrés en un seul jour, sur votre blog <a href=\"http://taliwutblog.wordpress.com\" title=\"taliwut &amp; blog\" target=\"_blank\" notes-data-click=\"best_period_ever_feat\">taliwut &amp; blog</a>. Super!</p><ul><li><span  class=\"wpn-feat-current-record-title\">Current Record: </span><span class=\"wpn-feat-new-record-count\">20</span></li><li><span  class=\"wpn-feat-old-record-title\">Old Record: </span><span class=\"wpn-feat-old-record-count\">1</span></li></ul>";
        SpannableStringBuilder ssb = HtmlUtils.fromHtml(text);
        assertTrue(ssb.toString().contains("Current Record: 20\nOld Record: 1\n"));
    }
}