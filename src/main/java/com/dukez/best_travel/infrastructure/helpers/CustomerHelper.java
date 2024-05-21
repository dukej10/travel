package com.dukez.best_travel.infrastructure.helpers;

import org.springframework.stereotype.Component;

import com.dukez.best_travel.domain.repositories.jpa.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@Component
@AllArgsConstructor
public class CustomerHelper {
    private final CustomerRepository customerRepository;

    public void incrase(String customerId, Class<?> type) {
        var customerToupdate = this.customerRepository.findById(customerId).orElseThrow();

        switch (type.getSimpleName()) {
            case "TourService":
                customerToupdate.setTotalTours(customerToupdate.getTotalTours() + 1);
                break;
            case "TicketService":
                customerToupdate.setTotalFlights(customerToupdate.getTotalFlights() + 1);
                break;
            default:
                customerToupdate.setTotalLodgings(customerToupdate.getTotalLodgings() + 1);
                break;
        }
        this.customerRepository.save(customerToupdate);
    }
}
