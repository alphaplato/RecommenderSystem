package com.plato.recoserver.recoserver.common;

import lombok.ToString;

import java.util.Objects;

/**
 * @author Kevin
 * @date 2022-03-14
 */
@ToString
public abstract class AbstractItem<T> implements Item<T> {

    private T id;
    protected int type;
    private double score;

    protected AbstractItem(T id) {
        this.id = id;
    }

    protected AbstractItem(T id, int type) {
        this.id = id;
        this.type = type;
    }

    protected AbstractItem(T id, int type, double score) {
        this.id = id;
        this.type = type;
        this.score = score;
    }

    @Override
    public T getId() {
        return id;
    }


    @Override
    public int type() {
        return this.type;
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id) ^ Integer.hashCode(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item<?> another = (Item<?>) obj;
            return type == another.type() && Objects.equals(id, another.getId());
        }
        return false;
    }
}
