package java7;

import com.sandwich.koan.Koan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sandwich.util.Assert.assertEquals;

public class AboutDiamondOperator {

    @Koan
    public void diamondOperator() {
        String[] animals = {"Dog", "Cat", "Tiger", "Elephant", "Zebra"};
        //Generic type of array list inferred - empty <> operator
        List<String> animalsList = new ArrayList<>(Arrays.asList(animals));
        List<String> newAnimalList = createNewAnimalList();
        assertEquals(animalsList, newAnimalList);
    }

    @Koan
    public void diamondOperatorInMethodCall() {
        String[] animals = {"Dog", "Cat", "Tiger", "Elephant", "Zebra"};
        //type of new ArrayList<>() inferred from method parameter
        List<String> animalsList = fill(new ArrayList<>());
        List<String> newAnimalList = createNewAnimalList();
        assertEquals(animalsList, newAnimalList);
    }

    private List<String> fill(List<String> list) {
        String[] animals = {"Dog", "Cat", "Tiger", "Elephant", "Zebra"};
        list.addAll(Arrays.asList(animals));
        return list;
    }

    private static List<String> createNewAnimalList() {
        List<String> newAnimalList = new ArrayList<String>();
        newAnimalList.add("Dog");
        newAnimalList.add("Cat");
        newAnimalList.add("Tiger");
        newAnimalList.add("Elephant");
        newAnimalList.add("Zebra");
        return newAnimalList;
    }
}
