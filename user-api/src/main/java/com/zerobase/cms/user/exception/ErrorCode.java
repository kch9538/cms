package com.zerobase.cms.user.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	NOT_MATCH_EMAIL_PATTERN(HttpStatus.BAD_REQUEST, "올바른 이메일 형식을 입력해주세요"),
	NOT_MATCH_PHONE_PATTERN(HttpStatus.BAD_REQUEST, "올바른 연락처 형식을 입력해주세요"),
	NOT_MATCH_PASSWORD_PATTERN(HttpStatus.BAD_REQUEST, "올바른 비밀번호 형식을 입력해주세요"),
	ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
	ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
	WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
	EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
	LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디나 비밀번호를 확인해주세요."),
	NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다");

	private final HttpStatus httpStatus;
	private final String detail;
}
