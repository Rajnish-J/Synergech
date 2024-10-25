package com.Check.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Check.Entity.DoctorVO;

@Repository
public interface DoctorRepo extends JpaRepository<DoctorVO, Long> {

	@Query("SELECT d.doctorId AS doctorId, d.firstName AS firstName, d.lastName AS lastName, s.specialtyName AS specialtyName "
			+ "FROM DoctorVO d JOIN d.specialization s")
	List<DoctorDetailsProjection> findDoctorDetailsWithSpecialization();

	// fetching all patients id for validation:
	@Query("SELECT d.doctorId AS doctorId FROM DoctorVO d")
	List<Long> fetchPatientId();

}
