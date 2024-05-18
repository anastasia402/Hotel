package Service;
import Repository.IRepository;

import java.util.Collection;

public interface IService<Entity, Tid> {

    void add(Entity elem);
    void delete(Entity elem);
    void update(Entity elem, Tid id);
    Collection<Entity> getAll();

}
