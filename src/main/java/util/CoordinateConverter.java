package util;

/**
 * Конвертация координат поля из строки в индекс массива и обратно.
 */
public class CoordinateConverter {

    public static int[] parseCoordinate(String input) {
        if (input == null || input.length() < 2 || input.length() > 3)
            throw new IllegalArgumentException("Неверный формат координат. Используйте A1-J10");
        char letter = Character.toUpperCase(input.charAt(0));
        String numberStr = input.substring(1);
        if (letter < 'A' || letter > 'J') throw new IllegalArgumentException("Буква должна быть от A до J");
        int number;
        try { number = Integer.parseInt(numberStr); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Неверный числовой формат"); }
        if (number < 1 || number > 10) throw new IllegalArgumentException("Число должно быть от 1 до 10");
        return new int[]{number-1, letter-'A'};
    }

    public static String toCoordinateString(int x, int y) {
        if (x < 0 || x >= 10 || y < 0 || y >= 10) throw new IllegalArgumentException("Координаты вне диапазона");
        return "" + (char)('A'+y) + (x+1);
    }
}
