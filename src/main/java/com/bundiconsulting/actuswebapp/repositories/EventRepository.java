package com.bundiconsulting.actuswebapp.repositories;

import com.bundiconsulting.actuswebapp.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository   extends CrudRepository<Event, String> {
    @Override
    void delete(Event deleted);
}
