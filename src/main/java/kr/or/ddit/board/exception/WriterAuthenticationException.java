package kr.or.ddit.board.exception;

/**
 * 작성자 인증에 실패한 경우에 대한 예외 정의
 *
 */
public class WriterAuthenticationException extends BoardException{

	public WriterAuthenticationException() {
		super();
		
	}

	public WriterAuthenticationException(int boNo) {
		super(boNo);
		
	}

	public WriterAuthenticationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public WriterAuthenticationException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public WriterAuthenticationException(String message) {
		super(message);
		
	}

	public WriterAuthenticationException(Throwable cause) {
		super(cause);
		
	}

}
