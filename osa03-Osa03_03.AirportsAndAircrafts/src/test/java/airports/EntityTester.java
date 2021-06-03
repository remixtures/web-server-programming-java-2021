package airports;

import fi.helsinki.cs.tmc.edutestutils.Reflex;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import static org.junit.Assert.*;

public class EntityTester {

    public void testEntity(String className,
            Map<String, Class> attributeAndType,
            Map<String, List<Class>> attributeAnnotations) {
        exists(className);
        isAnEntity(className);
        implementsSerializable(className);
        hasAttributes(className, attributeAndType);
        hasGettersAndSetters(className, attributeAndType);
        hasRequiredAnnotations(className, attributeAnnotations);
    }

    public static List<Class> createAnnotationList(Class... classes) {
        return Arrays.asList(classes);
    }

    private void exists(String className) {
        Reflex.reflect(className);
    }

    private void isAnEntity(String className) {
        assertNotNull("Class " + className + " should have annotation " + Entity.class.getName() + ".", Reflex.reflect(className).cls().getAnnotation(Entity.class));
    }

    private void implementsSerializable(String className) {
        Class clazz = Reflex.reflect(className).cls();
        assertTrue("Class " + className + " should implement interface " + Serializable.class.getName() + ".", Serializable.class.isInstance(clazz));
    }

    private void hasAttributes(String className, Map<String, Class> attributeAndType) {
        for (String attrName : attributeAndType.keySet()) {
            classHasPrivateAttribute(className, attrName, attributeAndType.get(attrName));
        }
    }

    private void hasGettersAndSetters(String className, Map<String, Class> attributeAndType) {
        for (String attrName : attributeAndType.keySet()) {
            attributeHasGetAndSetMethod(className, attrName, attributeAndType.get(attrName));
        }
    }

    private void hasRequiredAnnotations(String className, Map<String, List<Class>> attributeAnnotations) {
        for (String attrName : attributeAnnotations.keySet()) {
            List<Class> annotations = attributeAnnotations.get(attrName);

            for (Class annotation : annotations) {
                attributeHasAnnotation(className, attrName, annotation);
            }
        }
    }

    private void classHasPrivateAttribute(String clazz, String fieldName, Class type) {
        Field field = findField(clazz, fieldName);

        Class fieldType = field.getType();
        if (!type.isAssignableFrom(fieldType) || !fieldType.isAssignableFrom(type)) {
            fail("Field " + fieldName + " must be type " + type.getName());
        }

        assertTrue("Field " + fieldName + " must be private.", Modifier.isPrivate(field.getModifiers()));
    }

    private void attributeHasGetAndSetMethod(String clazz, String attributeName, Class type) {
        attributeHasGetMethod(attributeName, clazz, type);
        attributeHasSetMethod(attributeName, clazz, type);
    }

    private void attributeHasAnnotation(String clazz, String attributeName, Class annotationClass) {
        Field field = findField(clazz, attributeName);

        Annotation annotation = field.getAnnotation(annotationClass);
        assertNotNull("Field " + attributeName + " in class " + clazz + " should have annotation " + annotationClass.getName(), annotation);
    }

    private void attributeHasGetMethod(String attributeName, String clazz, Class type) {
        Method getMethod = null;
        String getMethodname = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);

        try {
            getMethod = Reflex.reflect(clazz).cls().getDeclaredMethod(getMethodname);
        } catch (NoSuchMethodException e) {
            fail("Verify that the class " + clazz + " has a method called " + getMethodname);
        }

        Class returnType = getMethod.getReturnType();
        if (!type.isAssignableFrom(returnType) || !returnType.isAssignableFrom(type)) {
            fail("Method " + getMethodname + " must return a " + type.getName());
        }

        assertTrue("Method " + attributeName + " must be public.", Modifier.isPublic(getMethod.getModifiers()));
    }

    private void attributeHasSetMethod(String attributeName, String clazz, Class type) {
        Method setMethod = null;
        String setMethodname = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);

        try {
            setMethod = Reflex.reflect(clazz).cls().getDeclaredMethod(setMethodname, type);
        } catch (NoSuchMethodException e) {
            fail("Verify that the class " + clazz + " has a method called " + setMethodname + " that takes a " + type.getName() + " as a parameter.");
        }

        Class returnType = setMethod.getReturnType();
        if (!void.class.isAssignableFrom(returnType) || !returnType.isAssignableFrom(void.class)) {
            fail("Method " + setMethodname + " must be a void-method.");
        }

        assertTrue("Method " + attributeName + " must be public.", Modifier.isPublic(setMethod.getModifiers()));
    }

    public static Field findField(String clazz, String fieldName) {
        Field field = null;
        try {
            field = Reflex.reflect(clazz).cls().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Verify that the class " + clazz + " has an attribute called " + fieldName);
        }

        return field;
    }
}
