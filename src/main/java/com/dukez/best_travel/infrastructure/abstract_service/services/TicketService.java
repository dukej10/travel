package com.dukez.best_travel.infrastructure.abstract_service.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dukez.best_travel.api.models.request.TicketRequest;
import com.dukez.best_travel.api.models.response.FlyResponse;
import com.dukez.best_travel.api.models.response.TicketResponse;
import com.dukez.best_travel.domain.entities.TicketEntity;
import com.dukez.best_travel.domain.repositories.CustomerRepository;
import com.dukez.best_travel.domain.repositories.FlyRepository;
import com.dukez.best_travel.domain.repositories.TicketRepository;
import com.dukez.best_travel.infrastructure.abstract_service.ITickerService;
import com.dukez.best_travel.infrastructure.helpers.CustomerHelper;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITickerService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);

    // nota: se puede ahorrar el siguiente constructor si se utiliza e
    // @AllArgsConstructor
    // public TicketService(FlyRepository flyRepository, CustomerRepository
    // customerRepository,
    // TicketRepository ticketRepository) {
    // this.flyRepository = flyRepository;
    // this.customerRepository = customerRepository;
    // this.ticketRepository = ticketRepository;
    // }

    @Override
    public TicketResponse create(TicketRequest request) {
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        System.out.println("Fly: " + fly);
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        System.out.println("Customer: " + customer);
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .departureDate(LocalDateTime.now())
                .arrivalDate(LocalDateTime.now())
                .purchaseDate(LocalDate.now())
                .price(fly.getPrice().multiply(fly.getPrice().multiply(charger_price_percentage)))
                .fly(fly)
                .customer(customer)
                .build();
        var ticketPersist = ticketRepository.save(ticketToPersist);
        // Incrementar número de vuelos del cliente
        this.customerHelper.incrase(customer.getDni(), this.getClass());

        log.info("Ticket saved with id {}", ticketPersist.getId());
        return this.entityToResponse(ticketPersist);
    }

    @Override
    public TicketResponse read(UUID id) {
        var entity = ticketRepository.findById(id).orElseThrow();
        return entityToResponse(entity);

    }

    @Override
    public TicketResponse update(UUID id, TicketRequest request) {
        var ticketToUpdate = ticketRepository.findById(id).orElseThrow();
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketToUpdate.setDepartureDate(LocalDateTime.now());
        ticketToUpdate.setArrivalDate(LocalDateTime.now());
        ticketToUpdate.setPurchaseDate(LocalDate.now());
        var ticketUpdated = ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated with id {}", ticketUpdated.getId());
        return entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID id) {
        var ticketToDelete = ticketRepository.findById(id).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        log.info("Finding price for fly with id {}", flyId);
        // TODO Auto-generated method stub
        var fly = flyRepository.findById(flyId).orElseThrow();
        System.out.println("Fly: " + fly);
        return fly.getPrice().add(
                fly.getPrice().multiply(charger_price_percentage));
    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);

        return response;
    }

}
