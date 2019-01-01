package com.zeba.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class ZebaBroadHolder extends BroadcastReceiver{
    private String action;
    private Method method;
    private WeakReference<Object> object;
    public ZebaBroadHolder(String action, Method method, Object obj){
        this.action=action;
        this.method=method;
        this.method.setAccessible(true);
        this.object=new WeakReference<Object>(obj);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent==null){
            return;
        }
        if(!intent.getAction().equals(action)){
            return;
        }
        try {
            Class[] clss=method.getParameterTypes();
            if(clss.length==0){
                method.invoke(object.get());
            }else{
                String className=intent.getStringExtra("className");
                String data=intent.getStringExtra("data");
                Class cls=Class.forName(className);
                if(clss.length==1&&data!=null&&className!=null){
                    Object obj= GsonUtil.Instance().fromJson(data,cls);
                    if(obj.getClass()==clss[0]){
                        method.invoke(object.get(),obj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
