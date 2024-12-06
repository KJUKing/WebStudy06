package kr.or.ddit.atch.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.annotation.RootContextWebConfig;
import kr.or.ddit.atch.vo.AtchFileDetailVO;
import kr.or.ddit.atch.vo.AtchFileVO;

@RootContextWebConfig
@Transactional
class AtchFileMapperTest {

	@Autowired
	AtchFileMapper mapper;

	AtchFileVO atchFile;

	@BeforeEach
	void beforeEach() {
		atchFile = new AtchFileVO();
		MockMultipartFile file1 = new MockMultipartFile("atchFile.fileDetails[0].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		MockMultipartFile file2 = new MockMultipartFile("atchFile.fileDetails[1].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		List<AtchFileDetailVO> fileDetails = Arrays.asList(new AtchFileDetailVO(file1), new AtchFileDetailVO(file2));
		atchFile.setFileDetails(fileDetails);
		atchFile.getFileDetails().forEach(fd -> fd.setFileStreCours("dummy 경로"));
		
		assertEquals(3, mapper.insertAtchFile(atchFile));
	}

	@Test
	void testSelectAtchFileDetail() {
		assertNotNull(mapper.selectAtchFileDetail(atchFile.getAtchFileId(), 2));
	}

	@Test
	void testSelectAtchFileEnable() {
		assertNotNull(mapper.selectAtchFileEnable(atchFile.getAtchFileId()));
	}

	@Test
	void testIncrementDowncount() {
		assertEquals(1, mapper.incrementDowncount(atchFile.getAtchFileId(), 2));
	}

	@Test
	void testDeleteAtchFileDetail() {
		assertEquals(1, mapper.deleteAtchFileDetail(atchFile.getAtchFileId(), 2));
	}

	@Test
	void testDisableAtchFile() {
		assertEquals(1, mapper.disableAtchFile(atchFile.getAtchFileId()));
	}

	@Test
	void testDeleteDisabledAtchFileDetails() {
		assertEquals(1, mapper.disableAtchFile(atchFile.getAtchFileId()));
		assertTrue(mapper.deleteDisabledAtchFileDetails(atchFile.getAtchFileId()) > 0);
	}

	@Test
	void testDeleteAtchFile() {
		assertEquals(1, mapper.disableAtchFile(atchFile.getAtchFileId()));
		assertEquals(1, mapper.deleteDisabledAtchFile(atchFile.getAtchFileId()));
	}

}
