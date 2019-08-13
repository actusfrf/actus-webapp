package org.actus.webapp.repositories;

import org.actus.webapp.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository   extends CrudRepository<Event, String> {
    @Override
    void delete(Event deleted);
}
