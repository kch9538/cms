package com.zerobase.cms.client.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

	private Long id;
	private String name;
	private String email;
	private Integer balance;

}