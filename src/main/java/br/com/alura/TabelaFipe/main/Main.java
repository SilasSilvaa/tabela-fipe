package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Data;
import br.com.alura.TabelaFipe.model.Models;
import br.com.alura.TabelaFipe.model.Vehicle;
import br.com.alura.TabelaFipe.service.ApiConsumption;
import br.com.alura.TabelaFipe.service.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final String URL = "https://parallelum.com.br/fipe/api/v1/";
    private static final ConvertData convertData = new ConvertData();
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {

        String address = "";

        System.out.println(""" 
                *** Options ****
                __________________
                | 1  Car         |
                | 2  Motorcycle  |
                | 3  Truck       |
                ------------------
                type an option for consult:
                """);
        int option = scanner.nextInt();

        switch (option) {
            case 1 -> address = URL + "carros/marcas";
            case 2 -> address = URL + "motos/marcas";
            case 3 -> address = URL + "caminhoes/marcas";
            default -> System.out.println("Type an valid option ");
        }

        String json = ApiConsumption.getData(address);
        System.out.println(json);

        List<Data> brands = convertData.getList(json, Data.class);

        brands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("Type brand code for consult");
        String brandCode = scanner.next();

        address = address + "/" + brandCode + "/modelos";
        json = ApiConsumption.getData(address);
        Models modelsList = convertData.gettingData(json, Models.class);

        System.out.println("Models from this brand");

        modelsList.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("Type name of the car");
        String nameCar = scanner.next();

        List<Data> filteredModels = modelsList.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nameCar.toLowerCase()))
                .toList();

        System.out.println("Filtered models");
        filteredModels.forEach(System.out::println);

        System.out.println("Enter a model code to search for values for the classification");
        String modelCode = scanner.next();

        address = address + "/" + modelCode + "/anos";
        json = ApiConsumption.getData(address);
        List<Data> years = convertData.getList(json, Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for(Data data : years ){
            String addressYears = address + "/" + data.codigo();
            json = ApiConsumption.getData(addressYears);
            Vehicle vehicle = convertData.gettingData(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("All vehicles filtered by year rating ");
        vehicles.forEach(System.out::println);
    }
}
