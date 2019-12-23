package com.netease.neeventbus.neeventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {

    private static volatile EventBus instance;
    // 定义一个容器，保存所有的方法
    private Map<Object, List<SubscribleMethod>> cacheMap;
    private Handler mHandler;
    private ExecutorService mExecutorService;

    private EventBus(){
        cacheMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault() {
        if(instance == null){
            synchronized (EventBus.class){
                if(instance == null){
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object obj){
        // 先去数据源中获取subcribleMethods，如果不存在，则去寻找方法并添加
        List<SubscribleMethod> list = cacheMap.get(obj);
        if(list == null){
            list = findSubscribleMethods(obj);
            cacheMap.put(obj,list);
        }
    }

    public List<SubscribleMethod> findSubscribleMethods(Object obj){
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        // 循环去查找父类是否存在subscrible注解方法
        while (clazz != null){

            // 判断当前是否是系统类，如果是，就退出循环
            String name = clazz.getName();
            if(name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")){
                break;
            }

            // 得到所有的方法，这个clazz在本项目中，暂时指代的是MainActivity
            Method[] methods = clazz.getMethods();

            for(Method method : methods){
                // 通过注解找到我们需要注册的方法
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if(subscribe == null){
                    continue;
                }
                // 获取方法中的参数，并判断
                Class<?>[] types = method.getParameterTypes();
                if(types.length != 1){
                    throw new RuntimeException("EventBus只能接受一个参数");
                }
                // 获取线程模式
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method,threadMode,types[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void post(final Object type){
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()){
            final Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for(final SubscribleMethod subscribleMethod : list){
                // 简单的理解：两个列对比一下，看看是否一致 (不严谨的说法)
                // a（subscribleMethod.getType()）对象所对应的类信息，是b（type.getClass()）对象所对应的类信息的父类或者父接口
                if(subscribleMethod.getType().isAssignableFrom(type.getClass())){
                    switch (subscribleMethod.getThreadMode()){
                        // 不管你在post是在主线程 还是在子线程，我都在主线程接受
                        case MAIN:
                            // 主 - 主
                            if(Looper.myLooper() == Looper.getMainLooper()){
                                invoke(subscribleMethod,obj,type);
                            }else{
                                // 子 - 主
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod,obj,type);
                                    }
                                });
                            }
                            break;
                        // 不管你在post是在子线程还是在主线程，我都在子线程接受
                        case BACKGROUND:
                            // 主 - 子
                            if(Looper.myLooper() == Looper.getMainLooper()){
                                mExecutorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod,obj,type);
                                    }
                                });
                            }else{
                                // 子 - 子
                                invoke(subscribleMethod,obj,type);
                            }
                            break;
                    }





                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object type) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(obj,type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
