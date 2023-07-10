package com.example.superspring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/abc")
	public String index() {
		DataSource dataSource = SpringContext.getBean(DataSource.class);
				try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from my_table where mykey = 'name'");
			if (resultSet.next()){
				return "The result value in abc is "+resultSet.getString("myvalue");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Greetings from Spring Boot!";
	}

}