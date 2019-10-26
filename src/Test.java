import java.util.TreeMap;

class Test{
    public static void main(String[] args) {
        TreeMap<String, String> s1 = new TreeMap<String, String>();
        s1.put("a", "b");
        TreeMap<String, String> s2 = (TreeMap<String, String>)s1.clone();
        s1.put("a", "c");
        System.out.println(s2.get("a"));
        System.out.println(s1.get("a"));
    }
}
