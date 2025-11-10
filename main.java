public class main {
    // Saved match info (set when a full match is found)
    public static boolean foundFullMatch = false;
    public static int foundAt = -1;
    public static int[] savedS1Positions = null;
    public static int[] savedS2Positions = null;
    public static void main(String[] args) {
        String s1 = "ababcabcabababd";
        String s2 = "ababd";
        int[] result = patternEx1(s1, s2);
      
        if(result == null){
            System.out.println("No result (pattern longer than text or empty input).\n");
            return;
        }

        for(int i =0 ; i < result.length; i++){
            System.out.println(result[i]);
        }

        if(foundFullMatch){
            System.out.println("Full match found at s1 index: " + foundAt);
            System.out.println("Saved matching positions (s1 -> s2):");
            for(int k = 0; k < savedS1Positions.length; k++){
                System.out.println("s1[" + savedS1Positions[k] + "]='" + s1.charAt(savedS1Positions[k]) + "'  ->  s2[" + savedS2Positions[k] + "]='" + s2.charAt(savedS2Positions[k]) + "'");
            }
        } else {
            System.out.println("No full match was found.");
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
}
