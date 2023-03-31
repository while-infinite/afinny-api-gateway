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
@Schema(description = "Response DTO about creating a new payment or transfer")
public class CreatePaymentResponseDto {
    private String id;
    private String first_name;
    private String last_name;
    private String middle_name;
    private String created_at;
    private String status;
    private String card_number;
    private String transfer_type_id;
    private String sum;
    private String remitter_card_number;
    private String name;
    private String payee_account_number;
    private String payee_card_number;
    private String mobile_phone;
    private String sum_commission;
    private String authorizationCode;
    private String currencyExchange;
    private String purpose;
    private String inn;
    private String bic;

}
