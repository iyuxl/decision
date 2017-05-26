package com.shj;

import com.google.common.collect.Lists;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiaoyaolan on 2017/4/10.
 */
public class Test {
    public static void main(String[] args) {
        int a = 200, b = 200;
        System.out.println(a == b);
        Integer c = 200, d = 200;
        System.out.println(c == d);
        Integer e = 20, f = 20;
        System.out.println(e == f);
        int g = 20, h = 20;
        System.out.println(g == h);
        List<String> lists = Lists.newArrayList("a", "b", "c");
        Collections.sort(lists, (s1, s2) -> s1.compareTo(s2));
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

        String name = "Runoob";
        Integer result = null;

        try {
            nashorn.eval("print('" + name + "')");
            result = (Integer) nashorn.eval("10 + 2");

        }catch(ScriptException e1){
            System.out.println("执行脚本错误: "+ e1.getMessage());
        }

        System.out.println(result.toString());

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println(filtered);
    }
}
