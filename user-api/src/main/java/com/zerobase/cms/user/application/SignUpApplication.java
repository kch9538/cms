package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.SignUpCustomerService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

	public void customerVerify (String email, String code){
		signUpCustomerService.verifyEmail(email, code);
	}

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;

	public String customerSignUp(SignUpForm form) {
		if (signUpCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
		} else {
			Customer c = signUpCustomerService.signUp(form);
			LocalDateTime now = LocalDateTime.now();

			String code = getRandomCode();
			SendMailForm sendMailForm = SendMailForm.builder()
				.from("tester@naver.com")
				.to(form.getEmail())
				.subject("Verification Email")
				.text(getVerificationEmailBody(form.getEmail(), form.getName(), getRandomCode()))
				.build();
			mailgunClient.sendEmail(sendMailForm);
			signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
			return "회원가입에 성공하였습니다.";
		}
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String code) {
		StringBuilder builder = new StringBuilder();
		return builder.append("Hello ")
			.append(name)
			.append("! Please Click Link for verification.\n\n ")
			.append("http://localhost:8080/signup/verify/customer?email=")
			.append(email)
			.append("&code=")
			.append(code).toString();
	}


}
