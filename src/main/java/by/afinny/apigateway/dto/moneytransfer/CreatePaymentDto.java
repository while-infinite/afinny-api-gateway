package by.afinny.apigateway.dto.moneytransfer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PUBLIC)
@ToString
@Schema(description = "DTO to create a new payment or transfer")
public class CreatePaymentDto {
    private Integer transferTypeId;
    private String sum;
    private String remitterCardNumber;
    private String name;
    private String payeeAccountNumber;
    private String payeeCardNumber;
    private String inn;
    private String bic;
    private String mobilePhone;
    private String sumCommission;
    private String purpose;
}
