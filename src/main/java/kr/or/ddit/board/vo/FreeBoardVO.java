package kr.or.ddit.board.vo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.or.ddit.atch.vo.AtchFileDetailVO;
import kr.or.ddit.atch.vo.AtchFileVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "boNo")
public class FreeBoardVO {
	private int rnum;

	private Integer boNo;
	@NotBlank
	@Size(max = 200)
	private String boTitle;
	@NotBlank
	@Size(max = 80)
	private String boWriter;
	@NotBlank
	@Size(max = 50)
	private String boIp;
	@Email
	@Size(max = 200)
	private String boMail;
	@NotBlank
	@Size(max = 200)
	private String boPass;
	@Size(max = 4000)
	private String boContent;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime boDate;

	private int boHit;

	@Nullable
	private Integer atchFileId;

	@JsonIgnore
	@ToString.Exclude
	@Nullable
	@Valid
	private AtchFileVO atchFile;
	
	@JsonIgnore
	@ToString.Exclude
	private MultipartFile[] uploadFiles;
	public void setUploadFiles(MultipartFile[] uploadFiles) {
		List<AtchFileDetailVO> fileDetails = Optional.ofNullable(uploadFiles)
													.map(Arrays::stream)
													.orElse(Stream.empty())
													.filter(f->!f.isEmpty())	
													.map(AtchFileDetailVO::new)
													.collect(Collectors.toList());
		if(!fileDetails.isEmpty()) {
			this.uploadFiles = uploadFiles;
			atchFile = new AtchFileVO();
			atchFile.setFileDetails(fileDetails);
		}
	}

// 첨부파일 한개의 bean property path 모델명.atchFile.fileDetails[0].uploadFile
}





















