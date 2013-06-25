package no.bera.springyES.util;

import no.bera.springyES.projection.NoHandlerFoundException;
import no.bera.springyES.projection.annotations.Handler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class HandlerReflectionUtil {

    public static Method getHandelingMethod(Class type, Class param, Class beanClass, boolean scanForSuper) {
        List<Method> methods = Arrays.asList(beanClass.getMethods());

        Method handelingMethod;
        if (scanForSuper)
            handelingMethod = getMethodWithSuperScan(type, param, methods);
        else
            handelingMethod = getMethod(param, methods);

        if (handelingMethod == null)
            throw new NoHandlerFoundException("No " + type.getName() + " handler for " + param.getName() + " could be found in " + beanClass.getName());
        else
            return handelingMethod;
    }

    private static Method getMethod(Class paramClass, List<Method> methods) {
        return findAssignableMethod(methods, paramClass);
    }

    private static Method getMethodWithSuperScan(Class type, Class param, List<Method> methods) {
        Method handelingMethod = null;
        Class<?> candidateClass = param;

        while (handelingMethod == null || !candidateClass.isAssignableFrom(type)){
            handelingMethod = findAssignableMethod(methods, candidateClass);
            candidateClass = candidateClass.getSuperclass();
        }
        return handelingMethod;
    }

    private static Method findAssignableMethod(List<Method> methods, Class<?> paramClass) {
        for (Method m : methods) {
            if (m.isAnnotationPresent(Handler.class) && paramClass.isAssignableFrom(getParam(m)))
                return m;
        }
        return null;
    }

    private static Class<?> getParam(Method candidateMethod) {
        List<Class<?>> paramClass = Arrays.asList(candidateMethod.getParameterTypes());

        if (paramClass.size() > 1)
            throw new RuntimeException("Handler " + candidateMethod.getName() + " has more than one parameter. A handler should have exactly one parameter.");
        else if (paramClass.isEmpty())
            throw new RuntimeException("Handler " + candidateMethod.getName() + " has no parameters. A handler should have exactly one parameter.");
        return paramClass.get(0);
    }

}
