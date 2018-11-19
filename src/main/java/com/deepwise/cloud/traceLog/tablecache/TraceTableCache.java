package com.deepwise.cloud.traceLog.tablecache;


import com.deepwise.cloud.traceLog.config.DwLogConfig;
import com.deepwise.cloud.traceLog.util.HttpUtils;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;

/**
 * @Author: Zzaki
 * @Description:
 * @Date: Created on 2018/8/21
 * @Company: DeepWise
 */
public class TraceTableCache {

    protected long tickTime = 60 * 1000L;

    private long lastUpdateTime = System.currentTimeMillis();

    private Table<String, String, Object> table;

    public TraceTableCache(Long tickTime){
        this.table = HashBasedTable.create();
        this.tickTime = tickTime;
        TraceTableCache tableCache = this;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateToRemote(tableCache);
            }
        },1000,tickTime);
    }

    public boolean contains(String rowKey,String columnKey){
        return this.table.contains(rowKey,columnKey);
    }

    public Object get(String rowKey,String columnKey){
        return this.table.get(rowKey,columnKey);
    }

    public boolean isEmpty(){
        return this.table.isEmpty();
    }

    public int size(){
        return this.table.size();
    }

    public void clear(){
        this.table.clear();
    }

    public Object put(String rowKey,String columnKey,Object value){
        return this.table.put(rowKey,columnKey,value);
    }

    public Object remove(String rowKey,String columnKey){
        return this.table.remove(rowKey,columnKey);
    }

    public Map<String,Object> row(String rowKey){
        return this.table.row(rowKey);
    }

    public Set<String> rowKeySet(){
        return this.table.rowKeySet();
    }

    public Map<String,Map<String ,Object>> rowMap(){
        return this.table.rowMap();
    }

    private void updateToRemote(TraceTableCache trace){
        if (System.currentTimeMillis() - lastUpdateTime > tickTime){
            List<Object> traceList = new ArrayList<>();
            table.rowMap().forEach((rowKey,columnMap)->{
                columnMap.forEach((k,v)->{
                    traceList.add(v);
                });
            });
            HttpUtils.doPost(DwLogConfig.getGraylogGelfUrl(),traceList.toArray());
            table.clear();
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    @Override
    public String toString() {
        return table.toString();
    }
}
