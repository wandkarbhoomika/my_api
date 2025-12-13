package com.project.insurance.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.insurance.api.entity.QuoteHistory;


@Repository
public interface QuoteRepository extends JpaRepository<QuoteHistory, Long> {
}
