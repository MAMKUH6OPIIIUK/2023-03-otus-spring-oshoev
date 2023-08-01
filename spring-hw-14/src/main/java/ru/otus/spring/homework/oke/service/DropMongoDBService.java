package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DropMongoDBService implements DropDBService {
    private final MongoTemplate mongoTemplate;

    @Override
    public void dropDb() {
        this.mongoTemplate.getDb().drop();
    }
}
