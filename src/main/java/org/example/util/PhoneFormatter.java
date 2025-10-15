package org.example.util;

import org.springframework.stereotype.Component;

@Component
public class PhoneFormatter {

    /**
     * Форматирует телефон согласно маске компании
     * @param phone введенный номер телефона (может быть с символами или без)
     * @param mask маска формата телефона (например, "+7 (***) ***-**-**")
     * @return отформатированный номер
     * @throws IllegalArgumentException если номер не соответствует маске
     */
    public String formatPhone(String phone, String mask) {
        // Извлекаем только цифры из введенного номера
        String digitsOnly = phone.replaceAll("\\D", "");

        // Извлекаем префикс из маски (например, "7" из "+7" или "375" из "+375")
        String prefixWithPlus = mask.substring(0, mask.indexOf('(')).trim();
        String prefix = prefixWithPlus.replace("+", "");

        // Подсчитываем количество звездочек в маске (это количество цифр после префикса)
        int requiredDigits = mask.replaceAll("[^*]", "").length();

        // Проверяем, начинается ли номер с префикса
        String phoneDigits;
        if (digitsOnly.startsWith(prefix)) {
            phoneDigits = digitsOnly.substring(prefix.length());
        } else {
            phoneDigits = digitsOnly;
        }

        // Проверяем количество цифр
        if (phoneDigits.length() != requiredDigits) {
            throw new IllegalArgumentException("Некорректно введен номер, формат " + mask);
        }

        // Форматируем номер согласно маске
        StringBuilder result = new StringBuilder();
        int digitIndex = 0;

        for (char c : mask.toCharArray()) {
            if (c == '*') {
                result.append(phoneDigits.charAt(digitIndex++));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Нормализует номер телефона для поиска в БД (удаляет все символы кроме цифр)
     */
    public String normalizePhone(String phone) {
        return phone.replaceAll("\\D", "");
    }
}
