package com.capatu.reminder_service.repository;

import com.capatu.reminder_service.entity.ReminderModel;
import com.capatu.reminder_service.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderModel, Long> {

    List<ReminderModel> findByStatus(ReminderStatus status);
}
