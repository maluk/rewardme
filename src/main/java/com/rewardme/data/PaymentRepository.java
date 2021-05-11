package com.rewardme.data;

import com.rewardme.data.entity.Payment;
import com.rewardme.data.entity.Recipient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long>  {
}
