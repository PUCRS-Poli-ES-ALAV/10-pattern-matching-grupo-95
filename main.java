public class main {
    // Saved match info (set when a full match is found)
    public static boolean foundFullMatch = false;
    public static int foundAt = -1;
    public static int[] savedS1Positions = null;
    public static int[] savedS2Positions = null;
    public static void main(String[] args) {
        String s1 = "ababcabcabababd";
        String s2 = "ababd";
        try {
            int result = search(s1, s2);
            System.out.println(result);
        } catch (Exception e) {
            
            e.printStackTrace();
        }
      
       
    }



    public static int[] patternEx1(String s1, String s2){


        int tam1 = s1.length();
        int tam2 = s2.length();

        if(tam2 > tam1){
            return null;
        }
        if(tam1 == 0 || tam2 == 0){
            return null;
        }

    int possibleStarts = tam1 - tam2 + 1;
    int[] values = new int[possibleStarts];

        for(int i = 0; i <= tam1 - tam2; i++){
            int matchedChars = 0;
            for(int j = 0; j < tam2; j++){
                if(s1.charAt(i + j) != s2.charAt(j)){
                    break;
                }
                matchedChars++;
            }

            values[i] = matchedChars; // number of consecutive matching chars starting at i

           
            if(matchedChars == tam2){
                foundFullMatch = true;
                foundAt = i;
                savedS1Positions = new int[tam2];
                savedS2Positions = new int[tam2];
                for(int k = 0; k < tam2; k++){
                    savedS1Positions[k] = i + k;
                    savedS2Positions[k] = k;
                }
                break; // stop searching further
            }
        }


        return values;
    }

    //rabin karp  para evitar resolver os problemas na maioria dos casos em N^n
    private static int search(String pat, String txt) {
        int M = pat.length();// 1 atribuicao e 1 consulta
        int N = txt.length();// 1 e 1
        long patHash = hash(pat, M); // 1 atribuicao e 1 consulta
        System.out.println(patHash);
            for (int i = 0; i <= N - M; i++) {// N-M+1 iteracoes, cada iteracao 1 atribuicao e 2 consultas
                long txtHash = hash(txt.substring(i, i+M), M); //2 atribuicoes e 1 consulta
                if (patHash == txtHash) //umca omparacao
                    return i; // ocorrência? colisão?
            }
            return N; // nenhuma ocorrência
}

private static long hash(String s, int M) {
   long h = 0;
   long Q = 101; // valorprimo
   long R = 256; //valor da tabela ascii
   for (int j = 0; j < M; j++)
      h = (h * R + s.charAt(j)) % Q;// a forma de atribuir, no estado inicial h ta em zero
      //conforme se avança vai preenchendo o hash
   return h;
}

//No pior dos casos como dito no slide e demonstrado no codigo, a complexidade é O(NM) por conta das
//múltiplas colisoes na montagem da lista. No melhor dos casos é O(N + M) quando não há colisões, ideal para
//casos pequenos
}
