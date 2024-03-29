package com.aulaspring.SB_projetocurso.services.validation.utils;

public class BR {
	
	// CPF
    private static final int[] WEIGHT_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    // CNPJ
    private static final int[] WEIGHT_CNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static int recursiveSum(int[] weight, char[] chr, int number) {
        if (number <= 0) return 0;
        final int chrIndex = number - 1;
        final int weightIndex = weight.length > chr.length ? number : chrIndex;
        return (recursiveSum(weight, chr, chrIndex) +
                Character.getNumericValue(chr[chrIndex]) * weight[weightIndex]);
    }

    private static int calculate(final String str, final int[] weight) {
        final char[] chr = str.toCharArray();
        int sum = recursiveSum(weight, chr, chr.length);
        sum = 11 - (sum % 11);
        return sum > 9 ? 0 : sum;
    }

    private static boolean checkEquals(String tfn, int length, int[] weight) {
        final String number = tfn.substring(0, length);
        final int digit1 = calculate(number, weight);
        final int digit2 = calculate(number + digit1, weight);
        return tfn.equals(number + digit1 + digit2);
    }

    /**
     * Valida CPF
     *
     * @param cpf
     * @return
     */
    public static boolean isValidCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches(cpf.charAt(0) + "{11}")) return false;
        return checkEquals(cpf, 9, WEIGHT_CPF);
    }

    /**
     * Valida CNPJ
     *
     * @param cnpj
     * @return
     */
    public static boolean isValidCnpj(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}") || cnpj.matches(cnpj.charAt(0) + "{14}")) return false;
        return checkEquals(cnpj, 12, WEIGHT_CNPJ);
    }
}


