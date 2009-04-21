package org.sqlite;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * These tests check whether access to files is woring correctly and some
 * Connection.close() cases.
 */
public class ConnectionTest
{
    @BeforeClass
    public static void forName() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
    }

    @Test
    public void openMemory() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:");
        conn.close();
    }

    @Test
    public void isClosed() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:");
        conn.close();
        assertTrue(conn.isClosed());
    }

    @Test
    public void openFile() throws SQLException
    {
        File testdb = new File("test.db");
        if (testdb.exists())
            testdb.delete();
        assertFalse(testdb.exists());
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        conn.close();
        assertTrue(testdb.exists());
        testdb.delete();
    }

    @Test(expected = SQLException.class)
    public void closeTest() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:");
        PreparedStatement prep = conn.prepareStatement("select null;");
        ResultSet rs = prep.executeQuery();
        conn.close();
        prep.clearParameters();
    }

    @Test
    public void openInvalidLocation() throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/");
        conn.close();
    }
}
