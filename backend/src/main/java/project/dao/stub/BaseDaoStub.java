package project.dao.stub;

import org.springframework.data.domain.Sort;
import project.dao.BaseDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;


public abstract class BaseDaoStub<T, ID extends Serializable> implements BaseDao<T, ID> {

    final Map<Object, T> storage = new ConcurrentHashMap<>();

    protected abstract <S extends T> ID getId(S entity);

    @Override
    public T findOne(ID id) {
        return storage.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<T> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> S save(S entity) {
        return (S) storage.put(getId(entity), entity);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(ID id) {
        storage.remove(id);
    }

    @Override
    public void delete(T entity) {
        storage.remove(getId(entity));
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        if (entities == null) {
            return;
        }
        StreamSupport.stream(entities.spliterator(), false).peek((entity) -> delete(entity));
    }

    @Override
    public void deleteAll() {
        storage.clear();
    }

    @Override
    public long count() {
        return storage.size();
    }
}
