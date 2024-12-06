package kr.or.ddit.board.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.annotation.RootContextWebConfig;
import kr.or.ddit.atch.vo.AtchFileDetailVO;
import kr.or.ddit.atch.vo.AtchFileVO;
import kr.or.ddit.board.exception.WriterAuthenticationException;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.paging.PaginationInfo;
import kr.or.ddit.paging.SimpleCondition;

@RootContextWebConfig
@Transactional
class BoardServiceTest {

	@Autowired
	BoardService service;

	FreeBoardVO boardWithFiles;
	FreeBoardVO boardWithNoFiles;
	static final String PLAINPASS = "java";

	@BeforeEach
	void beforeEach() throws IllegalAccessException, InvocationTargetException {
		boardWithFiles = new FreeBoardVO();
		boardWithNoFiles = new FreeBoardVO();
		boardWithFiles.setBoTitle("새글 제목");
		boardWithFiles.setBoWriter("작성자");
		boardWithFiles.setBoPass(PLAINPASS);
		boardWithFiles.setBoIp("192.168.0.1");
		boardWithFiles.setBoMail("aa@gmail.com");
		boardWithFiles.setBoContent("새글의 내용");

		BeanUtils.copyProperties(boardWithFiles, boardWithNoFiles);

		AtchFileVO atchFile = new AtchFileVO();
		MockMultipartFile file1 = new MockMultipartFile("atchFile.fileDetails[0].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		MockMultipartFile file2 = new MockMultipartFile("atchFile.fileDetails[1].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		atchFile.setFileDetails(Arrays.asList(new AtchFileDetailVO(file1), new AtchFileDetailVO(file2)));

		assertDoesNotThrow(() -> {
			boardWithFiles.setAtchFile(atchFile);
			service.createBoard(boardWithFiles);
			boardWithFiles.setBoPass(PLAINPASS);
		});
		assertDoesNotThrow(() -> {
			boardWithNoFiles.setAtchFile(null);
			service.createBoard(boardWithNoFiles);
			boardWithNoFiles.setBoPass(PLAINPASS);
		});

	}

	@AfterEach
	void afterEach() {
		assertDoesNotThrow(() -> {
			Optional.ofNullable(boardWithFiles)
					.map(FreeBoardVO::getAtchFile)
					.map(AtchFileVO::getFileDetails)
					.map(List::stream).orElse(Stream.empty())
					.filter(fd -> fd.getFileStreCours() != null)
					.forEach(fd -> FileUtils.deleteQuietly(new File(fd.getFileStreCours())));
			Optional.ofNullable(boardWithNoFiles)
					.map(FreeBoardVO::getAtchFile)
					.map(AtchFileVO::getFileDetails)
					.map(List::stream).orElse(Stream.empty())
					.filter(fd -> fd.getFileStreCours() != null)
					.forEach(fd -> FileUtils.deleteQuietly(new File(fd.getFileStreCours())));
		});
	}

	@Test
	void testCreateBoardNoFiles() throws IOException {
	}

	@Test
	void testCreateBoardWithFiles() throws IOException {
	}

	@Test
	void testReadBoard() {
		assertNotNull(service.readBoard(boardWithFiles.getBoNo()));
		assertEquals(2, boardWithFiles.getAtchFile().getFileDetails().size());

	}

	@Test
	void testReadBoardList() {
		PaginationInfo<FreeBoardVO> paging = new PaginationInfo<>();
		paging.setCurrentPage(1);
		SimpleCondition simpleCondition = new SimpleCondition();
		simpleCondition.setSearchWord("은대");
		paging.setSimpleCondition(simpleCondition);
		assertDoesNotThrow(() -> service.readBoardList(paging));
	}

	@Test
	void testWriterAuthenticate() {
		assertDoesNotThrow(() -> service.writerAuthenticate(boardWithFiles.getBoNo(), PLAINPASS));
		assertThrows(WriterAuthenticationException.class,
				() -> service.writerAuthenticate(boardWithFiles.getBoNo(), "afasdf"));
	}

	/**
	 * 기존 파일 그룹이 있고, 신규 파일 그룹이 있는 경우.
	 */
	@Test
	void testModifyBoardCase1() {
		AtchFileVO addAtchFile = new AtchFileVO();
		MockMultipartFile file1 = new MockMultipartFile("atchFile.fileDetails[0].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		MockMultipartFile file2 = new MockMultipartFile("atchFile.fileDetails[1].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		addAtchFile.setFileDetails(Arrays.asList(new AtchFileDetailVO(file1), new AtchFileDetailVO(file2)));
		boardWithFiles.setAtchFile(addAtchFile);

		assertDoesNotThrow(() -> service.modifyBoard(boardWithFiles));
		boardWithFiles = service.readBoard(boardWithFiles.getBoNo());
		assertEquals(4, boardWithFiles.getAtchFile().getFileDetails().size());
	}

	/**
	 * 기존 파일 그룹이 없고, 신규 파일 그룹이 있는 경우.
	 */
	@Test
	void testModifyBoardCase2() {
		AtchFileVO addAtchFile = new AtchFileVO();
		MockMultipartFile file1 = new MockMultipartFile("atchFile.fileDetails[0].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		MockMultipartFile file2 = new MockMultipartFile("atchFile.fileDetails[1].boFiles", "test.jpg", "image/jpeg",
				ArrayUtils.EMPTY_BYTE_ARRAY);
		addAtchFile.setFileDetails(Arrays.asList(new AtchFileDetailVO(file1), new AtchFileDetailVO(file2)));
		boardWithNoFiles.setAtchFile(addAtchFile);

		assertDoesNotThrow(() -> service.modifyBoard(boardWithNoFiles));
		boardWithNoFiles = service.readBoard(boardWithNoFiles.getBoNo());
		assertEquals(2, boardWithNoFiles.getAtchFile().getFileDetails().size());
	}

	/**
	 * 기존 파일 그룹이 있고, 신규 파일 그룹이 없는 경우.
	 */
	@Test
	void testModifyBoardCase3() {
		boardWithFiles.setAtchFile(null);

		assertDoesNotThrow(() -> service.modifyBoard(boardWithFiles));
		boardWithFiles = service.readBoard(boardWithFiles.getBoNo());
		assertEquals(2, boardWithFiles.getAtchFile().getFileDetails().size());
	}

	/**
	 * 기존 파일 그룹과 신규 파일 그룹이 모두 없는 경우.
	 */
	@Test
	void testModifyBoardCase4() {

		assertDoesNotThrow(() -> service.modifyBoard(boardWithNoFiles));

		assertNull(service.readBoard(boardWithNoFiles.getBoNo()).getAtchFile());
	}

	@Test
	void testRemoveBoard() {
		assertThrows(WriterAuthenticationException.class,
				() -> service.removeBoard(boardWithFiles.getBoNo(), "asfasdf"));
		assertDoesNotThrow(() -> service.removeBoard(boardWithFiles.getBoNo(), PLAINPASS));
	}

	@Test
	void testDownload() {
		assertDoesNotThrow(() -> 
			Optional.of(service.readBoard(boardWithFiles.getBoNo()))
					.map(FreeBoardVO::getAtchFile)
					.map(AtchFileVO::getFileDetails)
					.map(List::stream)
					.orElse(Stream.empty())
					.map(fd -> service.download(boardWithFiles.getAtchFileId(), fd.getFileSn()).getSavedFile())
					.allMatch(sf->sf.exists())
		);
								
	}
}
