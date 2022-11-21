package com.learn.controller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2022-11-15
 * @desc
 */
@RestController
@RequestMapping(value = "/long-pull/")
public class LongPullController {
    Multimap<String,DeferredResult<String>> watchMap= Multimaps.synchronizedMultimap(HashMultimap.create());
    @GetMapping(path="watch/{id}")
    public DeferredResult<String> watch(@PathVariable String id){
        DeferredResult<String>deferredResult=new DeferredResult<>(30000L);
        deferredResult.onCompletion(()->{
            watchMap.remove(id,deferredResult);
        });
        watchMap.put(id,deferredResult);
        return deferredResult;
    }

    @GetMapping(path="publish/{id}")
    public String publish(@PathVariable String id){
        Collection<DeferredResult<String>> deferredResults = watchMap.get(id);
        if(CollectionUtils.isEmpty(deferredResults)){
            return "empty";
        }
        for (DeferredResult<String> deferredResult : deferredResults) {
            deferredResult.setResult("更新:"+System.currentTimeMillis());
        }
        return "success";
    }
}
