package by.afinny.apigateway.dto.credit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter(AccessLevel.PUBLIC)
public class ResponseCreditOrderDocument {

    private UUID id;
    private UUID clientId;
    private UUID creditOrderId;
    private LocalDate creationDate;
    private String documentName;
    private String fileFormat;
    private byte[] file;
}
