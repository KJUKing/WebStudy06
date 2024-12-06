package kr.or.ddit;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import kr.or.ddit.annotation.RootContextWebConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RootContextWebConfig
class DataSourceTest {

	@Inject
	DataSource dataSource;
	
	@Test
	void test() {
		log.info("주입된 객체 : {}", dataSource);
	}

}












