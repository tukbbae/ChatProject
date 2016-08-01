package com.hmlee.chatchat.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.Address;

import java.util.List;

/**
 * Created by hmlee
 */
@Repository
public interface AddressRepository extends DataTablesRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.phoneNumber = :phoneNumber")
    public Address findAddressByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT a FROM Address a WHERE a.regiId = :regiId")
    public Address findAddressByregiId(@Param("regiId") String regiId);

    @Query("SELECT a FROM Address a WHERE a.department = :department")
    public DataTablesOutput<Address> findAddressByDepartment(@Param("department") String department);

    @Query("SELECT a FROM Address a WHERE a.name LIKE :name")
    public List<Address> findAddressByName(@Param("name") String name);
}
