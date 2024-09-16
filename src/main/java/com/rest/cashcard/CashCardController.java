package com.rest.cashcard;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController     // capable of handling HTTP requests.
@RequestMapping("/cashcards")
class CashCardController {
    
    private CashCardRepository cashCardRepository;
    
    private CashCardController(CashCardRepository cashCardRepository){
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

/* 
URL
/cashcards?page=1&size=3&sort=amount,desc
*/ 
    @GetMapping()
    private ResponseEntity<Iterable<CashCard>> findAll(Pageable pageable){
        Page<CashCard> page = cashCardRepository.findAll(
            PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
));
        return ResponseEntity.ok(page.getContent());
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);

        /*
        MÉTODOS ADECUADOS para HTTP y REST menos verbosos

        return  ResponseEntity
        .created(uriOfCashCard)
        .build();

        VERSUS

        return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, uriOfCashCard.toASCIIString())
        .build();
        
         */

        URI locationOfNewCashCard = ucb
                    .path("cashcards/{id}")
                    .buildAndExpand(savedCashCard.id())
                    .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }
}