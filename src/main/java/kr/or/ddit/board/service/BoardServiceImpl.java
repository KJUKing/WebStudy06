package kr.or.ddit.board.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import kr.or.ddit.atch.service.AtchFileService;
import kr.or.ddit.atch.vo.AtchFileDetailVO;
import kr.or.ddit.atch.vo.AtchFileVO;
import kr.or.ddit.board.exception.BoardException;
import kr.or.ddit.board.exception.WriterAuthenticationException;
import kr.or.ddit.board.mapper.BoardMapper;
import kr.or.ddit.board.vo.FreeBoardVO;
import kr.or.ddit.paging.PaginationInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardMapper mapper;
	private final AtchFileService atchFileService;
	private final PasswordEncoder encoder;
// DirectoryInfo 설정 및 properties 파읿 빈 등록(context-common.xml)
	@Value("#{dirInfo.saveDir}")
	private Resource saveFolderRes;
	private File saveFolder;

	@PostConstruct
	public void init() throws IOException {
		this.saveFolder = saveFolderRes.getFile();
	}

	@Override
	public void createBoard(final FreeBoardVO board) {
		Integer atchFileId = Optional.ofNullable(board.getAtchFile())
				.filter(af->! CollectionUtils.isEmpty(af.getFileDetails()))
				.map(af -> {
					atchFileService.createAtchFile(af, saveFolder);
					return af.getAtchFileId();
				}).orElse(null);
		
		board.setAtchFileId(atchFileId);
		board.setBoPass(encoder.encode(board.getBoPass()));
		mapper.insertBoard(board);
	}

	@Override
	public FreeBoardVO readBoard(int boNo) {
		FreeBoardVO board = mapper.selectBoard(boNo);
		if (board == null)
			throw new BoardException(String.format("%d 번 글이 없음.", boNo));
		mapper.incrementHit(boNo);
		return board;
	}

	@Override
	public List<FreeBoardVO> readBoardList() {
		return mapper.selectBoardListNonPaging();
	}

	@Override
	public List<FreeBoardVO> readBoardList(PaginationInfo<FreeBoardVO> paging) {
		paging.setTotalRecord(mapper.selectTotalRecord(paging));
		List<FreeBoardVO> boardList = mapper.selectBoardList(paging);
		return boardList;
	}

	@Override
	public FreeBoardVO writerAuthenticate(int boNo, String rawPassword) throws WriterAuthenticationException {
		FreeBoardVO authBoard = readBoard(boNo);
		String savedPassword = authBoard.getBoPass();
		if (!encoder.matches(rawPassword, savedPassword))
			throw new WriterAuthenticationException("비밀번호 오류, 인증실패");
		return authBoard;
	}

	/**
	 * 기존의 첨부파일 그룹이 있는 경우, 신규 파일과 기존 파일 그룹을 병합해 저장함.
	 * 
	 * @param atchFileId
	 */
	private Integer mergeSavedDetailsAndNewDetails(AtchFileVO savedAtchFile, AtchFileVO newAtchFile) {
		AtchFileVO mergeAtchFile = new AtchFileVO();
		List<AtchFileDetailVO> fileDetails = Stream.concat(
			Optional.ofNullable(savedAtchFile)
					.filter(saf->! CollectionUtils.isEmpty(saf.getFileDetails()))
					.map(saf->saf.getFileDetails().stream())
					.orElse(Stream.empty())
			, Optional.ofNullable(newAtchFile)
					.filter(naf->! CollectionUtils.isEmpty(naf.getFileDetails()))
					.map(naf->naf.getFileDetails().stream())
					.orElse(Stream.empty())
		).collect(Collectors.toList());		
				
		mergeAtchFile.setFileDetails(fileDetails);
		
		if( ! mergeAtchFile.getFileDetails().isEmpty() ) {
			atchFileService.createAtchFile(mergeAtchFile, saveFolder);
		}
		
		if (savedAtchFile != null && savedAtchFile.getFileDetails() != null) {
			// 기존 첨부파일 그룹은 비활성화
			atchFileService.disableAtchFile(savedAtchFile.getAtchFileId());
		}

		return mergeAtchFile.getAtchFileId();
	}

	@Override
	public void modifyBoard(final FreeBoardVO board) {
		final FreeBoardVO saved = writerAuthenticate(board.getBoNo(), board.getBoPass());

		Integer newAtchFileId = Optional.ofNullable(board.getAtchFile())
										.filter(af -> af.getFileDetails() != null)
										.map(af ->mergeSavedDetailsAndNewDetails(saved.getAtchFile(), af))
										.orElse(null);
		
		board.setAtchFileId(newAtchFileId);
		mapper.updateBoard(board);
	}

	@Override
	public void removeBoard(int boNo, String boPass) {
		FreeBoardVO saved = writerAuthenticate(boNo, boPass);
		Optional.ofNullable(saved.getAtchFileId())
				.ifPresent(fid -> atchFileService.disableAtchFile(fid));
		mapper.deleteBoard(saved.getBoNo());
	}

	@Override
	public AtchFileDetailVO download(int atchFileId, int fileSn) {
		return Optional.ofNullable(atchFileService.readAtchFileDetail(atchFileId, fileSn, saveFolder))
						.filter(fd -> fd.getSavedFile().exists())
						.orElseThrow(() -> new BoardException(String.format("[%d, %d]해당 파일이 없음.", atchFileId, fileSn)));
	}

	@Override
	public void removeFile(int atchFileId, int fileSn) {
		atchFileService.removeAtchFileDetail(atchFileId, fileSn, saveFolder);
	}
}
