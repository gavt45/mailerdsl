/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.gumirov.gav.mailerdsl;

import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    @Test public void testSomeLibraryMethod() {
        MailerDSL classUnderTest = new MailerDSL();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }

    @Test public void testAll(){
        MailerDSL classUnderTest = new MailerDSL();
        assertTrue(classUnderTest.init("https://127.0.0.1:443", "secret"));
        assertNotEquals("", classUnderTest.send("andreyt45@gmail.com", "subject", "TEST in junit"));
    }
}
