package org.example;

import model.Game;
import model.Player;
import model.Ship;
import model.Board;
import util.CoordinateConverter;
import java.util.Scanner;

/**
 * Главный класс игры "Морской бой".
 * Отвечает за запуск игры, управление фазами расстановки кораблей и игровым процессом.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== МОРСКОЙ БОЙ ===");
        System.out.print("Введите имя первого игрока: ");
        String player1Name = scanner.nextLine();
        System.out.print("Введите имя второго игрока: ");
        String player2Name = scanner.nextLine();

        Game game = new Game(player1Name, player2Name);
        game.startGame();

        setupPhase(game, scanner);
        gamePhase(game, scanner);

        scanner.close();
    }

    /**
     * Управляет фазой расстановки кораблей.
     */
    private static void setupPhase(Game game, Scanner scanner) {
        while (game.getState() == Game.GameState.SETUP_PLAYER1
                || game.getState() == Game.GameState.SETUP_PLAYER2) {
            clearScreen();
            System.out.println("=== РАССТАНОВКА КОРАБЛЕЙ ===");
            System.out.println("Игрок: " + game.getCurrentPlayer().getName());
            System.out.println("\n1 - Автоматическая расстановка");
            System.out.println("2 - Ручная расстановка");
            System.out.print("Выберите вариант: ");
            int choice = getIntInput(scanner, 1, 2);

            if (choice == 1) {
                game.placeShipsAutomatically();
                System.out.println("Корабли расставлены автоматически!");
                showFinalBoardPreview(game.getCurrentPlayer(), scanner);
            } else {
                manualShipPlacement(game, scanner);
            }
        }
    }

    /**
     * Управляет ручной расстановкой кораблей.
     */
    private static void manualShipPlacement(Game game, Scanner scanner) {
        Player currentPlayer = game.getCurrentPlayer();
        Board board = currentPlayer.getBoard();
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        int shipsPlaced = 0;

        while (shipsPlaced < shipSizes.length) {
            clearScreen();
            System.out.println("=== РУЧНАЯ РАССТАНОВКА КОРАБЛЕЙ ===");
            System.out.println("Игрок: " + currentPlayer.getName());
            System.out.println("Осталось разместить кораблей: " + (shipSizes.length - shipsPlaced));
            System.out.println("Текущий корабль: размер " + shipSizes[shipsPlaced]);
            System.out.println();
            board.displayForOwner();
            System.out.println("\nРазмещение корабля размером " + shipSizes[shipsPlaced]);
            System.out.println("Введите координаты и ориентацию (V/H):");

            try {
                System.out.print("Координата начала (A1-J10): ");
                String coordInput = scanner.nextLine().trim();
                int[] coordinates = CoordinateConverter.parseCoordinate(coordInput);

                System.out.print("Ориентация (V/H): ");
                String orientationInput = scanner.nextLine().trim().toUpperCase();
                boolean vertical = orientationInput.equals("V") || orientationInput.equals("В");

                Ship ship = new Ship(shipSizes[shipsPlaced]);
                boolean placed = board.placeShip(ship, coordinates[0], coordinates[1], vertical);

                if (placed) {
                    System.out.println("✅ Корабль размером " + shipSizes[shipsPlaced] + " размещен!");
                    shipsPlaced++;
                    waitForEnter(scanner);
                } else {
                    String error = board.getPlacementError(ship, coordinates[0], coordinates[1], vertical);
                    System.out.println("❌ Невозможно разместить корабль здесь! " + error);
                    waitForEnter(scanner);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Ошибка: " + e.getMessage());
                waitForEnter(scanner);
            }
        }

        showFinalBoardPreview(currentPlayer, scanner);
    }

    /**
     * Показывает финальное поле игрока после расстановки.
     */
    private static void showFinalBoardPreview(Player player, Scanner scanner) {
        clearScreen();
        System.out.println("=== ВАШЕ ПОЛЕ ===");
        System.out.println("Игрок: " + player.getName());
        System.out.println("ЗАПОМНИТЕ РАСПОЛОЖЕНИЕ ВАШИХ КОРАБЛЕЙ!");
        System.out.println("Во время игры это поле будет скрыто!");
        System.out.println();
        player.getBoard().displayForOwner();
        System.out.println("\nНажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    /**
     * Основная игровая фаза.
     */
    private static void gamePhase(Game game, Scanner scanner) {
        while (game.getState() == Game.GameState.PLAYING) {
            Player currentPlayer = game.getCurrentPlayer();
            Player opponent = game.getOpponent();

            clearScreen();
            System.out.println("=== ХОД ИГРОКА: " + currentPlayer.getName() + " ===");
            System.out.println("\nПоле противника:");
            opponent.getBoard().displayForOpponent();

            System.out.println("\n" + currentPlayer.getName() + ", ваш ход!");
            System.out.println("Ваши корабли: " + getShipsStatus(currentPlayer.getBoard()));
            System.out.println("Корабли противника: " + getShipsStatus(opponent.getBoard()));

            boolean validMove = false;
            while (!validMove) {
                System.out.print("\nВведите координаты для выстрела (например, A5): ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("show") || input.equalsIgnoreCase("показать")) {
                    showPlayerBoard(currentPlayer, scanner);
                    continue;
                }

                try {
                    int[] coordinates = CoordinateConverter.parseCoordinate(input);
                    Board.ShotResult result = opponent.getBoard().receiveShot(coordinates[0], coordinates[1]);

                    clearScreen();
                    System.out.println("=== РЕЗУЛЬТАТ ВЫСТРЕЛА ===");
                    System.out.println("Игрок: " + currentPlayer.getName());
                    System.out.println("Координаты: " + input);

                    switch (result) {
                        case HIT:
                            System.out.println("🎯 ПОПАДАНИЕ! Продолжайте ход!");
                            break;
                        case SUNK:
                            System.out.println("🎯 ПОПАДАНИЕ! Корабль потоплен! Продолжайте ход!");
                            if (game.checkWinCondition()) {
                                System.out.println("\n🎉🎉🎉 ПОБЕДА! 🎉🎉🎉");
                                System.out.println("Игрок " + currentPlayer.getName() + " потопил все корабли противника!");
                                showFinalBoards(game);
                                return;
                            }
                            break;
                        case MISS:
                            System.out.println("💧 ПРОМАХ! Ход переходит противнику.");
                            game.switchPlayer();
                            break;
                        case INVALID:
                            System.out.println("❌ Неверные координаты! Попробуйте снова.");
                            waitForEnter(scanner);
                            continue;
                        case ALREADY_SHOT:
                            System.out.println("❌ Вы уже стреляли в эту клетку! Попробуйте снова.");
                            waitForEnter(scanner);
                            continue;
                    }

                    System.out.println("\nОбновленное поле противника:");
                    opponent.getBoard().displayForOpponent();

                    validMove = true;
                    waitForEnter(scanner);

                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Ошибка: " + e.getMessage() + " Попробуйте снова.");
                    waitForEnter(scanner);
                }
            }
        }
    }

    private static String getShipsStatus(Board board) {
        int totalShips = board.getShips().size();
        int sunkShips = (int) board.getShips().stream().filter(Ship::isSunk).count();
        int aliveShips = totalShips - sunkShips;
        return aliveShips + " целых / " + sunkShips + " потоплено";
    }

    private static void showFinalBoards(Game game) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ИТОГОВЫЕ ПОЛЯ:");
        System.out.println("=".repeat(50));

        System.out.println("\nПоле " + game.getPlayer1().getName() + ":");
        game.getPlayer1().getBoard().displayForOwner();

        System.out.println("\nПоле " + game.getPlayer2().getName() + ":");
        game.getPlayer2().getBoard().displayForOwner();

        System.out.println("\nНажмите Enter для завершения...");
        new Scanner(System.in).nextLine();
    }

    private static void showPlayerBoard(Player player, Scanner scanner) {
        clearScreen();
        System.out.println("=== ВАШЕ ПОЛЕ ===");
        System.out.println("Игрок: " + player.getName());
        player.getBoard().displayForOwner();
        System.out.println("\nНажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }

    private static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) return input;
                System.out.print("Введите число от " + min + " до " + max + ": ");
            } catch (Exception e) {
                System.out.print("Введите корректное число: ");
                scanner.nextLine();
            }
        }
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.println("Нажмите Enter чтобы продолжить...");
        scanner.nextLine();
    }
}
