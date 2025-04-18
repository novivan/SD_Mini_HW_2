package sd.mini2.Infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import sd.mini2.Domain.model.Enclosure;
import sd.mini2.Domain.repositories.EnclosureRepository;

@Repository
public class InMemoryEnclosureRepository implements EnclosureRepository {
    private final Map<UUID, Enclosure> enclosures = new ConcurrentHashMap<>();

    @Override
    public Enclosure save(Enclosure enclosure) {
        enclosures.put(enclosure.getId(), enclosure);
        return enclosure;
    }

    @Override
    public void remove(Enclosure enclosure) {
        enclosures.remove(enclosure.getId());
    }

    @Override
    public Optional<Enclosure> findById(UUID id) {
        return Optional.ofNullable(enclosures.get(id));
    }

    @Override
    public List<Enclosure> findAll() {
        return new ArrayList<>(enclosures.values());
    }
}