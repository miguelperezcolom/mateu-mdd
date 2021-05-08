package io.mateu.mdd;

public class Mateu {

    public static <T> T getInstance(Class<T> type) {
        System.out.println("If CDI or Spring available, you can also inject");
        try {
            return (T) Class.forName(type.getName() + "Impl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
