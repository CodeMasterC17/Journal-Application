package net.engineeringdigest.journalapplication.dto;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty
//    @Schema(description = "The user's username")
    private String userName;
    private String email;
    private boolean sentimentAnalysis;
    @NotEmpty
    private String password;
}
