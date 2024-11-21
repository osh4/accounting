package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
