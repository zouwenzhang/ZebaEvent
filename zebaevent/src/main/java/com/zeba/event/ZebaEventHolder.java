package com.zeba.event;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

public class ZebaEventHolder {
    private Method method;
    private WeakReference<Object> object;
    public ZebaEventHolder(Method method, Object obj){
        this.method=method;
        this.method.setAccessible(true);
        this.object=new WeakReference<Object>(obj);
    }

    public void clear(){
        this.method=null;
        if(object!=null){
            object.clear();
        }
        object=null;
    }
    public void onReceive(Object data) {
        try {
            Class[] clss=method.getParameterTypes();
            if(clss.length==0&&data==null){
                method.invoke(object.get());
            }else if(clss.length==1){
                if(clss[0]==data.getClass()){
                    method.invoke(object.get(),data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
