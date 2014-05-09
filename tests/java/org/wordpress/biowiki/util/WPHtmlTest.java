package org.wordpress.biowiki.util;

import android.test.InstrumentationTestCase;
import android.text.SpannableStringBuilder;

import kr.kdev.dg1s.biowiki.util.BWHtml;
import kr.kdev.dg1s.biowiki.util.HtmlToSpannedConverter;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WPHtmlTest extends InstrumentationTestCase {
    @Override
    protected void setUp() {
    }

    @Override
    protected void tearDown() {
    }

    // This test failed before #685 was fixed (throws a InvocationTargetException)
    public void testStartImg() throws NoSuchMethodException, IllegalAccessException {
        SpannableStringBuilder text = new SpannableStringBuilder();
        Attributes attributes = new AttributesImpl();

        // startImg is private, we use reflection to change accessibility and invoke it from here
        Method method = HtmlToSpannedConverter.class.getDeclaredMethod("startImg", SpannableStringBuilder.class,
                Attributes.class, BWHtml.ImageGetter.class);
        method.setAccessible(true);
        try {
            method.invoke(null, text, attributes, null);
        } catch (InvocationTargetException e) {
            assertTrue("startImg failed see #685", false);
        }
    }
}