package com.zeba.event;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zeba.event.annotation.Receiver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZebaBroad {
    private static volatile ZebaBroad myBroad;
    private static Context context;
    private static Map<Object,List<ZebaBroadHolder>> mapHodlers=new HashMap<Object,List<ZebaBroadHolder>>();
    public static void init(Application application){
        context=application;
    }
    public static ZebaBroad get(){
        if (myBroad == null) {
            synchronized (ZebaBroad.class) {
                if (myBroad == null) {
                    myBroad = new ZebaBroad();
                }
            }
        }
        return myBroad;
    }

    public void register(Object obj){
        if(context==null){
            return;
        }
        Class cls=obj.getClass();
        Method[] methods= cls.getDeclaredMethods();
        for(int i=0;i<methods.length;i++){
            Method method=methods[i];
            if(method.isAnnotationPresent(Receiver.class)){
                Receiver receiver=method.getAnnotation(Receiver.class);
                String action=receiver.value();
                if(!"".equals(action)){
                    ZebaBroadHolder holder=new ZebaBroadHolder(action,method,obj);
                    if(mapHodlers.get(obj)==null){
                        List<ZebaBroadHolder> list=new ArrayList<ZebaBroadHolder>();
                        mapHodlers.put(obj,list);
                    }
                    mapHodlers.get(obj).add(holder);

                    IntentFilter intentFilter=new IntentFilter();
                    intentFilter.addAction(action);
                    context.registerReceiver(holder,intentFilter);
                }
            }
        }
    }

    public void unregister(Object obj){
        List<ZebaBroadHolder> list=mapHodlers.get(obj);
        if(list==null||context==null){
            return;
        }
        for(ZebaBroadHolder holder:list){
            context.unregisterReceiver(holder);
        }
        list.clear();
        mapHodlers.remove(obj);
    }

    public void send(String action){
        send(action,null);
    }

    public void send(String action,Object obj){
        if(context==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(action);
        if(obj!=null){
            intent.putExtra("className",obj.getClass().getName());
            intent.putExtra("data", GsonUtil.Instance().toJson(obj));
        }
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(String action){
        get().send(action);
    }

    public static void sendBroadcast(String action,Object obj){
        get().send(action,obj);
    }
}
