package com.vfsglobal.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetails {
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;
    private String cardName;
}

