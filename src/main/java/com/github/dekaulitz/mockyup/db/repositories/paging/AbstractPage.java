package com.github.dekaulitz.mockyup.db.repositories.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * for
 *
 * @param <T>
 */
public abstract class AbstractPage<T> implements BasePage<T>, Serializable {
    @Getter
    private final Query query = new Query();
    private final Query count = new Query();
    @Setter
    @Getter
    protected List<T> rows;
    @Setter
    @Getter
    protected int page = 1;
    @Setter
    @Getter
    protected int pageCount;
    @Setter
    @Getter
    protected int size = 10;
    @Setter
    @Getter
    protected long rowCount;
    @Setter
    @Getter
    private Criteria criteria;
    private MongoTemplate mongoTemplate;
    private Pageable pageable;

    @Autowired
    @Override
    public void setConnection(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AbstractPage<T> setPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    @Override
    public void addCriteria(String q) {
        if (q != null) {
            String[] search = q.split(":");
            if (search.length == 2 && !search[1].isEmpty()) {
                if (search[0].equals("_id") || search[0].equals("id")) {
                    this.criteria = Criteria.where(search[0]).regex(".*" + search[1] + ".*", "i");
                } else
                    this.criteria = Criteria.where(search[0]).regex(".*" + search[1] + ".*", "i");
            }
        }
    }

    //add additional criteria
    public void addAdditionalCriteria(Criteria criteria) {
        query.addCriteria(criteria);
        count.addCriteria(criteria);
    }

    public void build(Class<T> entityClass) {

        if (criteria != null) {
            query.addCriteria(criteria);
            count.addCriteria(criteria);
        }
        query.with(pageable);
        this.size = pageable.getPageSize();
        this.rows = mongoTemplate.find(query, entityClass);
        this.rowCount = mongoTemplate.count(count, entityClass);
        this.page = pageable.getPageNumber() + 1;
        this.pageCount = (int) Math.ceil(this.rowCount / (double) pageable.getPageSize());
    }
}
