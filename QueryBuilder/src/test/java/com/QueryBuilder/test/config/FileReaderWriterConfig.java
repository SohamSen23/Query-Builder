package com.QueryBuilder.test.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.QueryBuilder.communicator.FileReaderWriter;

@Configuration
@Profile("testFileReaderWriter")
public class FileReaderWriterConfig {

	@Bean
	@Primary
    public FileReaderWriter orderService() {
        return Mockito.mock(FileReaderWriter.class);
    }
}
