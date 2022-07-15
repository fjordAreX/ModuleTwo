package Lesson2;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MyList<T extends Number> implements List<T> {
    private int size;
    private T[] arr;
    private final Class<T> CLASS;

    public MyList(Class<T> clazz) {
        this.CLASS = clazz;
        this.arr = (T[]) Array.newInstance(CLASS, 10);
    }

   public double getDouble(int index) throws Exception {
        if (CLASS == Double.class || CLASS.getSuperclass().getSimpleName().equals("Object")){
            if (checkIndex(index)){
                return (double) get(index);
            }else{
                throw new IndexOutOfBoundsException();
            }
        }
        throw new Exception("Class is not Double");
    }

    public int sumIntegers() throws Exception {
        if (CLASS==Integer.class || CLASS.getSuperclass().getSimpleName().equals("Object")){
            int count = 0;
            for (int i = arr.length-size; i <arr.length ; i++) {
                count += (int)arr[i];
            }
            return count;
        }
        throw new Exception("At least one element is not Integer");
    }

    public boolean add(T x) {
        if (arr.length == size ) {
            T[] arrTemp = (T[]) Array.newInstance(CLASS, ((int) (arr.length * 1.5)));

            for (int i = arr.length - 1; i >= 0; i--) {
                arrTemp[i+ (arrTemp.length - arr.length)] = arr[i];
            }
            arr = arrTemp;
        }
        size++;
        arr[arr.length - size] = x;
        return true;
    }

    @Override
    public void add(int index, T element) {
        if(checkIndex(index)) {
            T[] firstPart = Arrays.copyOfRange(arr, 0, arr.length - size+index);
            T[] secondPart = Arrays.copyOfRange(arr, arr.length - size+index, arr.length);
            T[] arrTemp = (T[]) Array.newInstance(CLASS, firstPart.length+secondPart.length+1);

            for (int i = 0; i < firstPart.length; i++) {
                arrTemp[i]=firstPart[i];
            }
            arrTemp[firstPart.length]=element;

            for (int i =firstPart.length+1,c=0; i < arrTemp.length; i++,c++) {
                arrTemp[i]=secondPart[c];
            }
            arr = arrTemp;
            size++;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean result = false;
        for (int i = arr.length-size; i < arr.length ; i++) {
            if (arr[i].equals(o)){
                result = true;
            arr[i]=null;
            size--;
            }
        }
        sortNull();
        return result;
    }

    @Override
    public T remove(int index) {
        if(checkIndex(index)) {
            T result = arr[arr.length - size+index];
            T[] firstPart = Arrays.copyOfRange(arr, 0, arr.length - size+index);
            T[] secondPart = Arrays.copyOfRange(arr, arr.length - size+index+1, arr.length);
            T[] tempArr = (T[]) Array.newInstance(CLASS, firstPart.length+secondPart.length);

            for (int j = 0; j < firstPart.length; j++) {
                tempArr[j] = firstPart[j];
            }
            for (int j = 0; j < secondPart.length; j++) {
                tempArr[j + firstPart.length] = secondPart[j];
            }
            size--;
            arr = tempArr;
            return result;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void clear() {
        Arrays.fill(arr, null);
        size=0;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int count=0;
        for (int i = arr.length - size; i < arr.length; i++) {
            if (o.equals(arr[i])){
                count++;
            }
        }
        return count >= 2;
    }

    @Override
    public T get(int index) {
        if(checkIndex(index)) {
            return arr[arr.length - size + index];
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public T set(int index, T element) {
        if(checkIndex(index)) {
            return arr[arr.length - size+index]=element;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int indexOf(Object o) {
        if (!checkContains(o)){
            return -1;
        }
        for (int i = arr.length - size; i <arr.length ; i++) {
            if(arr[i].equals(o)){
                return i-(arr.length - size);
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!checkContains(o)){
            return -1;
        }
        for (int i = arr.length-1; i >= arr.length - size ; i--) {
            if(arr[i].equals(o)){
                return i-(arr.length - size);
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        if (size==0){
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = arr.length-size; i <arr.length ; i++) {
            sb.append(arr[i]+", ");
        }
        return sb.substring(0,sb.length()-2) + "]" ;
    }

    private boolean checkIndex(int index){
        return index <= size && index >= 0;
    }

    private boolean checkContains(Object o){
        for (int i = 0; i < size; i++) {
            if (o.equals(arr[arr.length - size+i])){
                return true;
            }
        }
        return false;
    }

    private void sortNull() {

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] == null) {
                if (i + 1 >= arr.length) {
                    T[] firstPart = Arrays.copyOfRange(arr, 0, i);
                    T[] tempArr = (T[]) Array.newInstance(CLASS, firstPart.length+1);
                    tempArr[0]=arr[i];
                    for (int j = 1; j < firstPart.length; j++) {
                        tempArr[j]= firstPart[j];
                    }
                    arr = tempArr;
                } else {
                    T[] firstPart = Arrays.copyOfRange(arr, 0, i);
                    T[] secondPart = Arrays.copyOfRange(arr, i, i + 1);
                    T[] thirdPart = Arrays.copyOfRange(arr, i + 1, arr.length);
                    T[] tempArr = (T[]) Array.newInstance(CLASS, firstPart.length + secondPart.length + thirdPart.length);

                    for (int j = 0; j < secondPart.length; j++) {
                        tempArr[j] = secondPart[j];
                    }
                    for (int j = 0; j < firstPart.length; j++) {
                        tempArr[j + secondPart.length] = firstPart[j];
                    }
                    for (int j = 0; j < thirdPart.length; j++) {
                        tempArr[j + secondPart.length + firstPart.length] = thirdPart[j];
                    }
                    arr = tempArr;
                }
            }
        }
    }


    // Не менял дальше

    @Override
    public MyList<T> subList(int fromIndex, int toIndex) {
        if (checkIndex(fromIndex)&&checkIndex(toIndex)){
            return new MyList<>(CLASS);
        }
        return null;
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return List.super.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        List.super.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        List.super.sort(c);
    }

    @Override
    public Spliterator<T> spliterator() {
        return List.super.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return List.super.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return List.super.parallelStream();
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        List.super.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return List.super.toArray(generator);
    }

}