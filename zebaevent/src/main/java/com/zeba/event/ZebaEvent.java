package com.zeba.event;

import com.zeba.event.annotation.Subscriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZebaEvent {
    private static volatile ZebaEvent myBroad;
    private Map<String,List<ZebaEventHolder>> mapHodlers=new HashMap<String,List<ZebaEventHolder>>();

    public static ZebaEvent get(){
        if (myBroad == null) {
            synchronized (ZebaEvent.class) {
                if (myBroad == null) {
                    myBroad = new ZebaEvent();
                }
            }
        }
        return myBroad;
    }

    public void register(Object obj){
        Class cls=obj.getClass();
        Method[] methods= cls.getDeclaredMethods();
        for(int i=0;i<methods.length;i++){
            Method method=methods[i];
            if(method.isAnnotationPresent(Subscriber.class)){
                Subscriber receiver=method.getAnnotation(Subscriber.class);
                String action=receiver.value();
                if(!"".equals(action)){
                    ZebaEventHolder holder=new ZebaEventHolder(method,obj);
                    if(mapHodlers.get(obj)==null){
                        List<ZebaEventHolder> list=new ArrayList<ZebaEventHolder>();
                        mapHodlers.put(action,list);
                    }
                    mapHodlers.get(obj).add(holder);
                }
            }
        }
    }

    public void unregister(Object obj){
        List<ZebaEventHolder> list=mapHodlers.get(obj);
        if(list==null){
            return;
        }
        for(ZebaEventHolder holder:list){
            holder.clear();
        }
        list.clear();
        mapHodlers.remove(obj);
    }

    public void send(String action){
        send(action,null);
    }

    public void send(final String action,final Object data){
        List<ZebaEventHolder> list=mapHodlers.get(action);
        if(list==null){
            return;
        }
        for(ZebaEventHolder holder:list){
            holder.onReceive(data);
        }
    }

    public static void sendEvent(String action){
        get().send(action);
    }

    public static void sendEvent(String action,Object obj){
        get().send(action,obj);
    }
}
