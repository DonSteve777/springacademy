package com.rest.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface CashCardRepository extends CrudRepository<CashCard, Long>,
PagingAndSortingRepository<CashCard, Long> {


}
