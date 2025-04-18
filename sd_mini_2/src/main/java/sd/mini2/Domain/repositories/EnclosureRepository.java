package sd.mini2.Domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import sd.mini2.Domain.model.Enclosure;

public interface EnclosureRepository {
    Enclosure save(Enclosure enclosure);
    void remove(Enclosure enclosure);
    Optional<Enclosure> findById(UUID id);
    List<Enclosure> findAll();
}
