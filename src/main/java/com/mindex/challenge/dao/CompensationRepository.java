package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    Compensation findCompensationByEmployeeEmployeeId(String employeeId);

}
