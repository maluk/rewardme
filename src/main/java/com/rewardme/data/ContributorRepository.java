package com.rewardme.data;

import com.rewardme.data.entity.Contributor;
import com.rewardme.data.entity.Recipient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContributorRepository extends CrudRepository<Contributor, Long>  {
    List<Contributor> findByUuid(String uuid);
}
