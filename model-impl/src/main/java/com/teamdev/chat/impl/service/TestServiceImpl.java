package com.teamdev.chat.impl.service;

import com.teamdev.chat.entity.Cat;
import com.teamdev.chat.hrepository.TestRepository;
import com.teamdev.chat.service.TestService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.Iterator;

@Service
public class TestServiceImpl implements TestService {

    @Inject
    TestRepository testRepository;

    @Override
    public void sayHello() {
        final Cat user1 = new Cat("user1", "222", new Date());
        final Cat user2 = new Cat("user2", "222", new Date());
        testRepository.save(user1);
        testRepository.save(user2);
        final Iterable<Cat> all = testRepository.findAll();
        final Iterator<Cat> iterator = all.iterator();
    }
}
