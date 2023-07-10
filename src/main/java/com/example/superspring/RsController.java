package com.example.superspring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RsController implements RsInterface {

	@Autowired
	private DataSource dataSource;

	@Override
	public String index() {
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from my_table where mykey = 'name'");
			if (resultSet.next()){
				return "The result value is "+resultSet.getString("myvalue");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "No result found";
	}

}