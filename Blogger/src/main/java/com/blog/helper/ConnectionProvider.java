package com.blog.helper;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {

	private static Connection con;

	public static Connection getConnection() {
		try {

			if (con == null) {
				// driver class load
				Class.forName("com.mysql.jdbc.Driver");

				// create a connection..
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/techblog", "root", "Tejas@1996");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

}
