package com.infinity.architecture.utils.nfe;


import com.infinity.architecture.utils.variables.VarUtils;

public class NfeUtils {

    /**
     * CALCULA O DIGITO VERIFICADOR DA CHAVE DE ACESSO (APENAS BPE POR ENQUANTO)
     * @param chave     STRING DA CHAVE DE ACESSO APENAS NUMEROS
     * @return          DIGITO VERIFICADOR DA CHAVE DE ACESSO INTEIRO
     */
    public static int calculaDvChaveBpe(String chave) throws NumberFormatException {
        // RECEBE O DIGITO VERIFICADOR DA CHAVE
        int dv = 0;
        // CONVERTE A CHAVE PARA UM ARRAY DE CARACTERES
        char[] chaveChars = chave.toCharArray();
        // ARMAZENA O RESULTADO DO CALCULO DA CHAVE
        int result = 0;
        // MULTIPLICADOR ATUAL DA POSICAO[2, 3, 4, 5, 6, 7, 8]
        int multiplicador = 2;
        // PARA CADA DIGITO DA CHAVE DE ACESSO
        for (int i = chaveChars.length - 1; i > -1; i--) {
            // CONVERTE O DIGITO DA CHAVE DE ACESSO PARA INT
            int numAt = Integer.parseInt(String.valueOf(chaveChars[i]));
            // REALIZA O CALCULO
            int resultAt = numAt * multiplicador;
            // SALVA E ATUALIZA O RESULTADO
            result += resultAt;

            // REINICIA OU SOMA O MULTIPLICADOR
            if (multiplicador == 9) {
                multiplicador = 2;
            } else {
                multiplicador++;
            }
        }

        // ARMAZENA O RESTO DA DIVISAO
        float resto = 0;
        // SE O RESTO FOR MENOR QUE 2 DV É IGUAL A 0
        if ((resto = result % 11) < 2) {
            dv = 0;
        } else { // SENAO DV = (11 - RESTO)
            dv = (int)(11 - resto);
        }

        // RETORNA O DIGITO VERIFICADOR
        return dv;
    }

    /**
     * GERA A CHAVE DA BPE
     * @param codUfIni      COD UF DE ORIGEM
     * @param anoMesAAMM    ANO E MES NO FORMATO yyMM
     * @param cnpjEmis      CNPJ DO EMITENTE
     * @param modelo        MODELO DA NOTA
     * @param serie         SERIE DA NOTA
     * @param numNota       NUMERO DA NOTA
     * @param tipoEmis      TIPO DE EMISSAO
     * @param codBp         CODIGO DO BILHETE DE PASSAGEM
     * @return              STRING 44 DIGITOS DA CHAVE DA BPE JÁ COM O DV(DIGITO VERIFICADOR)
     * @throws Exception
     */
    public static String geraChaveBpe(
        String codUfIni, String anoMesAAMM, String cnpjEmis, String modelo, String serie, String numNota, String tipoEmis, String codBp
    ) throws Exception {
        // ARMAZENA A CHAVE DE ACESSO
        StringBuilder strChave = new StringBuilder();

        if (codUfIni != null && codUfIni.trim().length() > 0) {
            // COD UF INICIAL RECEBIDO
            if (anoMesAAMM != null && anoMesAAMM.trim().length() > 0) {
                // AAMM RECEBIDO
                if (cnpjEmis != null && cnpjEmis.trim().length() > 0) {
                    // CNPJ DE EMISSAO RECEBIDO
                    if (modelo != null && modelo.trim().length() > 0) {
                        // MODELO DA NOTA RECEBIDO
                        if (serie != null && serie.trim().length() > 0) {
                            // SERIE DA NOTA RECEBIDO
                            if (numNota != null && numNota.trim().length() > 0) {
                                // NUMERO DA NOTA RECEBIDO
                                if (tipoEmis != null && tipoEmis.trim().length() > 0) {
                                    // TIPO EMISSAO RECEBIDO
                                    if (codBp != null && codBp.trim().length() > 0) {
                                        // COD BPE RECEBIDO
                                        // GERA A CHAVE
                                        strChave.append(codUfIni).append(anoMesAAMM).append(cnpjEmis).append(modelo).append(serie).append(numNota).append(tipoEmis).append(codBp);
                                        // GERA O DIGITO VERIFICADOR DA CHAVE
                                        strChave.append(calculaDvChaveBpe(strChave.toString()));
                                    } else {
                                        // COD BPE NAO RECEBIDO
                                        throw new Exception("geraChaveBpe(): codBp não recebido!");
                                    }
                                } else {
                                    // TIPO EMISSAO NÃO RECEBIDO
                                    throw new Exception("geraChaveBpe(): tipoEmis não recebido!");
                                }
                            } else {
                                // NUMERO DA NOTA NÃO RECEBIDO
                                throw new Exception("geraChaveBpe(): numNota não recebido!");
                            }
                        } else {
                            // SERIE DA NOTA NÃO RECEBIDO
                            throw new Exception("geraChaveBpe(): serie não recebido!");
                        }
                    } else {
                        // MODELO DA NOTA NAO RECEBIDO
                        throw new Exception("geraChaveBpe(): modelo não recebido!");
                    }
                } else {
                    // CNPJ DE EMISSAO NÃO RECEBIDO
                    throw new Exception("geraChaveBpe(): cnpjEmis não recebido!");
                }
            } else {
                // AAMM NÃO RECEBIDO
                throw new Exception("geraChaveBpe(): anoMesAAMM não recebido!");
            }
        } else {
            // COD UF INICIAL NAO RECEBIDO
            throw new Exception("geraChaveBpe(): codUfIni não recebido!");
        }
        // RETORNA A CHAVE DE ACESSO
        return strChave.toString();
    }

    /**
     * GERA O CÓDIGO DO BILHETE DE PASSAGEM
     * @return  COD BILHETE DE PASSAGEM DE 8 POSICOES ZEROS A ESQUERDAS
     */
    public static int geraCodBp() {
        return VarUtils.getRandomNumberInRange(0, 99999998) + 1;
    }
}
