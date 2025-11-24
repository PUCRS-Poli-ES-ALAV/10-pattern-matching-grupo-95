import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.List;
import java.util.List;

public class Main {

    // Saved match info (set when a full match is found)
    public static boolean foundFullMatch = false;
    public static int foundAt = -1;
    public static int[] savedS1Positions = null;
    public static int[] savedS2Positions = null;

  
    public static void main(String[] args) {
        String s1 = "ababcabcabababd";
        String s2 = "ababd";


            runBenchmarks(s1, s2);
            
            
        int[] valores = patternEx1(s1, s2);
        System.out.println("patternEx1 (quantidade de chars casando em cada posição):");
        for (int i = 0; i < valores.length; i++) {
            System.out.printf("i = %d -> %d%n", i, valores[i]);
        }
        if (foundFullMatch) {
            System.out.println("Full match encontrado em i = " + foundAt);
        }
        
        
        
        
        System.out.println("\npatternMatch (naive):");
        patternMatch(s2, s1);
        
        
        System.out.println("\nRabin-Karp simples (hash Horner):");
        int pos = search(s2, s1);
        if (pos != s1.length()) {
            System.out.println("Padrão encontrado em: " + pos);
        } else {
            System.out.println("Padrão não encontrado.");
        }
        
        
        System.out.println("\nDouble hash (prefixos) – exemplo de uso:");
        Main rk = new Main();
        rk.RabinKarpHash(s1); // pré-processa a string s1
        
        
        if (pos != s1.length()) {
            ArrayList<Integer> subHash = rk.getSubHash(pos, pos + s2.length() - 1);
            System.out.println("Hash duplo do substring s1[" + pos + ".." + (pos + s2.length() - 1) + "]: " + subHash);
        }
        
            // E strings grandes:
            String big = "a".repeat(1_000_000);
            String pat = "a".repeat(5000);
            runBenchmarks(big, pat);
    }
    
    
    public static void patternMatch(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        // precisa ser <=, senão perde a última posição possível
        for (int i = 0; i <= N - M; i++) {
            int j;
            // aqui o limite é M (tamanho do padrão), não N
            for (j = 0; j < M; j++) {
                // txt deslocado por i, pat começa em 0
                if (txt.charAt(i + j) != pat.charAt(j)) {
                    break;
                }
            }

            if (j == M) {
                System.out.println("Padrão encontrado em " + i);
            }
        }
    }

   
    public static int[] patternEx1(String s1, String s2) {
        int tam1 = s1.length();
        int tam2 = s2.length();

        if (tam2 > tam1) {
            return null;
        }
        if (tam1 == 0 || tam2 == 0) {
            return null;
        }

        int possibleStarts = tam1 - tam2 + 1;
        int[] values = new int[possibleStarts];

        for (int i = 0; i <= tam1 - tam2; i++) {
            int matchedChars = 0;
            for (int j = 0; j < tam2; j++) {
                if (s1.charAt(i + j) != s2.charAt(j)) {
                    break;
                }
                matchedChars++;
            }

        
            values[i] = matchedChars;

            if (matchedChars == tam2) {
                foundFullMatch = true;
                foundAt = i;
                savedS1Positions = new int[tam2];
                savedS2Positions = new int[tam2];
                for (int k = 0; k < tam2; k++) {
                    savedS1Positions[k] = i + k;
                    savedS2Positions[k] = k;
                }
                break; 
            }
        }

        return values;
    }


    private static int search(String pat, String txt) {
        int M = pat.length(); 
        int N = txt.length(); 
        long patHash = hash(pat, M); 
        System.out.println("Hash do padrão: " + patHash);

        for (int i = 0; i <= N - M; i++) {
            // substring de txt do tamanho M
            long txtHash = hash(txt.substring(i, i + M), M);
            if (patHash == txtHash) {
                // Pode ser ocorrência ou colisão
                // (aqui não estamos verificando caractere a caractere de novo)
                return i;
            }
        }
        return N; // nenhuma ocorrência
    }

    // Hash Horner
    private static long hash(String s, int M) {
        long h = 0;
        long Q = 101;  // valor primo
        long R = 256;  // tamanho da tabela ASCII
        for (int j = 0; j < M; j++) {
            h = (h * R + s.charAt(j)) % Q;
            // forma de atribuir: no estado inicial h=0 e vai sendo preenchido
        }
        return h;
    }

 
    
    private final int mod1 = (int) 1e9 + 7;
    private final int mod2 = (int) 1e9 + 9;
    private final int base1 = 31;
    private final int base2 = 37;

    private int[] hash1, hash2;
    private int[] power1, power2;

    // modular addition
    private int add(int a, int b, int mod) {
        a += b;
        if (a >= mod) a -= mod;
        return a;
    }

    // modular subtraction
    private int sub(int a, int b, int mod) {
        a -= b;
        if (a < 0) a += mod;
        return a;
    }

    // modular multiplication
    private int mul(int a, int b, int mod) {
        return (int) ((1L * a * b) % mod);
    }

    // convert character to int (assumindo letras minúsculas 'a'..'z')
    private int charToInt(char c) {
        return c - 'a' + 1;
    }

    // pré-processa a string s e gera prefix-hash duplo
    public void RabinKarpHash(String s) {
        int n = s.length();
        hash1 = new int[n];
        hash2 = new int[n];
        power1 = new int[n];
        power2 = new int[n];

        hash1[0] = charToInt(s.charAt(0));
        hash2[0] = charToInt(s.charAt(0));
        power1[0] = 1;
        power2[0] = 1;

        for (int i = 1; i < n; ++i) {
            hash1[i] = add(mul(hash1[i - 1], base1, mod1),
                           charToInt(s.charAt(i)), mod1);
            power1[i] = mul(power1[i - 1], base1, mod1);

            hash2[i] = add(mul(hash2[i - 1], base2, mod2),
                           charToInt(s.charAt(i)), mod2);
            power2[i] = mul(power2[i - 1], base2, mod2);
        }
    }

 
    public ArrayList<Integer> getSubHash(int l, int r) {
        int h1 = hash1[r];
        int h2 = hash2[r];
        if (l > 0) {
            h1 = sub(h1, mul(hash1[l - 1], power1[r - l + 1], mod1), mod1);
            h2 = sub(h2, mul(hash2[l - 1], power2[r - l + 1], mod2), mod2);
        }

        ArrayList<Integer> res = new ArrayList<>();
        res.add(h1);
        res.add(h2);
        return res;
    }




      public static int kmpSearch(String pattern, String text) {
        if (pattern == null || text == null) return -1;
        int m = pattern.length();
        int n = text.length();
        if (m == 0 || n == 0 || m > n) return -1;

        int[] lps = buildLPS(pattern); // prefixo/sufixo mais longo
        int i = 0; // índice em text
        int j = 0; // índice em pattern

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
                if (j == m) {
                    // match completo termina em i-1
                    return i - j;
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1]; // "volta" no padrão
                } else {
                    i++;            // anda no texto
                }
            }
        }
        return -1; // não encontrou
    }


    public static List<Integer> kmpAllOccurrences(String pattern, String text) {
        List<Integer> result = new ArrayList<>();
        if (pattern == null || text == null) return result;
        int m = pattern.length();
        int n = text.length();
        if (m == 0 || n == 0 || m > n) return result;

        int[] lps = buildLPS(pattern);
        int i = 0; // índice em text
        int j = 0; // índice em pattern

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
                if (j == m) {
                    // encontrou um match que termina em i-1
                    result.add(i - j);
                    j = lps[j - 1]; // continua procurando
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return result;
    }

    /**
     * Constrói a tabela LPS (Longest Prefix Suffix) do padrão.
     * lps[i] = tamanho do maior prefixo próprio que também é sufixo em pattern[0..i]
     */
    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // comprimento do prefixo/sufixo atual
        int i = 1;   // lps[0] = 0, começamos do índice 1

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    // volta para o último melhor prefixo/sufixo
                    len = lps[len - 1];
                } else {
                    // não há prefixo/sufixo, fica 0
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // ------------------------------------------------------------
//  BENCHMARK PARA TODOS OS ALGORITMOS
// ------------------------------------------------------------
static class BenchmarkResult {
    String name;
    long iterations;
    long instructions;
    double millis;

    BenchmarkResult(String name, long iterations, long instructions, double millis) {
        this.name = name;
        this.iterations = iterations;
        this.instructions = instructions;
        this.millis = millis;
    }
}

public static void runBenchmarks(String text, String pattern) {
    System.out.println("========================================");
    System.out.println("Benchmark: text N=" + text.length() + ", pattern M=" + pattern.length());
    System.out.println("========================================");

    List<BenchmarkResult> results = new ArrayList<>();

    // ---------------------------
    // NAIVE
    // ---------------------------
    long iter = 0, instr = 0;
    long t0 = System.nanoTime();

    for (int i = 0; i <= text.length() - pattern.length(); i++) {
        iter++;
        int j;
        for (j = 0; j < pattern.length(); j++) {
            instr++;
            if (text.charAt(i + j) != pattern.charAt(j)) break;
        }
        instr++;
    }

    long t1 = System.nanoTime();
    results.add(new BenchmarkResult("Naive", iter, instr, (t1 - t0) / 1e6));

    // ---------------------------
    // RABIN-KARP SIMPLES
    // ---------------------------
    iter = instr = 0;
    t0 = System.nanoTime();

    long patHash = hash(pattern, pattern.length());
    instr++;

    for (int i = 0; i <= text.length() - pattern.length(); i++) {
        iter++;
        long txtHash = hash(text.substring(i, i + pattern.length()), pattern.length());
        instr += 5; // custo aproximado do hash
        if (txtHash == patHash) instr++;
    }

    t1 = System.nanoTime();
    results.add(new BenchmarkResult("Rabin-Karp Simples", iter, instr, (t1 - t0) / 1e6));

    // ---------------------------
    // RABIN-KARP ROLLING
    // ---------------------------
    iter = instr = 0;
    t0 = System.nanoTime();

    long pHash = hash(pattern, pattern.length());
    long tHash = hash(text.substring(0, pattern.length()), pattern.length());

    long R = 256;
    long Q = 101;
    long RM = 1;
    for (int i = 1; i < pattern.length(); i++) RM = (RM * R) % Q;

    for (int i = 0; i <= text.length() - pattern.length(); i++) {
        iter++;
        if (tHash == pHash) instr++;

        if (i < text.length() - pattern.length()) {
            tHash = (tHash + Q - RM * text.charAt(i) % Q) % Q;
            tHash = (tHash * R + text.charAt(i + pattern.length())) % Q;
            instr += 6;
        }
    }

    t1 = System.nanoTime();
    results.add(new BenchmarkResult("Rabin-Karp Rolling", iter, instr, (t1 - t0) / 1e6));

    // ---------------------------
    // KMP
    // ---------------------------
    iter = instr = 0;
    t0 = System.nanoTime();

    int[] lps = buildLPS(pattern);

    int i = 0, j = 0;
    while (i < text.length()) {
        iter++;
        if (text.charAt(i) == pattern.charAt(j)) {
            i++; j++;
            instr += 2;
            if (j == pattern.length()) {
                instr++;
                j = lps[j - 1];
            }
        } else {
            if (j != 0) j = lps[j - 1];
            else i++;
            instr += 3;
        }
    }

    t1 = System.nanoTime();
    results.add(new BenchmarkResult("KMP", iter, instr, (t1 - t0) / 1e6));

    // ---------------------------
    // IMPRIMIR TABELA
    // ---------------------------
    System.out.println("\nAlgoritmo               Iterações        Instruções        Tempo (ms)");
    for (BenchmarkResult r : results) {
        System.out.printf("%-22s %-15d %-18d %.3f\n",
                r.name, r.iterations, r.instructions, r.millis);
    }
    System.out.println();
}


}


