package e_commerce.demo.dto;

import lombok.Data;

@Data
public class EmailConfirmationRequest {

    private String email;
    private String confirmationCode;
}
