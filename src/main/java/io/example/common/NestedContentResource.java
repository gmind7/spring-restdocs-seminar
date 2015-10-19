package io.example.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

/**
 * Created by gmind on 2015-10-05.
 */
public class NestedContentResource<T> extends ResourceSupport {

    private final Resources<T> nested;

    public NestedContentResource(Iterable<T> toNested) {
        this.nested = new Resources<T>(toNested);
    }

    @JsonUnwrapped
    public Resources<T> getNested() {
        return this.nested;
    }
}
