package bp;

import java.util.*;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String[] input_data = {"C100_1-100", "C200_1-120-1200", "C300_1-120-30", "C400_1-80-20",
                "C100_2-50", "C200_2-40-1000", "C300_2-200-45", "C400_2-10-20", "C100_3-10",
                "C200_3-170-1100", "C300_3-150-29", "C400_3-100-28", "C100_1-300", "C200_1-100-750", "C300_1-32-15"};

        Car[] cars = new Car[input_data.length];
        for(int i=0; i<input_data.length; i++) {
            cars[i] = new Car(input_data[i]);
        }

        for (Car car : cars) {
            System.out.println(car);
        }

        System.out.println("Cars type 100 summary consumption cost");
        System.out.println(Gas_Cons_by_Type(cars,"100"));
        System.out.println("Cars type 200 summary consumption cost");
        System.out.println(Gas_Cons_by_Type(cars,"200"));
        System.out.println("Cars type 300 summary consumption cost");
        System.out.println(Gas_Cons_by_Type(cars,"300"));
        System.out.println("Cars type 400 summary consumption cost");
        System.out.println(Gas_Cons_by_Type(cars,"400"));
        System.out.println("Cars summary consumption cost");
        System.out.println(Gas_Cons_by_Type(cars,null));

        System.out.println("Max Consumption cost at Cars type " + Get_MaxMin_Cons_type(cars, true));
        System.out.println("Min Consumption cost at Cars type " + Get_MaxMin_Cons_type(cars, false));

        System.out.println("Cars type 100 sorted");
        Show_Car_List_by_Type(cars,"100");
        System.out.println("Cars type 200 sorted");
        Show_Car_List_by_Type(cars,"200");
        System.out.println("Cars type 300 sorted");
        Show_Car_List_by_Type(cars,"300");
        System.out.println("Cars type 400 sorted");
        Show_Car_List_by_Type(cars,"400");
    }

    public static Double Gas_Cons_by_Type(Car p_cars[], String p_car_type) {
        double sum = 0;
        double gas_cons = 0;
        for (Car car : p_cars) {
            if (p_car_type==null||car.type.equals(p_car_type)) {
                gas_cons = car.mileage_km * car.GasCons100() * car.GasCost() / 100;
                sum += gas_cons;
            }
        }
        return sum;
    };

    public static String Get_MaxMin_Cons_type(Car p_cars[], boolean isMax) {
        Map<String, Double> v_types = new HashMap<String, Double>();
        double sum_gas_cons;
        double gas_cons;
        String v_max_type = "null";
        double v_max_type_value = 0;
        for (Car car : p_cars) {
            gas_cons = car.mileage_km * car.GasCons100() * car.GasCost() / 100;
            if (v_types.containsKey(car.type)) {
                sum_gas_cons = v_types.get(car.type);
            }else{
                sum_gas_cons = 0;
            }
            v_types.put(car.type, sum_gas_cons + gas_cons);
        }
        for(Map.Entry<String, Double> entry : v_types.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if (isMax && (v_max_type.equals("null") || value > v_max_type_value)) {
                v_max_type_value = value;
                v_max_type = key;
            }
            if (!isMax && (v_max_type.equals("null") ||value < v_max_type_value)) {
                v_max_type_value = value;
                v_max_type = key;
            }
        }
        return v_max_type;
    };

    public static void Show_Car_List_by_Type(Car p_cars[], String p_type) {
        List<Car> items = new ArrayList();
        for (Car car : p_cars) {
            if (p_type.equals(car.type)) {
                items.add(car);
            }
        }
        Comparator<Car> comparatorByAddData =
                (e1, e2) -> Comparator.<String>nullsFirst(Comparator.naturalOrder()).compare(e1.add_data, e2.add_data);
        Comparator<Car> comparatorByMileage_km =
                (e1, e2) -> Comparator.<Integer>nullsFirst(Comparator.naturalOrder()).compare(e1.mileage_km, e2.mileage_km);
        Comparator<Car> comparatorByParams =
            (e1, e2) -> {
                if (e1.mileage_km == e2.mileage_km)
                    return Comparator.nullsLast(comparatorByAddData).compare(e1, e2);
                else
                    return Comparator.nullsLast(comparatorByMileage_km).compare(e1, e2);
            };
        items.sort(comparatorByParams);
        for (Car item : items) {
            System.out.println(item);
        }
    }

    private static class Car {
        private String type;
        private int gos_number;
        private int mileage_km;
        private String add_data;

        Map<String, Double> car_gas_cost = new HashMap<String, Double>() // стоимость литра по типу авто
        {{
            put("100", 46.10);
            put("200", 48.90);
            put("300", 47.50);
            put("400", 48.90);
        }};

        Map<String, Double> car_gas_cons100 = new HashMap<String, Double>() // расход топлива по типу авто на 100км
        {{
            put("100", 12.5);
            put("200", 12.0);
            put("300", 11.5);
            put("400", 20.0);
        }};

        public Car(String p_data) {
            Pattern pattern = Pattern.compile("_|-");
            String[] v_params = pattern.split(p_data.substring(1));
            int index = 0;
            for (String v_param : v_params) {
                switch (index++) {
                    case 0: type = v_param; break;
                    case 1: gos_number = Integer.parseInt(v_param); break;
                    case 2: mileage_km = Integer.parseInt(v_param); break;
                    case 3: add_data = v_param; break;
                    default:
                }
            }
        }

        public Double GasCost() {
            return car_gas_cost.get(this.type);
        };

        public Double GasCons100() {
            return car_gas_cons100.get(this.type);
        };

        public int GetMileage_km() {
            return this.mileage_km;
        }

        public String GetAddData() {
            return this.add_data;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "type='" + type + '\'' +
                    ", gos_number=" + gos_number +
                    ", mileage_km=" + mileage_km +
                    ", add_data='" + add_data + '\'' +
                    ", gas_cost=" + GasCost() +
                    ", gas_cons100=" + GasCons100() +
                    '}';
        }
    }
}
