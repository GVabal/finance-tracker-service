package dev.vabalas.financetrackerservice.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EntryDto {
    @Pattern(regexp = "(INCOME)|(EXPENSE)", flags = {Pattern.Flag.CASE_INSENSITIVE},
            message = "Type can only be INCOME or EXPENSE.")
    private final String entryType;

    @NotNull(message = "Date must be specified.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}", flags = {Pattern.Flag.CASE_INSENSITIVE},
            message = "Date format must be yyyy-MM-ddThh:mm")
    private final String time;

    @NotBlank(message = "Category cannot be empty.")
    private final String category;

    @NotNull(message = "Amount must be specified.")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private final BigDecimal amount;

    public Entry toEntry() {
        return new Entry(
                EntryType.valueOf(entryType.toUpperCase()),
                LocalDateTime.parse(time),
                category,
                amount
        );
    }
}
