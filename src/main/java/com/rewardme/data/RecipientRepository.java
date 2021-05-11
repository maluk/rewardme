package com.rewardme.data;

import com.rewardme.data.entity.Payment;
import com.rewardme.data.entity.Recipient;
import com.rewardme.data.entity.RecipientType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipientRepository extends CrudRepository<Recipient, Long>  {
    List<Recipient> findByName(String name);
}
