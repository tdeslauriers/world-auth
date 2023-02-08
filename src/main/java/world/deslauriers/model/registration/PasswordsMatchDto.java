package world.deslauriers.model.registration;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import world.deslauriers.validation.PasswordComplexity;

import javax.validation.constraints.NotBlank;

@Serdeable
public record PasswordsMatchDto(
        @NonNull @NotBlank @PasswordComplexity String password,
        @NonNull @NotBlank @PasswordComplexity String confirmPassword
) {
}
