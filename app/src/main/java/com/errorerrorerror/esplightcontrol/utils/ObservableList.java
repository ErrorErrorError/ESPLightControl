package com.errorerrorerror.esplightcontrol.utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class ObservableList<T> {

    protected final List<T> list;
    private final PublishSubject<List<T>> publishSubject;

    public ObservableList() {
        this.list = new ArrayList<>();
        this.publishSubject = PublishSubject.create();
        publishSubject.onNext(this.list);
    }

    public ObservableList(List<T> list){
        this.list = list;
        this.publishSubject = PublishSubject.create();
        publishSubject.onNext(this.list);
    }

    public void reInsertList(){
        publishSubject.onNext(list);
    }

    public boolean add(T value) {
        boolean t = list.add(value);
        publishSubject.onNext(list);
        return t;
    }

    public boolean remove(Object o){
        boolean t = list.remove(o);
        publishSubject.onNext(list);
        return t;
    }

    public List<T> getList(){
        return list;
    }

    public boolean contains(T obj){
        return list.contains(obj);
    }

    public PublishSubject<List<T>> getObservableList() {
        return publishSubject;
    }
}

