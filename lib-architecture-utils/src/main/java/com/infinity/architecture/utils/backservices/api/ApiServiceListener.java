package com.infinity.architecture.utils.backservices.api;


import com.infinity.architecture.utils.backservices.Resource;

public interface ApiServiceListener<T> {
    void onResponse(Resource<T> resource);
}
