package io.example.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by gmind on 2015-10-07.
 */
@Data
@MappedSuperclass
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private PK id;

    @Transient
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

}
