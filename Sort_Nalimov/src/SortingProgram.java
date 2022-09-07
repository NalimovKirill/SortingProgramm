import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author nalimov
 */
public class SortingProgram {

    public static void main(String[] args) {

        if(args.length < 3) {
            System.out.println("Указаны не верные аргументы");
            exit();
        }
        // Указатель начала файлов
        var filesIndex = 3;

        // Раскидывем агрументы
        var sortType = (String)args[0];
        var dataType = (String)args[1];
        var outputFile = (String)args[2];

        // Проверка на наличие аргумента типа сортировки
        if(!sortType.equals("-a") && !sortType.equals("-d")) {
            // Проверка на наличие аргумента типа данных в типе сортировке
            if(sortType.equals("-s") || sortType.equals("-i")) {
                System.out.println("Не усказан тип сортировки, по умалчанию будет установлен тип ASK");

                // Смещаем данные
                outputFile = dataType;
                dataType = sortType;
                // Смещаем указатель, так как на 1 аргумет было меньше
                filesIndex--;
            }
            else {
                System.out.println("Усказан не верный тип сортировки, по умалчанию будет установлен тип ASK");
            }

            // Устанавливаем значение по умолчание
            sortType = "-a";
        }

        if(!dataType.equals("-s") && !dataType.equals("-i")) {
            System.out.println("Усказан не верный тип данны.");
            exit();
        }

        if(outputFile.isEmpty())
        {
            System.out.println("Усказан не верный путь выходного файла");
            exit();
        }

        var inputFiles = new ArrayList<String>();

        // Сохраняем входные файлы
        for (int i = filesIndex; i < args.length; i++) {
            inputFiles.add(args[i]);
        }
        // Установка режима сортировки
        var isAsc = sortType.equals("-a");
        var out = new ArrayList<String>();

        // Перебираем файлы
        for(var file: inputFiles)
        {
            // Счётчик строк для отображения в ошибке
            var rowCouner = 0;
            var rows = new ArrayList<String>();

            // Читаем файл
            try(var reader = new BufferedReader(new FileReader(file))) {
                while(reader.ready()) {
                    rowCouner++;
                    var row = reader.readLine();
                    // Пропускаем строки содержащие пробел
                    if(row.contains(" ")) {
                        System.out.println("Строка " + rowCouner + " в файле " + file + " содержит пробел, пропускаем строку.");
                    }
                    else {
                        rows.add(row);
                    }
                }
                reader.close();
            }
            catch(FileNotFoundException e){
                System.out.println("Файл " + file + " не найден.");
                continue;
            }
            catch (IOException e) {
                System.out.println("Произошла непредвиденная ошибка. " + e.getMessage());
                continue;
            }

            // Добавляем прошлый результат для правильной сортировки
            rows.addAll(out);
            out.clear();

            // Сортировка для чисел
            if(dataType.equals("-i")) {
                // Конвертируем строки в числа
                var intArray = convertStringArrayToIntArray(rows);
                // Сортируем
                Collections.sort(intArray);
                // Разворачиваем если сортировка по убыванию
                if(!isAsc)
                {
                    Collections.reverse(intArray);
                }
                // Сохраняем
                for (int i: intArray) {
                    out.add(Integer.toString(i));
                }
            }
            // Сортировка для строк
            else if(dataType.equals("-s")) {
                // Сортируем
                Collections.sort(rows);
                // Разворачиваем если сортировка по убыванию
                if(!isAsc)
                {
                    Collections.reverse(rows);
                }
                // Сохраняем
                out.addAll(rows);
            }
        }
        // Сохраняем результат в файл
        try(var writer = new FileWriter(outputFile))
        {
            for (var row: out) {
                writer.write(row + "\n");
            }
        }
        catch (IOException e) {
            System.out.println("Ошибка записи: " + e.getMessage());
        }

        exit();
    }

    // Конвертирует сисок строк в список чисел
    // Строки, которые не могут быть приобразованы в число, пропускаются
    private static ArrayList<Integer> convertStringArrayToIntArray(ArrayList<String> arrayList){
        var result = new ArrayList<Integer>();

        for(String a: arrayList)
        {
            try {
                var i = Integer.parseInt(a);
                result.add(i);
            }
            catch (NumberFormatException e) {
                System.out.println("Не удалось преобразовать значение " + a + " в число.");
            }
        }

        return result;
    }

    // Выход с задержкой
    private static void exit() {
        try {
            System.in.read();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
