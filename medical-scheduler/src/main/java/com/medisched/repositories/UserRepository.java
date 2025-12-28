package com.medisched.repositories;

import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    List<Doctor> findByLastName(String lastName);
}
