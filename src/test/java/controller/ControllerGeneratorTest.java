package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerGeneratorTest {
	@DisplayName("리소스 파일 컨트롤러 생성")
	@Test
	void generateControllerWithResource() {
		String path = "/index.html";
		Controller controller = ControllerGenerator.generateController(path);
		assertThat(controller).isInstanceOf(ResourceController.class);
	}

	@DisplayName("회원가입 시 CreatUserController 생성")
	@Test
	void generateControllerWhenCreateUser() {
		String path = "/user/create";
		Controller controller = ControllerGenerator.generateController(path);
		assertThat(controller).isInstanceOf(CreateUserController.class);
	}
}