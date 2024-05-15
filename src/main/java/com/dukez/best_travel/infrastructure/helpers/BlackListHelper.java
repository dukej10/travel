package com.dukez.best_travel.infrastructure.helpers;

import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.dukez.best_travel.util.exceptions.ForbiddenCustomerException;

@Component
@AllArgsConstructor
@Transactional
public class BlackListHelper {
    public void isInBlackListCustomer(String customerId) {
        if (customerId.equals("WALA771012HCRGR054")) {
            throw new ForbiddenCustomerException("This customer is blocked");
        }
    }
}
