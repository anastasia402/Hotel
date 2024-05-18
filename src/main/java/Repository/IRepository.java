package Repository;

import Domain.Entity;

import java.util.Collection;

public interface IRepository<Entity, Tid> {

    void add(Entity elem);
    void delete(Entity elem);
    void update(Entity elem, Tid id);
    Collection<Entity> getAll();

}
