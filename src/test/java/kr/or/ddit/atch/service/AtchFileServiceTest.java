package kr.or.ddit.atch.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.function.Failable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
class AtchFileServiceTest {
	@Autowired
	AtchFileService service;
	static File saveFolder;

	@BeforeAll
	static void beforeAll() {
		saveFolder = new File("d:/testFolder");
		if (!saveFolder.exists())
			saveFolder.mkdirs();
	}

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
		atchFile.getFileDetails().forEach(Failable.asConsumer(fd -> fd.uploadFileSaveTo(saveFolder)));
		assertDoesNotThrow(() -> service.createAtchFile(atchFile, saveFolder));
	}

	@AfterAll
	static void afterAll() throws IOException {
		FileUtils.deleteDirectory(saveFolder);
	}

	@Test
	void testReadAtchFileEnable() {
		atchFile = service.readAtchFileEnable(atchFile.getAtchFileId(), saveFolder);
		assertNotNull(atchFile);
	}

	@Test
	void testReadAtchFile() {
		atchFile = service.readAtchFile(atchFile.getAtchFileId(), true, saveFolder);
		assertNotNull(atchFile);
		assertNull(service.readAtchFile(atchFile.getAtchFileId(), false, saveFolder));
	}

	@Test
	void testReadAtchFileDetail() {
		AtchFileDetailVO detail = service.readAtchFileDetail(atchFile.getAtchFileId(), 1, saveFolder);
		assertNotNull(detail);
		assertTrue(detail.getSavedFile().exists());
	}

	@Test
	void testRemoveAtchFileDetail() {
		assertDoesNotThrow(() -> service.removeAtchFileDetail(atchFile.getAtchFileId(), 1, saveFolder));
		assertDoesNotThrow(() -> service.removeAtchFileDetail(atchFile.getAtchFileId(), 2, saveFolder));
	}

	@Test
	void testDisableAtchFile() {
		assertDoesNotThrow(() -> service.disableAtchFile(atchFile.getAtchFileId()));
	}

	@Test
	void testRemoveAtchFile() {
		assertDoesNotThrow(() -> service.disableAtchFile(atchFile.getAtchFileId()));
		assertDoesNotThrow(() -> service.removeDiabledAtchFile(atchFile.getAtchFileId()));
	}
}
