package ru.edu.Task5;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.edu.Task5.service.ClientFromMdm;
import ru.edu.Task5.service.InstanceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( classes = {Task5Application.class})
@AutoConfigureMockMvc
@TestPropertySource("/application.yml")
@Testcontainers
@Transactional
class Task5ApplicationTests {

	@Container
	private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:latest")
			// Создаём базу данных с указанным именем
			.withDatabaseName("postgrestest")
			// Указываем имя пользователя и пароль
			.withUsername("postgres")
			.withPassword("postgres")
			// Скрипт, который будет выполнен при запуске контейнера и наполнит базу тестовыми данными
			.withInitScript("script.sql");

	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {
		// Устанавливаем URL базы данных
		registry.add("spring.datasource.url", database::getJdbcUrl);
		// Имя пользователя и пароль для подключения
		registry.add("spring.datasource.username", database::getUsername);
		registry.add("spring.datasource.password", database::getPassword);
		// Эти значения приложение будет использовать при подключении к базе данных
	}

	@Mock
	ClientFromMdm clientMdm;

	@InjectMocks
	InstanceService instanceService;

	@Autowired
	MockMvc mockMvc;

	private String getJSonContentFromFile(String filePath) throws IOException {
		File file = ResourceUtils.getFile("classpath:" + filePath);
		return new String(Files.readAllBytes(file.toPath()));
	}

	@Test
	void createInstanceOkTest() throws Exception {

		String json = getJSonContentFromFile("CreateInstanceOk.json");

		this.mockMvc.perform(post("/corporate-settlement-instance/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
					.andDo(print())
					.andExpect(status().isOk());
	}

	@Test
	void createInstanceBadReqTest() throws Exception {

		String json = getJSonContentFromFile("CreateInstanceBadReq.json");

		this.mockMvc.perform(post("/corporate-settlement-instance/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
					.andDo(print())
					.andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountBadReqTest() throws Exception {

		this.mockMvc.perform(post("/corporate-settlement-account/create")
					.param("instanceId", "1")
					.param("registryTypeCode", "03.012.002_47533_ComSoLd")
					.param("accountType", "Клиентский")
					.param("currencyCode", "800")
					.param("branchCode", "0022")
					.param("priorityCode", "00")
					.param("mdmCode", "15")
					.param("clientCode", "123456"))
					.andExpect(status().isBadRequest());
	}

}
