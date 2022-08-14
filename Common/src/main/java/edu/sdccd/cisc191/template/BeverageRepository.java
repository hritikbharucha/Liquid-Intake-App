package edu.sdccd.cisc191.template;

import org.springframework.data.repository.CrudRepository;

//repository for Beverages in database
public interface BeverageRepository extends CrudRepository<Beverage, Long> {

}
