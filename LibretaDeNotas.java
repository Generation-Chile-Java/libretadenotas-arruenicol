package genc182502;

import java.util.*;

public class LibretaDeNotas {
    public static final String red = "\033[1;31m";
    public static final String green = "\033[1;32m";
    public static final String reset = "\033[1;37m";

    public static double notaAprobatoria = 4.0;
    public static void main(String[] args) {
        HashMap<String, ArrayList<Double>> listaNotas = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        System.out.println("¡Bienvenido! \nIngrese la cantidad de alumnos: ");
        int cantAlumnos = sc.nextInt();

        while (cantAlumnos < 2) {
            System.out.print("Ingrese al menos 2 alumnos: ");
            cantAlumnos = sc.nextInt();
        }

        System.out.println("Ingrese la cantidad de notas por alumno: ");
        int cantNotasPorAlumno = sc.nextInt();
        sc.nextLine();

        while (cantNotasPorAlumno <= 0) {
            System.out.print("Ingrese al menos 1 nota.");
            cantNotasPorAlumno = sc.nextInt();
        }

        for (int i = 0; i < cantAlumnos; i++) {
            System.out.println("Ingrese el nombre del alumno " + (i + 1) + ":");
            String nombreAlumno = sc.nextLine().trim();
            nombreAlumno = capitalizeFirstLetter(nombreAlumno);

            ArrayList<Double> notas = new ArrayList<>();

            for (int j = 0; j < cantNotasPorAlumno; j++) {
                System.out.println("Ingrese la nota " + (j + 1) + ":");
                double nota = validarNota(sc);
                notas.add(nota);
            }
            listaNotas.put(nombreAlumno, notas);
        }
        mostrarResumenNotas(listaNotas);
        menu(listaNotas);
        sc.close();
    }

    public static void menu(HashMap<String, ArrayList<Double>> listaNotas) {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println(
                    "\nMenú: "+
                    "\n1. Mostrar el Promedio de Notas por Estudiante.\n" +
                    "2. Mostrar si la Nota es Aprobatoria o Reprobatoria por Estudiante.\n" +
                    "3. Mostrar si la Nota está por Sobre o por Debajo del Promedio del Curso por Estudiante.\n" +
                    "0. Salir del Menú.\n");
            int opcion = sc.nextInt();
            switch (opcion) {
                case 0:
                    salir = true;
                    break;
                case 1:
                    promedioEstudiantes(listaNotas);
                    break;
                case 2:
                    aprobadoReprobado(sc, listaNotas);
                    break;
                case 3:
                    comparacionPromedioCurso(sc, listaNotas);
                    break;
                default:
                    System.out.println("\nOpcion inválida\n");
                    break;

            }
        }
        sc.close();
    }

    public static double obtenerNotaMaxima(ArrayList<Double> notas) {
        double max = Double.MIN_VALUE;
        // compara cada nota y reasigna el valor de la variable
        for (double nota : notas) {
            if (nota > max) {
                max = nota;
            }
        }
        return max;
    }

    public static double obtenerNotaMinima(ArrayList<Double> notas) {
        double min = Double.MAX_VALUE;
        // compara cada nota y reasigna el valor de la variable
        for (double nota : notas) {
            if (nota < min) {
                min = nota;
            }
        }
        return min;
    }

    public static double calcularPromedio(ArrayList<Double> notas) {
        double suma = 0;
        // recorre la lista de notas y las suma
        for (double nota : notas) {
            suma += nota;
        }
        //si no hay notas, el prmedio es 0. si hay notas divide por cant de notas
        return notas.isEmpty() ? 0 : suma / notas.size();
    }

    public static void mostrarResumenNotas(HashMap<String, ArrayList<Double>> listaNotas) {
        for (Map.Entry<String, ArrayList<Double>> entry : listaNotas.entrySet()) {
            String estudiante = entry.getKey();
            ArrayList<Double> notas = entry.getValue();

            // llamado a funcion del calculo del promedio
            double promedio = calcularPromedio(notas);

            // llamado a funciones para calcular min y max
            double max = obtenerNotaMaxima(notas);
            double min = obtenerNotaMinima(notas);

            System.out.printf("Estudiante: %s - Promedio: %.2f - Nota Máxima: %.2f - Nota Mínima: %.2f\n",
                    estudiante, promedio, max, min);
        }
    }

    public static void promedioEstudiantes(HashMap<String, ArrayList<Double>> listaNotas) {
        System.out.println("\nPromedio de Notas: \n");
        for (Map.Entry<String, ArrayList<Double>> entry : listaNotas.entrySet()) {
            //llama la funcion de calculo de promedio y lo calcula por cada estudiante
            double promedio = calcularPromedio(entry.getValue());
            System.out.printf("Alumno: %s --- Promedio: %.2f\n", entry.getKey(), promedio);
        }
    }

    public static void comparacionPromedioCurso(Scanner sc, HashMap<String, ArrayList<Double>> listaNotas) {
        System.out.println("\nSobre o por Debajo del Promedio Curso: \n");
        System.out.print("Ingrese el nombre del estudiante: ");
        sc.nextLine();
        String nombre = sc.nextLine().trim();
        nombre = capitalizeFirstLetter(nombre);

        if (!listaNotas.containsKey(nombre)) { //verifica que el alumno existe
            System.out.println("El estudiante no existe en el sistema.\n");
            return;
        }

        System.out.print("Ingrese el número de la nota a comparar (1, 2, 3...): ");
        int indiceNota = sc.nextInt() - 1; //recibe el indice y le resta porque empieza en 0

        if (indiceNota < 0 || indiceNota >= listaNotas.get(nombre).size()) {
            System.out.println("Número de nota inválido.\n");
            return;
        }

        // obtiene la nota según indice
        double notaEstudiante = listaNotas.get(nombre).get(indiceNota);

        // calcula el promedio de todos los estudiantes según esa nota
        double sumaNotas = 0;
        int cantidadNotas = 0;

        for (ArrayList<Double> notas : listaNotas.values()) {
            if (indiceNota < notas.size()) {
                sumaNotas += notas.get(indiceNota); //obtiene el total de notas
                cantidadNotas++;
            }
        }

        double promedioNotaEspecifica = sumaNotas / cantidadNotas; //calcula el promedio de la nota especifica

        // comparacion de la nota con el promedio curso
        if (notaEstudiante > promedioNotaEspecifica) {
            System.out.println("La nota está por " + green + "sobre" + reset + " el promedio del curso ("+ promedioNotaEspecifica+").");
        } else if (notaEstudiante < promedioNotaEspecifica) {
            System.out.println("La nota está por " + red + "debajo" + reset + " del promedio del curso ("+ promedioNotaEspecifica+").");
        } else {
            System.out.println("La nota está " + green + "igual" + reset + " al promedio del curso ("+ promedioNotaEspecifica+").");
        }
    }

    public static void aprobadoReprobado(Scanner sc, HashMap<String, ArrayList<Double>> listaNotas) {
        System.out.println("\nNota Aprobatoria o Reprobatoria: \n");
        System.out.print("Ingrese el nombre del estudiante: ");
        sc.nextLine();
        String nombre = sc.nextLine().trim();
        nombre = capitalizeFirstLetter(nombre);

        // verificar si el estudiante existe
        if (listaNotas.containsKey(nombre)) {
            ArrayList<Double> notas = listaNotas.get(nombre);

            // imprime las notas con su índice +1
            System.out.println("\nNotas de " + nombre + ":");
            for (int i = 0; i < notas.size(); i++) {
                System.out.printf("Nota %d: %.2f\n", i + 1, notas.get(i));
            }

            System.out.print("\nIngrese el número de la nota a verificar (1, 2, ...): ");
            int indiceNota = sc.nextInt() - 1;

            // validar índice
            if (indiceNota < 0 || indiceNota >= notas.size()) {
                System.out.println("Número de nota inválido.\n");
                return;
            }
            double nota = notas.get(indiceNota); //obtiene la nota

            // verifica si la nota es aprobatoria o reprobatoria
            if (nota >= notaAprobatoria) {
                System.out.println("La nota " + (indiceNota + 1) + " es " + green + "aprobatoria" + reset + ".\n");
            } else {
                System.out.println("La nota " + (indiceNota + 1) + " es " + red + "reprobatoria" + reset + ".\n");
            }

        } else {
            System.out.println("El estudiante " + nombre + " no existe.\n");
        }
    }

    public static double validarNota(Scanner sc) {
        double nota;
        do {
            while (!sc.hasNextDouble()) {
                System.out.println("Nota inválida. Intentelo de nuevo: ");
                sc.next();
            }
            nota = sc.nextDouble();

            if (nota < 1.0 || nota > 7.0) {
                System.out.println("La nota debe estar entre 1.0 y 7.0. Intentelo de nuevo: ");
            }
        } while (nota < 1.0 || nota > 7.0);
        sc.nextLine();
        return nota;
    }

    public static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}