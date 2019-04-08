package com.QueryBuilder;

import static java.lang.System.exit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.QueryBuilder.reader.QueryReader;

@SpringBootApplication
public class QueryBuilderApplication implements CommandLineRunner{
	
	@Autowired
    private QueryReader queryReader;

	public static void main(String[] args) {
		SpringApplication.run(QueryBuilderApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {

    	queryReader.readQuery();

        exit(0);
    }

}
