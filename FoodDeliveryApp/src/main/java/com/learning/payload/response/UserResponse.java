package com.learning.payload.response;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learning.payload.request.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {	// customized response for output
	@NotBlank
	private String name;
	@NotNull
	private String email;
	
	//@NotEmpty
	private Set<Address> address;
	@JsonFormat(pattern="MM-dd-yyyy")
	private LocalDate doj;
	@NotEmpty
	private Set<String> roles;
}
