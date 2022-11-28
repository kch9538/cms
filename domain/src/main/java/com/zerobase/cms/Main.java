package com.zerobase.cms;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@RequiredArgsConstructor
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		System.out.println("Hello world!");
	}
}